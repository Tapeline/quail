use "lang/qml" as qml

pi = 3.14159265359

function plotter(f, name, range, size, posStep) {
    posStep = posStep or 1
    w = size.get(0)
    h = size.get(1)
    precision = w // (abs(range[0]) + abs(range[1]))
    from = range.get(0)
    to = range.get(1)
    centerX = w // 2
    centerY = h // 2
    
    plotWindow = qml.screen.Window(name, size[0], size[1])
    plot = qml.screen.Surface(size[0], size[1])

    plot.setColor(255, 0, 0)
    plot.drawText(
        10, 60,
        "Plot of function from " + string(from) + " to " + string(to) + " precision=" + string(precision),
        "SansSerif", qml.typography.REGULAR, 14
    )

    plot.setColor(255, 0, 0)
    plot.drawText(
        centerX, 40,
        "y ",
        "Serif", qml.typography.REGULAR, 14
    )

    plot.setColor(255, 0, 0)
    plot.drawText(
        w - 14, centerY,
        "x ",
        "Serif", qml.typography.REGULAR, 14
    )

    plot.setColor(0, 0, 255)
    plot.drawLine(0, centerY, w, centerY)
    plot.drawLine(centerX, 0, centerX, h)
    
    x = centerX
    through 0:+to as i {
        plot.drawLine(x, centerY + 5, x, centerY - 5, 0, 0, 255)
        plot.drawText(
            x, centerY + 14,
            string(i),
            "Serif", qml.typography.REGULAR, 14
        )
        x = x + precision
    }
    
    x = centerX
    through 0:+from as i {
        plot.drawLine(x, centerY + 5, x, centerY - 5, 0, 0, 255)
        plot.drawText(
            x, centerY + 14,
            string(i),
            "Serif", qml.typography.REGULAR, 14
        )
        x = x - precision
    }

    y = centerY
    through 0:+from as i {
        plot.drawLine(centerX + 5, y, centerX - 5, y)
        plot.drawText(
            centerX + 14, y,
            string(i),
            "Serif", qml.typography.REGULAR, 14
        )
        y = y - precision
    }

    y = centerY
    through 0:+to as i {
        plot.drawLine(centerX + 5, y, centerX - 5, y)
        plot.drawText(
            centerX + 14, y,
            string(i),
            "Serif", qml.typography.REGULAR, 14
        )
        y = y + precision
    }

    points = []
    through from:+to : posStep as i
        points.add([(i*precision).round(), (f(i)*precision).round()])

    every i in 0:(points.size() - 1) {
        plot.setColor(0, 255, 0)
        plot.drawLine(
            centerX + points.get(i).get(0),
            h - (centerY + points.get(i).get(1)),
            centerX + points.get(i + 1).get(0),
            h - (centerY + points.get(i + 1).get(1))
        )
    }

    plotWindow.setVisible(true)
    plotWindow.stampSurface(plot)
    while (plotWindow.isVisible()) {}
}

function f(x) {
    return x^(x^x)
}

function f2(x) {
    x = x + 2
    return abs(sin(x^x) / 2^(x^x-pi/2) / pi)
}


plotter(f2, "Quail Canvas Test", [-2, 2], [500, 500], 0.1)