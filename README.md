<p>
    <a href="https://github.com/jpaffrath/jask/releases">
        <img src="https://img.shields.io/badge/version-0.0.1-orange.svg"
             alt="Version Badge">
    </a>
    <a href="https://github.com/jpaffrath/jask/blob/master/LICENSE">
        <img src="https://img.shields.io/badge/license-GPL--3.0-blue.svg"
             alt="License Badge">
    </a>
    <a href="https://java.com/">
        <img src="https://img.shields.io/badge/required java version-1.7-green.svg"
             alt="Java Badge">
    </a>
</p>

# jask
jask is a highly readable interpreter language in early development.
The jask interpreter is fully written in Java.
It is just a hobby project for fun and learning.
Contributions are always welcome!

Seeing jask for the first time? Try the [Getting started guide](https://github.com/jpaffrath/jask/wiki/Getting-started)!
For further information, please visit the [jask Wiki](https://github.com/jpaffrath/jask/wiki).

# Use
To use the interactive mode, invoke jask:
```Assembly
java -jar jask.jar
```
jask can interpret files:
```Assembly
jask file.jask arg1 arg2 ...
```
The arguments are stored in a global list variable called _ENV.  

To test your freshly compiled jask-version, just run the _test.jask_ file.

# Examples

### Creating new variables
Jask can store three types of data: numbers, strings, and boolean values.
```Assembly
store 100 in z
store 5.2 in d

store "Hello World!" in str

store TRUE in bool

store NULL in foo
```

### Assigning values
In order to assign values to variables, they need to be stored first.
```Assembly
assign 1 plus 2 to z
assign d minus 3 to z
assign 3 times 2 to z
assign 10 divide 2 to z
assign 8 mod 3 to z

assign "A string" to str
assign "Hello " plus "World!" to str

assign FALSE to bool
```
You can increment and decrement numbers very quickly:
```Assembly
increment myNum ; same as assign myNum plus 1 to myNum
decrement myNum ; same as assign myNum minus 1 to myNum
```

### Calling functions
```Assembly
myFunc(parameter)
```
Multiple parameters are separated by colons:
```Assembly
myFunc(firstParameter:secondParameter)
```
Want more examples? Visit [Functions Wiki](https://github.com/jpaffrath/jask/wiki/Functions)!

### Work with list variables
A list in jask can store strings, numbers and boolean values.
```Assembly
store list(1:2:3) in numbers
print(numbers)

store listGet(numbers:0) in item1
print(item1)

; add value 4
listAdd(numbers:4)

; remove value at index 2
listRemove(numbers:2)

store list(1:"String":TRUE) in myList
```
Want more examples? Visit [List variables Wiki](https://github.com/jpaffrath/jask/wiki/List-variables-in-jask)!

### Control flow
if-else conditions:
```Assembly
if z equals d
  printLine("z equals d")
else
  printLine("z and d are different")
endif
```
run and while loops:
```Assembly
store 0 in i

run i to 10 with i plus 1
    print("i is ":i)
endrun

store TRUE in val

while val equals TRUE
    print("val is TRUE!")
endrun
```
Want more examples? Visit [Control flow wiki](https://github.com/jpaffrath/jask/wiki/Control-flow)!

### Creating functions
Functions in jask can return nothing or any type of data.
```Assembly
function myPrint(str)
  print(str)
end

function func(param1:param2)
  print(param1)
  return TRUE
end
```
To access a variable which is stored outside a function, use the access operator "!":
```Assembly
store "Outside" in var

function myFunc()
    printLine(!var)
end
```
This allows you to define variables with the same name:
```Assembly
store "Outside" in var

function myFunc()
    store "Inside" in var

    printLine(!var) ; prints "Outside"
    printLine(var)  ; prints "Inside"
end
```
You find more information about the access operator in the [Access operator wiki!](https://github.com/jpaffrath/jask/wiki/The-access-operator)
Want to know more about functions in jask? Look at [Functions inside functions](https://github.com/jpaffrath/jask/wiki/Functions-inside-functions!) or [Modules inside functions](https://github.com/jpaffrath/jask/wiki/Modules-inside-functions!).

### Printing and reading data
```Assembly
store read() in res
print(res)

print("I am a concatenated ":"string!")
```

### Convert variables
```Assembly
store "100" in num

convert num to number
convert num to string
```

### Modules
You can import other jask files with the keyword 'use'.
An imported file is called a _module_.
If you import a module, jask searches for files in the directory where it has been executed.
See the following example how to import a module:
```Assembly
function myModuleFunction()
  print("Hello! I am a module!")
end
```
Let's say this is saved in a file called _myModule.jask_.
Now you can import the module, called _myModule_ (without the extension '.jask'):
```Assembly
use myModule
myModuleFunction()
```
You can also define a path in the use statement:
```Assembly
use my/local/path/to/library/myModule
myModuleFunction()
```
Want more examples? Visit [Modules wiki](https://github.com/jpaffrath/jask/wiki/Modules)!
