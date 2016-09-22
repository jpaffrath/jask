# jask
jask is a highly readable interpreter language in early development.
The jask interpreter is fully written in Java.
Currently the jask interpreter is very unstable and supports only the functions shown below.
It is just a hobby project for fun and learning.
Contributions are always welcome!

# Use
Pass .jask files as console arguments:
```C
jask FILE1 FILE2 ...
```
To define the standard libary path, use option -l
```C
jask FILE1 -l path/to/lib
```

# Examples
### Creating new variables
```C
store 100 in z
store 5.2 in d

store "Hello World!" in str

store TRUE in bool1
store FALSE in bool2
```

### Assigning values
```C
assign 1 plus 2 to z
assign d minus 3 to z
assign 3 times 2 to z
assign 10 divide 2 to z

assign "A string" to str
assign "Hello " plus "World!" to str

assign TRUE to bool
assign FALSE to bool
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
  printLine("z equals d")
else
  printLine("z and d are different")
endif

if a unequals b
  printLine("a unequals b")
else
endif

if x mod y equals z
  print(x)
else
endif

if val equals TRUE
  print("val is true")
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
  return TRUE
end
```

### Calling functions
```C
store "Hello!" in text
myPrint(text)
```

### Printing and reading data
```C
store read() in res
print(res)
```

### Convert variables
```C
store "100" in num

convert num to number
convert num to string
```

### Importing other files
The jask interpreter searches for files in the current directory.
File myFunctions.jask:
```C
function test()
  print("Hello!")
end
```
Your file:
```C
use myFunctions

store test() in str
print(str)
```
You can define a path in the use statement:
```C
use my/local/path/test
```
