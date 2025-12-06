package intexpr;

class Minus implements IntegerExpression {
    private final IntegerExpression left, right;
    
    // Abstraction function
    //    AF(left, right) = the expression left - right
    // Rep invariant
    //    true
    // Safety from rep exposure
    //    all fields are immutable and final
    
    /** Make a Minus which is the difference of left and right. */
    public Minus(IntegerExpression left, IntegerExpression right) {
        this.left = left;
        this.right = right;
    }
    
    @Override public int value() {
        return left.value() - right.value();
    }
    
    @Override public String toString() {
        return "Minus(" + left + "," + right + ")";
    }

    // TODO: implement equals() and hashCode()?
}
