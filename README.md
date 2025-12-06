# Integer Expression Parser


A Java-based integer expression parser project using Java 25 and Maven, with support for basic arithmetic operations.

## Project Overview

This project implements a recursive descent parser for integer arithmetic expressions, supporting addition, subtraction, multiplication, and division operations. It has been converted from an Eclipse project to a Maven project for better cross-IDE compatibility.

## Features

- **Four Basic Operations**: Addition (+), Subtraction (-), Multiplication (*), and Division (/)
- **Operator Precedence**: Multiplication and division have higher precedence than addition and subtraction
- **Left-to-right Associativity**: Operations of the same precedence are evaluated from left to right
- **Division by Zero Detection**: Division by zero is detected and throws `ArithmeticException`
- **Comprehensive Test Suite**: 16 unit tests covering all operations and edge cases
- **Clean Grammar**: Simplified, readable grammar definition using alternation syntax

## Requirements

- Java 25
- Maven 3.6+
- VS Code (recommended with Java Extension Pack)

## Project Structure

```
ex18-intexpr/
├── src/
│   └── intexpr/
│       ├── IntegerExpression.java          # Expression interface
│       ├── IntegerExpressionParser.java     # Main parser class
│       ├── IntegerExpressionParserTest.java # Test suite (16 tests)
│       ├── IntegerExpression.g              # Grammar definition file
│       ├── Number.java                      # Number expression
│       ├── Plus.java                        # Addition expression
│       ├── Minus.java                       # Subtraction expression
│       ├── Times.java                       # Multiplication expression
│       └── Divide.java                      # Division expression with div-by-zero check
├── lib/
│   └── parserlib.jar                        # MIT ParserLib dependency
├── pom.xml                                  # Maven configuration
└── .vscode/
    ├── settings.json                        # VS Code Java settings
    └── launch.json                          # Debug configuration
```

## Getting Started

### First-Time Setup - Install ParserLib Dependency

If you've just cloned the project or cleared your local Maven repository, install `parserlib.jar` to your local Maven repository:

```bash
mvn install:install-file -Dfile=lib/parserlib.jar -DgroupId=edu.mit.eecs -DartifactId=parserlib -Dversion=3.2.0 -Dpackaging=jar
```

**Note**: This only needs to be done once. Maven will automatically load it from the local repository (`~/.m2/repository/`) afterward.

### Compile the Project

```bash
mvn clean compile
```

### Run the Main Program

```bash
mvn exec:java -Dexec.mainClass="intexpr.IntegerExpressionParser"
```

### Run Tests

```bash
mvn test
```

Expected output: **Tests run: 16, Failures: 0, Errors: 0, Skipped: 0**

### Package the Project

```bash
mvn clean package
```

The generated JAR file will be located at `target/intexpr-1.0-SNAPSHOT.jar`

## Grammar Definition

The parser uses a simplified grammar defined in `IntegerExpression.g`:

```
@skip whitespace {
    expr ::= sum;
    sum ::= product (('+' | '-') product)*;
    product ::= primary (('*' | '/') primary)*;
    primary ::= number | '(' sum ')';
}
number ::= [0-9]+;
whitespace ::= [ \t\r\n]+;
```

**Key Design Decisions**:
- Operator precedence is encoded in the grammar structure (product before sum)
- Uses alternation syntax `('+' | '-')` for better readability
- Operator extraction is performed using text-based parsing to work around ParserLib API limitations

## VS Code Debugging

The project includes VS Code debug configuration:

1. Open VS Code
2. Press F5 or click "Run and Debug"
3. Select "Debug IntegerExpressionParser" configuration

## Dependencies

- **JUnit Jupiter 5.10.1**: Unit testing framework
- **MIT ParserLib 3.2.0**: Grammar parsing library
  - JAR file located at `lib/parserlib.jar`
  - Installed to local Maven repository via `mvn install:install-file -Dfile=lib/parserlib.jar -DgroupId=edu.mit.eecs -DartifactId=parserlib -Dversion=3.2.0 -Dpackaging=jar`
  - **The lib directory can be deleted** after installation, but keeping it is recommended for new contributors

## Implementation Details

### Division by Zero Handling

Division by zero is detected at object construction time in the `Divide` class:

```java
public Divide(IntegerExpression left, IntegerExpression right) {
    this.left = left;
    this.right = right;
    checkRep(); // Throws ArithmeticException if right.value() == 0
}
```

### Operator Extraction

Due to ParserLib's `ParseTree.children()` API only returning non-terminal nodes, operator extraction is performed using text-based parsing:

```java
String fullText = parseTree.text();
int productStart = fullText.indexOf(productText, currentPos);
String between = fullText.substring(currentPos, productStart).trim();
if (between.equals("+")) expression = new Plus(expression, right);
else if (between.equals("-")) expression = new Minus(expression, right);
```

## Testing

The test suite includes 16 comprehensive tests covering:
- Basic operations (addition, subtraction, multiplication, division)
- Operator precedence
- Left-to-right associativity
- Division by zero detection
- Parenthesized expressions
- Complex multi-operation expressions

Run tests with: `mvn test`

## Migration from Eclipse

The original source is: https://web.mit.edu/6.031/www/sp21/classes/18-parsers/code.html   
This project was successfully migrated from Eclipse to Maven:

1. ✅ Created `pom.xml` configuration with Java 25
2. ✅ Installed parserlib.jar to local Maven repository
3. ✅ Updated code to load grammar files from classpath resources
4. ✅ Removed Eclipse configuration files (.classpath, .project, .settings)
5. ✅ Configured VS Code debugging and Java support
6. ✅ Updated .gitignore file

