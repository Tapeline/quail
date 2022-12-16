use "lang/qml" as qml

screen = qml.screen.Window("Test", 800, 600)
surface = qml.screen.Surface(800, 600)
screen.setVisible(true)

while (screen.isVisible()) {
    if (screen.mousePressed()) {
        surface.setColor(0, 255, 0)
        surface.fillRect(10, 10, 100, 50)
    } else {
        surface.setColor(255, 0, 0)
        surface.fillRect(10, 10, 100, 50)
    }
    if (screen.keyboardAnyPressed()) {
        surface.setColor(0, 255, 0)
        surface.fillRect(10, 50, 100, 50)
    } else {
        surface.setColor(255, 0, 0)
        surface.fillRect(10, 50, 100, 50)
    }
    if (screen.keyboardIsShift()) {
        surface.setColor(0, 0, 255)
        surface.fillRect(10, 50, 100, 50)
    }
    print(screen.keyboardGetPressedKeys())
    screen.stampSurface(surface)
}