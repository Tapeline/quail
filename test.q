class Test {
    protected = 10

    sets protected(me) {}
}

t = Test()
t.protected = 0
out(t.protected)