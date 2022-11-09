use "pkg/test.q" as test
use "pkg/test.q" as t
import "/home/tapeline/JavaProjects/Files/Quailv2/qlibs/pkg/adder.q"

test.setmsg("hi".upper())

add(1, 2)

t.hello()

print("begin test")

print(abs(-43))
print(abs(43))
print(any([true, true]))
print(any([false, true]))
print(any([false, false]))
print(className("it's a string"))
print(enumerate([1, 2, 3]))
print(eval("return l3"))
print(exec("return l3"))
#print(input("input text>"))
print(millis())
print(superClassName(12))

stopFlag = false

async while !stopFlag {}

print(remainingAsyncs())
print(asyncsDone())

stopFlag = true

print(remainingAsyncs())
print(remainingAsyncs())
print(remainingAsyncs())
print(remainingAsyncs())