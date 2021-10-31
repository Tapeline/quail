# ================================================== #
# The detailed explanation of all Quark 0.1 features #
# ================================================== #

# Variable definintion
s = "Hello, world"      #StringType
n = 3.1415              #NumType
b = true                #BoolType


# Input and output
name = input "What's your name?"            #StringType input
put "Hello, "                               #Print without \n
out name                                    #Print with \n
age = numinput "How old are you?"           #NumType input
choco = boolinput "Do you like chocolate?"  #BoolType input


# Type conversion
convertedNum = tonum "31415"
convertedString = tostring false
convertedBool = tobool "true"


# if - elseif - else statement
# Possible operators: < > <= >= == != not
# Not implemented yet: and or (&& ||)
if (age < 6) do
  out "Sorry, but you aren't old enough to use the computer"
end elseif (age < 18) do
  out "You can use the computer"
end else do
  out "Welcome!"
end


# through loop
through 1...10 as i do    # <rangeStart>...<rangeEnd> as <variable>
  out i
end
out ""


# other useful features
milestone         # dumps the memory and AST in console
breakpoint        # milestone, but with pausing
destroy variable  # remove variable from memory
# string .. str   # concatenation


# The try - catch construction
# try do
#   here some code
# end catch as variable do
#   some code
# end
