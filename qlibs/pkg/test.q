hellomsg = "hello"

function hello()
    print(hellomsg)

function setmsg(msg)
    hellomsg = msg

print("Library was loaded from file")

return {
    "hello" = hello,
    "setmsg" = setmsg
}