n = 1
start = millis()
through 1:+1000000 as x
    n = n * x
print(millis() - start)