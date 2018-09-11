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
Jask can store three types of primitive data: numbers, strings and boolean values.
To mark a variable as empty, you can use the value NULL.
```Assembly
store 100 in z
store 5.2 in d

store "Hello World!" in str

store TRUE in bool

store NULL in foo
```
There are two abstract data types, lists and dictionaries, which will be covered later.

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
assign "Repeating string" times 10 to str

assign FALSE to bool
```
You can increment and decrement numbers very quickly:
```Assembly
increment myNum ; same as assign myNum plus 1 to myNum
decrement myNum ; same as assign myNum minus 1 to myNum
```

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
run i from 0 to 10 with i plus 1
    print("i is ":i)
endrun

store TRUE in val

while val equals TRUE
    print("val is TRUE!")
endrun

for element in list(1:2:3)
  printLine(element)
endrun
```
Want more examples? Visit [Control flow wiki](https://github.com/jpaffrath/jask/wiki/Control-flow)!

### Creating functions
Functions in jask can return nothing, any type of variables or data, calculations or other function calls.
```Assembly
function myFunction(param1:param2)
  printLine("Function call with parameters":param1:param2)
end

function divide(num1:num2)
  return num1 divide num2
end
```
Want more examples? Visit [Functions Wiki](https://github.com/jpaffrath/jask/wiki/Functions)!

### Work with list variables
A list in jask can store strings, numbers, boolean values, other lists and dictionaries.
In addition, a list can store different types at the same time.
```Assembly
store list(1:2:3) in numbers
print(numbers)

store listGet(numbers:0) in item1

; add value 4
assign listAdd(numbers:4) to numbers

; remove value at index 2
assign listRemove(numbers:2) to numbers

store list(1:"String":TRUE:list():dictionary("key":"value")) in myList
```
Want more examples? Visit [List variables Wiki](https://github.com/jpaffrath/jask/wiki/List-variables-in-jask)!

### Work with dictionary variables
A dictionary in jask stores values with associated keys.
```Assembly
store dictionary("myKey":"myValue") in dict
print(dict)

store dictionaryGet(dict:"myKey") in myValue
```
Want more examples? Visit [Dictionary variables Wiki](https://github.com/jpaffrath/jask/wiki/Dictionary-variables-in-jask)!

# Honorable mentions
jask has some very interesting features! Visit the [jask Wiki](https://github.com/jpaffrath/jask/wiki) to find out more!

### Callbacks
You can use callbacks in jask. [Callback wiki](https://github.com/jpaffrath/jask/wiki/Callbacks-in-jask)

### Modules
Other jask files can be used as modules. Learn how to do it in the [Modules wiki](https://github.com/jpaffrath/jask/wiki/Modules)!

### Converting variables
Learn how to convert variables into different types in the [Convert Variables Wiki](https://github.com/jpaffrath/jask/wiki/Convert-variables)

### Access Operator
You want to know how to access variables inside a function which are defined outside?
Visit [Access operator wiki!](https://github.com/jpaffrath/jask/wiki/The-access-operator)

### Functions inside functions
You can define functions inside functions!
Learn how to do it in the [Functions inside functions Wiki](https://github.com/jpaffrath/jask/wiki/Functions-inside-functions!)

### Modules inside functions
In a function you can import modules. Visit [Modules inside functions Wiki](https://github.com/jpaffrath/jask/wiki/Modules-inside-functions!) to learn more.
