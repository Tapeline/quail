# **quail** - A simple scripting language.

> Interesting fact about Quail:
> Originally, it's name was just Q. Then Quark, and now - Quail.
> And this is how a factor function would look like with Q syntax
```
**f x;
  *result;
  =>{i 1 x 1;
    ?(x == 0){;
      x = i;
    };
    ?(x != 0){;
      result = result * i;
    };
  };
  << result;
};
```
> And how it looks now

```
function factor(x) does
  num result
  through 1...x as i do
    if result is 0 then result = i end
    else do
      result = result * i
    end
  end
  return result
end
```

> ! ! !
> **Lookin' for a regex expert**
> ! ! !
> 
> Plz someone fix my regex (and lexer maybe) at `me.tapeline.quailj.tokenizetools.Lexer` and `tokens.TokenType`. If you want, write to Issues
>

> ! ! !
> **Massive refactoring in progress**
> ! ! !
>
> You can see what is happening in `dev-snapshots` repo

All the docs can be found in `example.quail` file. 

Guide for installation > https://github.com/Tapeline/quark/wiki/Installing-QuailJ


**CC BY-NC-SA 4.0**

This work is licensed under the Creative Commons «Attribution-NonCommercial-ShareAlike» 4.0 License. To view a copy of this license, visit
http://creativecommons.org/licenses/by-nc-sa/4.0/.
