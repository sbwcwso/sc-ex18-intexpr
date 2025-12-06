@skip whitespace {
    expr ::= sum;
    sum ::= product (('+' | '-') product)*;
    product ::= primary (('*' | '/' ) primary)*;
    primary ::= number | '(' sum ')';
}
number ::= [0-9]+;
whitespace ::= [ \t\r\n]+;
