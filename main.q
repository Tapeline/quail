use "pkg/test.q" as test
use "pkg/test.q" as t
import "/home/tapeline/JavaProjects/Files/Quailv2/qlibs/pkg/adder.q"

test.setmsg("hi".upper())

add(1, 2)

t.hello()

print("begin test")

function handler(evt)
    print("Handle", evt)

callEvent("event")
registerHandler("event", handler, true)
callEvent("event")
removeHandler("event", handler)
callEvent("event")

print("test end")