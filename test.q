class Letter {
    string capital
    string small
    string type

    constructor(me, type, capital, small) {
        me.type = type
        me.capital = capital
        me.small = small
    }

    method print(me) {
        print(me.type, "Capital:", me.capital, "Small:", me.small)
    }
}

class Vowel like Letter {
    constructor(me, capital, small) {
        Letter._constructor(me, "Vowel", capital, small)
    }
}

class Consonant like Letter {
    constructor(me, capital, small) {
        Letter._constructor(me, "Consonant", capital, small)
    }
}

class LetterA like Vowel {
    constructor(me) {
        Vowel._constructor(me, "A", "a")
    }
}

a1 = Letter("Vowel", "A1", "a1")
a1.print()

a2 = Vowel("A2", "a2")
a2.print()

c1 = Consonant("C", "c")
c1.print()

a3 = LetterA()
a3.print()
