package intexpr;

import java.io.IOException;
import java.util.List;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;

public class IntegerExpressionParser {
    
    /**
     * Main method. Parses and evaluates an example expression.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     */
    public static void main(final String[] args) throws UnableToParseException {
        // String input = "54+(2+ 89)";
        String input = "5+ 2+ 8 + 3 * 2 + 10 / 5";
        
        System.out.println(input);
        final IntegerExpression expression = IntegerExpressionParser.parse(input);
        final int value = expression.value();
        System.out.println("value " + value);
    }

    // the nonterminals of the grammar
    private static enum IntegerGrammar {
        EXPR, SUM, PRODUCT, PRIMARY, NUMBER, WHITESPACE,
    }

    private static Parser<IntegerGrammar> parser = makeParser();
    
    /**
     * Compile the grammar into a parser.
     * 
     * @param grammarFilename <b>Must be in this class's Java package.</b>
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<IntegerGrammar> makeParser() {
        try {
            // Read the grammar as a classpath resource, which works better with Maven
            // and allows this code to be packed up in a jar and still find its grammar file
            final java.io.InputStream grammarStream = IntegerExpressionParser.class.getResourceAsStream("IntegerExpression.g");
            return Parser.compile(grammarStream, IntegerGrammar.EXPR);

        // Parser.compile() throws two checked exceptions.
        // Translate these checked exceptions into unchecked RuntimeExceptions,
        // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }

    /**
     * Parse a string into an expression.
     * @param string string to parse
     * @return IntegerExpression parsed from the string
     * @throws UnableToParseException if the string doesn't match the IntegerExpression grammar
     */
    public static IntegerExpression parse(final String string) throws UnableToParseException {
        // parse the example into a parse tree
        final ParseTree<IntegerGrammar> parseTree = parser.parse(string);
        System.out.println("parse tree:\n" + parseTree);

        // display the parse tree in a web browser, for debugging only
        Visualizer.showInBrowser(parseTree);

        // make an AST from the parse tree
        final IntegerExpression expression = makeAbstractSyntaxTree(parseTree);
        System.out.println("abstract syntax tree:\n" + expression);
        
        return expression;
    }
    
    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in IntegerExpression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static IntegerExpression makeAbstractSyntaxTree(final ParseTree<IntegerGrammar> parseTree) {
        switch (parseTree.name()) {
        case EXPR: // expr ::= sum;
            {
                final ParseTree<IntegerGrammar> child = parseTree.children().get(0);
                return makeAbstractSyntaxTree(child);
            }

        case SUM: // sum ::= product ('+' product | '-' product)*;
            {
                final List<ParseTree<IntegerGrammar>> children = parseTree.children();
                IntegerExpression expression = makeAbstractSyntaxTree(children.get(0));
                
                // Extract the full text and find operators between products
                String fullText = parseTree.text();
                int currentPos = 0;
                
                for (int i = 0; i < children.size(); i++) {
                    String productText = children.get(i).text();
                    int productStart = fullText.indexOf(productText, currentPos);
                    
                    if (i > 0) {
                        // Find the operator between previous product and this one
                        String between = fullText.substring(currentPos, productStart).trim();
                        IntegerExpression right = makeAbstractSyntaxTree(children.get(i));
                        if (between.equals("+")) {
                            expression = new Plus(expression, right);
                        } else if (between.equals("-")) {
                            expression = new Minus(expression, right);
                        }
                    }
                    
                    currentPos = productStart + productText.length();
                }
                return expression;
            }

        case PRODUCT: // product ::= primary ('*' primary | '/' primary)*;
            {
                final List<ParseTree<IntegerGrammar>> children = parseTree.children();
                IntegerExpression expression = makeAbstractSyntaxTree(children.get(0));
                
                // Extract the full text and find operators between primaries
                String fullText = parseTree.text();
                int currentPos = 0;
                
                for (int i = 0; i < children.size(); i++) {
                    String primaryText = children.get(i).text();
                    int primaryStart = fullText.indexOf(primaryText, currentPos);
                    
                    if (i > 0) {
                        // Find the operator between previous primary and this one
                        String between = fullText.substring(currentPos, primaryStart).trim();
                        IntegerExpression right = makeAbstractSyntaxTree(children.get(i));
                        if (between.equals("*")) {
                            expression = new Times(expression, right);
                        } else if (between.equals("/")) {
                            expression = new Divide(expression, right);
                        }
                    }
                    
                    currentPos = primaryStart + primaryText.length();
                }
                return expression;
            }

        case PRIMARY: // primary ::= number | '(' sum ')';
            {
                final ParseTree<IntegerGrammar> child = parseTree.children().get(0);
                // check which alternative (number or sum) was actually matched
                switch (child.name()) {
                case NUMBER:
                    return makeAbstractSyntaxTree(child);
                case SUM:
                    return makeAbstractSyntaxTree(child); // in this case, we do the
                                                          // same thing either way
                default:
                    throw new AssertionError("should never get here");
                }
            }

        case NUMBER: // number ::= [0-9]+;
            {
                final int n = Integer.parseInt(parseTree.text());
                return new Number(n);
            }
            
        default:
            throw new AssertionError("should never get here");
        }
    }
}
