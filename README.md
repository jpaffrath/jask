# jask
jask is a highly readable interpreter language in early development.
The jask interpreter is fully written in Java.
Currently the jask interpreter is very unstable and supports only the functions shown below.
It is just a hobby project for fun and learning.
Contributions are always welcome!

# Use
To use the interactive mode, invoke jask:
```C
java -jar jask.jar
```
jask can interpret files:
```C
jask file.jask arg1 arg2 ...
```
The arguments are stored in a global list variable called _ENV.

# Examples
### Creating new variables
Jask can store three types of data: numbers, strings, and boolean values.
```C
store 100 in z
store 5.2 in d

store "Hello World!" in str

store TRUE in bool1
store FALSE in bool2

store NULL in foo
```

### Assigning values
In order to assign values to variables, they need to be stored first.
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

### Work with list variables
A list in jask can store strings, numbers and boolean values.
```C
store list(1:2:3) in numbers
print(numbers)

store listGet(numbers:0) in item1
print(item1)

// add value 4
listAdd(numbers:4)

// remove value at index 2
listRemove(numbers:2)

store list(1:"String":TRUE) in myList
```

### Statements
```C
if z equals d
  printLine("z equals d")
else
  printLine("z and d are different")
endif

if a unequals b
  printLine("a unequals b")
else
endif

if a greater b
  printLine("a greater b")
else
endif

if a smaller b
  printLine("a smaller b")
else
endif

if a smallerequal b
  printLine("a smaller or equal b")
else
endif

if a greaterequal b
  printLine("a greater or equal b")
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
Functions in jask can return nothing or any type of data.
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
myPrint("text")
func(2:a)
```

### Printing and reading data
```C
store read() in res
print(res)

print("I am a concatenated ":"string!")
```

### Convert variables
```C
store "100" in num

convert num to number
convert num to string
```

### Importing other files
You can import other jask files with the keyword 'use'.
If you import a file, jask searches for files in the directory where jask has been executed.
See the following example how to import files:
```C
function myLibFunction()
  print("This is a library!")
end
```
Your file:
```C
use myLibrary

myLibFunction()
```
You can also define a path in the use statement:
```C
use my/local/path/to/library/myLibrary

myLibFunction()
```
