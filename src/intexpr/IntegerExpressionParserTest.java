package intexpr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;


class IntegerExpressionParserTest {

    // Testing strategy:
    //
    // partition on largest number: single-digit, multi-digit 
    // partition on # of additions: 0, 1, >1
    // partition on # of subtractions: 0, 1, >1
    // partition on # of multiplications: 0, 1, >1
    // partition on # of divisions: 0, 1, >1
    // partition: add/subtract is subexpression of multiply/divide, or not
    // partition: multiply/divide as subexpression of add/subtract, or not
    // partition on parens: required, not required
    // partition on division by zero: divide by zero, not divide by zero
    
    // covers multidigit number, 0 adds, 0 subtracts, 0 multiplies, 0 divides
    @Test void testConstant() throws UnableToParseException {
        assertEquals(32, IntegerExpressionParser.parse("32").value());
    }

    // covers single digit, >1 adds, parens not required
    @Test void testTwoAdds() throws UnableToParseException {
        assertEquals(6, IntegerExpressionParser.parse("1+2+3").value());
    }

    // covers >1 multiplies, multiply subexpr of add, add subexpr of multiply, parens required
    @Test void testAddAndMultiply() throws UnableToParseException {
        assertEquals(70, IntegerExpressionParser.parse("5*(2+3*4)").value());
    }
    
    // covers 1 subtraction, parens not required
    @Test void testSimpleSubtraction() throws UnableToParseException {
        assertEquals(5, IntegerExpressionParser.parse("8-3").value());
    }
    
    // covers >1 subtractions
    @Test void testMultipleSubtractions() throws UnableToParseException {
        assertEquals(5, IntegerExpressionParser.parse("10-3-2").value());
    }
    
    // covers 1 division, parens not required
    @Test void testSimpleDivision() throws UnableToParseException {
        assertEquals(4, IntegerExpressionParser.parse("12/3").value());
    }
    
    // covers >1 divisions
    @Test void testMultipleDivisions() throws UnableToParseException {
        assertEquals(2, IntegerExpressionParser.parse("20/5/2").value());
    }
    
    // covers addition and subtraction
    @Test void testAddAndSubtract() throws UnableToParseException {
        assertEquals(7, IntegerExpressionParser.parse("10-5+2").value());
    }
    
    // covers multiplication and division
    @Test void testMultiplyAndDivide() throws UnableToParseException {
        assertEquals(18, IntegerExpressionParser.parse("12/2*3").value());
    }
    
    // covers all four operations with operator precedence
    @Test void testAllOperators() throws UnableToParseException {
        assertEquals(12, IntegerExpressionParser.parse("10+5-3*2/2").value());
    }
    
    // covers subtraction with parentheses
    @Test void testSubtractionWithParens() throws UnableToParseException {
        assertEquals(15, IntegerExpressionParser.parse("20-(3+2)").value());
    }
    
    // covers division with parentheses
    @Test void testDivisionWithParens() throws UnableToParseException {
        assertEquals(4, IntegerExpressionParser.parse("20/(3+2)").value());
    }
    
    // covers integer division (truncation)
    @Test void testIntegerDivision() throws UnableToParseException {
        assertEquals(3, IntegerExpressionParser.parse("10/3").value());
    }
    
    // covers divide by zero error
    @Test void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> {
            IntegerExpressionParser.parse("5/0").value();
        });
    }
    
    // covers divide by zero with expression
    @Test void testDivideByZeroExpression() {
        assertThrows(ArithmeticException.class, () -> {
            IntegerExpressionParser.parse("10/(5-5)").value();
        });
    }
    
    // covers complex expression with all operations
    @Test void testComplexExpression() throws UnableToParseException {
        assertEquals(9, IntegerExpressionParser.parse("(10+20)/3-1").value());
    }
    
}
