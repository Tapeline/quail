class Error {
    string type
    string message

    constructor(me, message) {
        me.type = "Error"
        me.message = message
    }
}

throw Error("Whoops")