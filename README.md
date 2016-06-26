# jask
jask is a highly readable interpreter language in early development.
The jask interpreter is fully written in Java.
Currently the jask interpreter is very unstable and supports only the functions shown below.
It is just a hobby project for fun and learning.
Contributions are always welcome!

# Examples
Creating new variables
```C
store 100 in z
store 5.2 in d
store "Hello World!" in str
```

Assigning values
```C
assign 1 plus 2 to z    // z = 1 + 2
assign d minus 3 to z   // z = 5.2 - 3
assign 3 times 2 to z   // z = 3 * 2
assign 10 divide 2 to z // z = 10 / 2
```

Statements
```c
if z equals d
  print(z)
else
  print(d)
endif

if d equals d
  print(d)
else
endif
```

Calling functions
```C
print(d) // build-in function
my(d:z)
```

Creating functions
```C
function my(z1:z2)
  store 0 in res
  assign z1 plus z2 to res
  print(res)
end
```
