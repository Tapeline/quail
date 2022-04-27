use canvas

out(canvas)

class Plotter {

	object builder(me) {
		return me
	}

	method plot(me, f, name, range, size, precision, coordstep) {
		coordstep = coordstep or 1
		out(size)
		w = size.get(0)
		h = size.get(1)
		from = range.get(0)
		to = range.get(1)
		plot = canvas.newcanvas(name, w, h)
		centerx = w // 2
		centery = h // 2
		plot.text(  "Plot of function from " + tostring(from) + " to " +
			tostring(to) + " precision=" + tostring(precision),
			{font = "SansSerif", size = 14},
			10, 60,
			255, 0, 0)
		plot.text(  "y ",
			{font = "Serif", size = 14},
			centerx, 40,
			255, 0, 0)
			plot.text(  "x ",
			{font = "Serif", size = 14},
			w - 14, centery,
			255, 0, 0)
		plot.line(0, centery, w, centery, 0, 0, 255)
		
		x = centerx
		through 0:to as i {
			plot.line(x, centery + 5, x, centery - 5, 0, 0, 255)
			plot.text(tostring(i),
				{font = "Serif", size = 14},
				x, centery + 14,
				255, 0, 0)
			x = x + precision
		}
		x = centerx
		through 0:from as i {
			plot.line(x, centery + 5, x, centery - 5, 0, 0, 255)
			plot.text(tostring(i),
				{font = "Serif", size = 14},
				x, centery + 14,
				255, 0, 0)
			x = x - precision
		}
		
		y = centery
		through 0:to as i {
			plot.line(centerx + 5, y, centerx - 5, y, 0, 0, 255)
			plot.text(tostring(i),
				{font = "Serif", size = 14},
				x, centery + 14,
				255, 0, 0)
			y = y - precision
		}
		y = centery
		through 0:from as i {
			plot.line(centerx + 5, y, centerx - 5, y, 0, 0, 255)
			plot.text(tostring(i),
				{font = "Serif", size = 14},
				centerx + 14, y,
				255, 0, 0)
			y = y + precision
		}
		
		plot.line(centerx, 0, centerx, h, 0, 0, 255)
		
		list points
		through from:to step 1/precision as i
			points.add([(i*precision).round(), (f(i)*precision).round()])
		
		every i in 0:(points.size() - 2) {
			plot.line(  centerx + points.get(i).get(0),
				h - (centery + points.get(i).get(1)),
				centerx + points.get(i + 1).get(0),
				h - (centery + points.get(i + 1).get(1)),
				0, 255, 0)
		}
		plot.update()
	}
}

function f(x) {
    return x^(x^x)
}

plotter = Plotter()
plotter.plot(f, "Quail Canvas Test", [-5, 5], [800, 800], 200)
