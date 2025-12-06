@skip whitespace {
    expr ::= sum;
    sum ::= product (addop product)*;
    product ::= primary (mulop primary)*;
    primary ::= number | '(' sum ')';
}
addop ::= '+' | '-';
mulop ::= '*' | '/';
number ::= [0-9]+;
whitespace ::= [ \t\r\n]+;
