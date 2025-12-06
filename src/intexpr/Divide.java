package intexpr;

class Divide implements IntegerExpression {
    private final IntegerExpression left, right;
    
    // Abstraction function
    //    AF(left, right) = the expression left / right (integer division)
    // Rep invariant
    //    right.value() != 0
    // Safety from rep exposure
    //    all fields are immutable and final
    
    /** 
     * Make a Divide which is the quotient of left divided by right (integer division).
     * @param left dividend
     * @param right divisor
     * @throws ArithmeticException if right evaluates to 0
     */
    public Divide(IntegerExpression left, IntegerExpression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }
    
    private void checkRep() {
        if (right.value() == 0) {
            throw new ArithmeticException("division by zero");
        }
    }
    
    @Override public int value() {
        return left.value() / right.value();
    }
    
    @Override public String toString() {
        return "Divide(" + left + "," + right + ")";
    }

    // TODO: implement equals() and hashCode()?
}
