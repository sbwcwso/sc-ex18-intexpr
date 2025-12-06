package intexpr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;


class IntegerExpressionParserTest {

    // Testing strategy:
    //
    // partition on largest number: single-digit, multi-digit 
    // partition on # of additions: 0, 1, >1
    // partition on # of multiplications: 0, 1, >1
    // partition: add is subexpression of multiply, or not
    // partition: mutiply as subexpression of add, or not
    // partition on parens: required, not required
    
    // covers multidigit number, 0 adds, 0 multiplies
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
    
}
