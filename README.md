# jask
jask is a highly readable interpreter language in early development.
The jask interpreter is fully written in Java.
Currently the jask interpreter is very unstable and supports only the functions shown below.
It is just a hobby project for fun and learning.
Contributions are always welcome!

# Use
Pass .jask files as console arguments:
```
jask FILE1 FILE2 ...
```

# Examples
### Creating new variables
```C
store 100 in z
store 5.2 in d
store "Hello World!" in str
```

### Assigning values
```C
assign 1 plus 2 to z    // z = 1 + 2
assign d minus 3 to z   // z = 5.2 - 3
assign 3 times 2 to z   // z = 3 * 2
assign 10 divide 2 to z // z = 10 / 2
```

### Work with List variables
```c
store list(1:2:3) in myList
print(myList)

store listGet(myList:0) in item1
print(item1)

store list("e1":"e2":"e3") in myStrList
print(myStrList)

store listGet(myStrList:1) in item2
print(item2)
````

### Statements
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

### Creating functions
```C
function myPrint(str)
  print(str)
end

function func(param1:param2)
  print(param1)
end
```

### Calling functions
```C
store "Hello!" in text
myPrint(text)
```

### Printing and reading data
```C
store 0 in res        // new variable 'res'
assign read() to res  // assign input to variable 'res'
print(res)            // print content of 'res' to console
```

### Importing other files
File test.jask:
```
function test()
  print("Hello!")
end
```
Your file:
```
use test.jask

store test() in str
print(str)
```
