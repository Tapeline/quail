# Welcome!

> **This guide will help you start writing code in Quail. I recommend you read it all.**

# Installing and running Quail

[Quail Download](get.html)

### Starting

`java -jar quail.jar run|translate|debug|profile pathtoquailfile`

# Getting started with Quail

> -=# Notice! #=-
> This guide is created for somewhat experienced
> programmers.

## 1. Types

### 1.1 Overview

There are 6 types

| Type name     | In code         | Purpose                                                                                                                                 |
| ------------- | --------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| Boolean       | `bool`          | Boolean values (true/false)                                                                                                             |
| Number        | `num`           | Number values (integers and floats)                                                                                                     |
| String        | `string`        | String values                                                                                                                           |
| List          | `list`          | A collection of different values`                                                                                                       |
| Container     | `container`     | A collection of different values stored in `key = value` structure                                                                      |
| Function      | `function`*     | Procedure to call                                                                                                                       |
| Metacontainer | `metacontainer` | Container, but when function, stored in it, called - it'll receive reference to it's container as 1st arg. (Much like `self` in Python) |
| Binary        |                 | Contains bits                                                                                                                           |
| JavaType      |                 | Wrapper for Java objects                                                                                                                |

*Also `func`, `method`, `staticmethod`

This table can remind you about Lua. Yes, Quail is inspired by Lua.

Each type except binary and JavaType carries a table alongside its value. It means you can add fields even to nums and nulls and use them as you want. 

### 1.2 Definition and usage

```lua
bool_val = false

num_val = 1
num_val_float = 1.5
num_val_negative = -3.14
num_val_incorrect_definition = .404 # Instead, you should write 0.404

string_val = "String"
string_val_incorrect_escape = "This is a double quote - \""
# It will return lexing error. Use this instead:
string_val_acceptable_escape = "This is a double quote - &q;"

list_val = [0, "hello", true, [1, 2, 3], bool_val, num_val]

container_val = {name = "Mark", age = 19}

s1 = tostring(bool_val)
s2 = tostring(num_val)
s3 = tostring(list_val)
s4 = tostring(container_val)

# also you can use
num n
bool b
string s
list l
container c
```

We'll talk about metacontainers a bit later.

### 1.3 References and things

Every variable is a reference to an object in heap.

 `a = b` won't copy `b` to `a` it will make `a` point to the same object as `b`

If you want to copy object, use `copy(v)`

### 1.4 Strong typing and other modifiers

Remember how you could define empty variable using `vartype varname`

Although Quail is dynamically typed and was designed that way, it also support strong static typing.

To strongly type a variable you need to write type name first. Yes, no ugly syntax like in Python (which types variables like this: `s: string`)

If you type (clarify) a variable, you can no longer assign value with different type.

There is another unique thing, `anyof`. You can force variable to accept various specific types. Example:

```lua
num a = 1                     # will only accept nums
string s = "d"                # will only accept strings

anyof num | string u = 1      # will accept both
u = "d"
```

The special one type though, is an `object`

```lua
object o        # will accept only containers

class Person {}
object<Person>  # will accept only objects of Person class
```

There are other modifiers:

```lua
require a = 10     # will give an error when set to null

final f = false    # will give an error when set to other value

local l = "local"  # force creation in nearest enclosing (make var local)

static f           # affects only functions and methods

# You can create very long modifier sequences
require local static anyof num | bool flag = 0   
```

## 2. Unary operators

Unary operator accepts only one operand in this way: `operator operand`

Let's see what unary ops are available in Quail

```lua
opposite = !true #also not, negate
negative = -10
check1 = exists s1 # true, because we've defined s1 above
check2 = exists whatOnEarthIsThisVariable # false
somelitter = null
check3 = exists somelitter # it can be strange but this is false. Why? Because setting variable to null is equal to deleting it
id = ##negative # double hash returns id of value.
```

## 3. Binary operators

Unlike unary ops, binary accept 2 operands like this `operand1 operator operand2`

E.g `2 + 2`

> Later on: a - 1st operand (left), b - 2nd operand (right)

### 3.1 Addition operator

| a      | b      | Description              |
| ------ | ------ | ------------------------ |
| Number | Number | Adds two numbers         |
| String | String | Concatenates two strings |
| List   | List   | Concatenates two lists   |

### 3.2 Subtraction operator

| a      | b      | Description                                     |
| ------ | ------ | ----------------------------------------------- |
| Number | Number | Subtract b from a                               |
| String | String | Remove all b in a `("hello" -  "llo" -> "he")`  |
| List   | List   | Remove all b in a `([3, 5, 7] - [7] -> [3, 5])` |

### 3.3 Multiplication operator

| a      | b      | Description                                     |
| ------ | ------ | ----------------------------------------------- |
| Number | Number | Multiply two numbers                            |
| String | Number | Repeat a b times `("he" * 3 -> "hehehe")`       |
| List   | Number | Repeat a b times `([0, 1] * 2 -> [0, 1, 0, 1])` |

### 3.4 Division operator

| a      | b      | Description                                                           |
| ------ | ------ | --------------------------------------------------------------------- |
| Number | Number | Divide two numbers                                                    |
| String | Number | Split a into b parts `("hello" / 3 -> ["he", "ll", "o"])`             |
| List   | Number | Split a into b parts `([0, 1, 2, 3, 4] / 3 -> [[0, 1], [2, 3], [4]])` |

### 3.5 `pow` operator ^

| a      | b      | Description |
| ------ | ------ | ----------- |
| Number | Number | a pow b     |

### 3.6 Integer division //

| a      | b      | Description                                                                     |
| ------ | ------ | ------------------------------------------------------------------------------- |
| Number | Number | Divide two numbers (a = 10, b = 4 -> result =/= 2,5 but result = 2)             |
| String | Number | Split a into b parts with same size `("hello" / 3 -> ["he", "ll"])`             |
| List   | Number | Split a into b parts with same size `([0, 1, 2, 3, 4] / 3 -> [[0, 1], [2, 3]])` |

### 3.7 Comparison operators (<, >, <=, >=)

| a      | b      | Description              |
| ------ | ------ | ------------------------ |
| Number | Number | Compare two numbers      |
| String | Number | Compare size of a to b   |
| List   | Number | Compare size of a to b   |
| String | String | Compare sizes of a and b |
| List   | List   | Compare sizes of a and b |

### 3.8 Comparison operators 2 (`!=`, `==`, also `is` and `is not`)

Whether a is equal to b or not. Much simpler, isn't it?

### 3.9 Remainder (%)

| a      | b      | Description                    |
| ------ | ------ | ------------------------------ |
| Number | Number | a % b, nothing to explain here |

### 3.10 Logical `and`

> *omg, this is the longest article I've ever made*

| a    | b    | Description       |
| ---- | ---- | ----------------- |
| Bool | Bool | a and b are true? |

### 3.11 Logical `or`

| a      | b      | Description      |
| ------ | ------ | ---------------- |
| Bool   | Bool   | a or b are true? |
| Null   | Null   | Returns null     |
| Object | Null   | Returns a        |
| Null   | Object | Returns b        |
| Object | Object | Returns a        |

### 3.12 `instanceof` operator

> Alias: `is type of`

| a      | b      | Description            |
| ------ | ------ | ---------------------- |
| Object | String | If Object is type of b |

### 3.13 `is the same type as` operator *(Maybe the longest ever operator name)*

| a      | b      | Description                     |
| ------ | ------ | ------------------------------- |
| Object | Object | If a's type's equal to b's type |

### 3.14 Field references

Unlike other languages, in Quail there are 2 other possible ways to access fields in object aside from `. <- the regular dot`

There is `'s` and `'` operator and `of` operator. Examples:

```lua
# assume that variable a has field x and function y
# The regular way:
out a.x
a.y()

# The 's way:
out a's x  # You can use in situations like this: student's grade
a's y()  # Also you can use ', when needed (e.g.: parents' names)

# The `of` way:
out x of a
y() of a
# Reverse way: `a.x`, but `x of a`!
```

### 3.15 Range operator `:`, `:+`

| a      | b      | Description                                                                                                                                                                                                                                                                 |
| ------ | ------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Number | Number | Returns a list contains all ints from a to b (including a) If ends with +, then b is includedNumber    Returns a list contains all ints from a to b with difference of c (including a) If ends with +, then b is included<br/>Example: 0 : 10 step 2 -> [0, 2, 4, 6, 8, 10] |

### 3.16 Stepped range operator `a : b step c`, `a :+ b step c`

| a      | b      | c      | Description                                                                                                        |
| ------ | ------ | ------ | ------------------------------------------------------------------------------------------------------------------ |
| Number | Number | Number | Returns a list contains all ints from a to b with difference of c (including a) If ends with +, then b is included |
|        |        |        | Example: 0 :+ 10 step 2 -> [0, 2, 4, 6, 8, 10]                                                                     |

### 3.17 Assignement operator `=`

| a             | b      | Description                              |
| ------------- | ------ | ---------------------------------------- |
| Some variable | Object | Create a copy of `b` and store it in `a` |

### 3.18 Shift left `<<` and right `>>`

| a      | b      | Description                            |
| ------ | ------ | -------------------------------------- |
| Number | Number | `a`+`b` (`>>`) or `a`-`b` (`<<`)       |
| String | Number | Shift `a` `b` characters left or right |
| List   | Number | Shift `a` `b` items left or right      |

## 4. Blocks

Block is a statement that can hold multiple statements
Block can be opened with these keywords: `do`, `does`, `with`, `has`, `{`
And it should be closed with one of these: `end`, `}`

## 5. Functions

```lua
# declaring a function
function sayhi() { # it does not receive any args
    out("Hi")
}

function opposite(n) {
    return not n
}

function sum(a, b) {
    return a + b
}

# since all of these 3 functions have only one statement, they can be simplified to:
function sayhi() out("Hi")
function opposite(n) return not n
function sum(a, b) return a + b

# also you can declare anonymous functions and then assign them to variable, or pass as value
anonfun = function (somearg) return somearg is type of "num"
# arg... - consumption of arguments
javalike = (arg...) -> {return arg}

# calling
functionToCall(arg0, arg1...)

# function can receive as many args as you want
# there can be 3 cases:
# 1. Insufficient args => all args that weren't passed will be null
# 2. Sufficent args => everything as it is
# 3. Too much args => extra args will be ignored

# multiple calls f()()()... allowed
```

Remember strong typing? Method overloading is included!

```lua
function a(arg) out("Nothing")     # Fallback

function a(num n) out(n - 1)       # When num passed

function a(string s) out(s.sub(1)) # When string passed

a("Hello")
a(4.5)
a(false)


function b(bool flag) out(!flag)   # The only variant is passing bool

b(false)                           # Ok

b(null)                            # Will cause error
```

And also, you can declare function's return value's type

```lua
bool invert(bool arg) return !arg
```

## 6. Lists and their methods

### 6.1 Few words about list

List is a structure that holds multiple values identified by integer number starting with 0

```
list l # < These 2 are equal
l = [] # |
l1 = [1, "b", false, null, [1, 2]] # any values can be stored

l1[1] = "a" # indexation
```

### 6.2 List methods

| Syntax            | Description                                       |
| ----------------- | ------------------------------------------------- |
| add(value)        | Add value to a list                               |
| find(value)       | Returns index of first occurence of value in list |
| get(index)        | Get value at index                                |
| remove(index)     | Remove value from list by index                   |
| removeitem(value) | Remove value from list                            |
| reverse()         | Returns a reversed list                           |
| count(value)      | Count occurrences                                 |
| size()            | Returns size of list                              |

## 7. Containers and their methods

### 7.1 Few words about container

Container is a structure that holds key(string) = value pairs.

```lua
c = {} # declaring an empty container
c = {a = "A",    # this is equal to "a" = "A"
     "b" = "B"}  # this is equal to b = "B"
c.d = "D"        # it wasn't defined yet, but you can define it on the way

# Values can be accessed in multiple ways
out(c.a + c.b)  # syntaxic sugar
out(c.get("d")) # the o.g. way
```

### 7.2 Container methods

| Syntax                 | Description                                                             |
| ---------------------- | ----------------------------------------------------------------------- |
| set(key, value)        | Set key to value                                                        |
| values()               | Return a list contains all values except built-in ones                  |
| get(key)               | Get value at key                                                        |
| remove(key)            | Remove value from container by key                                      |
| contains(key)          | Check if value is present                                               |
| pairs()                | Returns a list containing all key-value pairs except built-in ones      |
| keys()                 | Return a list contains all keys except built-in ones                    |
| size()                 | Returns size of container                                               |
| allkeys()              | Return a list of all keys                                               |
| allpairs()             | Returns a list containing all key-value pairs                           |
| alltostring()          | Full string conversion (beware, can cause stack overflow)               |
| assemble(keys, values) | Returns container assembled from provided container and key-value pairs |
| allvalues()            | Return a list contains all values                                       |

## 8. String methods

| Syntax                       | Description                                                         |
| ---------------------------- | ------------------------------------------------------------------- |
| back(c)                      | String background = c                                               |
| capitalize()                 | Capitalize                                                          |
| color(c)                     | String color = c                                                    |
| count(needle)                | Count occurrences                                                   |
| endswith(postfix)            | Ends with postfix?                                                  |
| find(needle)                 | Find index of first occurrence                                      |
| get(i)                       | Get i character in string                                           |
| isalpha()                    | Contains only letters?                                              |
| isalphanumeric()             | Contains only letters and digits?                                   |
| islowercase()                | Is all lowercase?                                                   |
| isnum()                      | Contains only digits?                                               |
| isuppercase()                | Is all uppercase?                                                   |
| lower()                      | Make all lowercase                                                  |
| replace(needle, replacement) | Replace all occurrences                                             |
| reverse()                    | Reverse string                                                      |
| size()                       | Length of string                                                    |
| split(separator)             | Split to list using separator                                       |
| startswith(prefix)           | Starts with prefix?                                                 |
| style(style)                 | Style (blink, bold, dim, invert, italic, strike, under)             |
| sub(start, [end])            | Substring from start to end (or to string end if end not specified) |
| upper()                      | Make all uppercase                                                  |

## 9. Metacontainers a.k.a. classes-objects

If you are not familiar with OOP, I strongly recommend you to learn it.

### 9.1 Syntax

```lua
class Letter {   # Definition of class. Keyword class can be replaced with metacontainer
    string capital   # Declaring fields
    string small
    string type

    # Constructor. In Quail we call it "object builder"
    # Notice the 1st arg: "me". This is a link to the object.
    # Like "self" in python and like "this" in C-likes.
    object builder(me, capital, small, type) {
        me.capital = capital
        me.small = small
        me.type = type
    }

    method gettype(me) {   # This is a method. You can declare it with "func", "function" and "method" keywords.
        return me.type
    }

    staticmethod a(me) {   # Declaring a static method
        out(me)   # will print null, because when you call a static method it does not receive any link to object
    }

    override tostring(me) {   # Overriding default behaviour for built-in function tostring
        return me.type + " " + me.capital
    }
}

class Vowel like Letter {   # Inheritance from Letter. Everything will be transferred from the Letter class including overrides

    object builder(me, capital, small) {
        me.capital = capital
        me.small = small
        me.type = "Vowel"
        return me
    }

}

B = Letter("B", "b", "Consonant")   # Initialize object
A = Vowel("A", "a")
out(tostring(B))
```

### 9.2 Override list

`>`, `<`, `>=`, `<=`, `==`, `!=`, `tostring`, `tonum`, `tobool`, `+`, `-`, `/`, `//`, `*`, `^`, `%`,`get`, `set`, `!`, `index`, `setindex`, `call` , `<<`, `:`, `>>`

### 9.3 System types modification

In Quail you can modify these types: `Number`, `Bool`, `String`, `List`, `Container`, `Null`
And make them contain methods and fields you want.

```lua
Number.add = function (me, other) return me + other
refreshtypes()
out(15.add(30))
```

Do not forget to call `refreshtypes()` after updating types

## 10. Libraries

### 10.1 Using libraries

```lua
use "random"   #  use or using
out(random.toss())
```

### 10.2 Creating libraries

This is how you create library:

```lua
# Here you initialize something

return {yourLibMethodName = yourFunctionName}
```

### 10.3 How does it work underneath?

When library is imported new runtime is created for it. All code inside the library gets executed and then all returned values are bound to that new runtime, so when they are called, they run inside runtime of the library, not the global runtime.

## 11. Built-ins

### 11.1 Variables

| Name       | Value                  |
| ---------- | ---------------------- |
| nothing    | null                   |
| million    | 1'000'000              |
| trillion   | 1'000'000'000'000      |
| billion    | 1'000'000'000          |
| scripthome | Root of running script |

### 11.2 Functions

| Name                                        | Description                                             |
| ------------------------------------------- | ------------------------------------------------------- |
| out(value)                                  | Print value with \n                                     |
| put(value)                                  | Print value                                             |
| input(value)                                | Print value and wait for input then return user's input |
| clock()                                     | Get current time in seconds                             |
| millis()                                    | Get current time in milliseconds                        |
| fileread(path)                              | Get contents of file                                    |
| fileexists(path)                            | Check if file exists                                    |
| filewrite(path, content)                    | Write to file (Write, not append)                       |
| tostring(value)                             | Convert value to string                                 |
| tonum(value)                                | Convert value to num                                    |
| tobool(value)                               | Convert value to bool                                   |
| refreshtypes()                              | See $9.3                                                |
| sin, cos, tan, their arc and h versions (v) | Math functions                                          |
| atan2(a, b)                                 | Math functions                                          |
| min and max (a, b)                          | Math functions                                          |
| abs(v)                                      | Absolute of v                                           |
| binfileread, binfilewrite - binary version  |                                                         |
| byte(v)                                     | Byte representation of v                                |
| bit(b, i)                                   | Return i bit in b binary                                |
| bitset(b, i, v)                             | Set i bit in b binary to boolean v                      |
| char(n)                                     | Get char from code returned by ord()                    |
| ord(c)                                      | Get code from char                                      |
| console(cmd)                                | Run cmd in console                                      |
| exec(code)                                  | Execute quail code                                      |
| newevent(evt, [data])                       | See $16.                                                |
| usejar(file, classpath)                     | See $15.                                                |
| thread(func, args)                          | See $17.                                                |

## 12. Instructions

One word simple commands

```lua
break # break a loop
continue # jump to a next iteration
memory # dump memory contents to console
breakpoint # dump memory contents to console and wait
```

## 13. Effects

Syntax: `EFFECT EXPRESSION`

```lua
use "lib" # also using and deploy. For more info see 9.
return value # return value
assert expr # throws an error when expr is false or null
throw "error" # throws an error
strike n # break n nested loops
```

## 14. Constructions

### 14.1 If

`if` `condition` `statement`

`if` `condition` `statement`
`elseif` `condition` `statement`
`...`

`if` `condition` `statement`
`else` `statement`

`if` `condition` `statement`
`elseif` `condition` `statement`
`...`
`else` `statement`

### 14.2 Through

`through` `range` `as` `variable` `statement`

### 14.3 Every

`every` `variable` `in` `list or string`

### 14.4 While

`while` `condition` `statement`

### 14.5 Loop-Stop

`loop` `statement` `stop when` `condition`

### 14.6 Try-Catch

`try` `statement` `catch as` `variable` `statement`

## 15. Embeds

You can add your own Java source code to your Quail project.
Place in some directory `dir` your Java file. Remember, if .java file is not in root of your `dir`, you need to specify the package in .java

```java
package here.goes.your.path;
```

Then you can run `embed(source, classpath, dir)` to embed your code. Example

```
/home/user/
|- /test/
|  |- yourjavafile.java
|- yourquailfile.q
```

In yourquailfile:

```lua
embed("test/yourjavafile.java", "test.yourjavafile", "/home/user")
```

In yourjavafile:

```java
package test;

public class yourjavafile ...
```

You should add some functionality to your embed, so proceed to [Quail API](github.com/Tapeline/quail/wiki/api)

Also you can embed jar:

```lua
usejar("yourjar.jar", "your.class.path");
```

## 16. Events

You can manage events with Quail.
Start by creating an event handler:

```lua
on eventname (event) STATEMENT
```

You can use either a variable or a string for eventname.
Then you can call this event:

```lua
newevent(eventname)
```

Additionally, you can pass some data to event using container:

```lua
newevent(eventname, containerwithdata)
```

Handler will receive it as argument.
If you want to cancel event, you can return null or false.
You can migrate event handlers from your library using `_events` field in exporting container

```lua
return {
    _events = [
         {
             event = "birthday",
             consumer = function (e) out("Happy Birthday!")
         }
     ]
}
```

You can register events with functions:
`registerhandler("event", yourHandlerFunc)`

> REMEMBER: quail registers NAMES of handlers, thus anonymous function here won't work.
> Even your `on event (e)` statement are firstly placed in memory with name `_eventhandler_YOUREVENTNAME_IDOFEVENTHANDLER`

## 17. Threads

Quail supports multithreading. To create new thread you can call `thread(func, args)`

```lua
th = thread((me, arg) -> {         # First argument is a ref. to thread
    out("In thread " + arg)        
    return arg                     # Thread runs until function ends
}, ["Hello"])                      # List of arguments for function

th.start()                         # Start the thread
th.sleep(1000)                     # Sleep for 1000 ms
out(th.result())                   # Get the result
th.wake()                          # Wake the thread

out(th.wait())                     # Wait till the thread will end and 
                                   # return the result
out(th.isended())                  # Has the thread ended
```

## 18. Advanced tips and tools

### 18.1 Preprocessor

Quail has a **very** simple preprocessor built-in. Directives start with `#:`

Directives:

- `include "file"` - includes text from file at the top of file

- `alias "regex" replacement` - replace regex with replacement

## 18.2 Profiler

If you run Quail using `profile` command, then you will get a file named QuailProfilingReport....json. You can open this file using quail::studio.

# Th-th-th that's all folks!
