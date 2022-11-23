#

use "lang/qml" as qml

w = qml.screen.Window("Hello", 640, 480)
w.setResizable(false)
w.setVisible(true)

while w.isVisible() {}

#