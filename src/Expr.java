public class Expr {
    public enum Op {PLUS, TIMES}
    public enum Ref {Z, C}

    Op op;
    Expr[] exprs;
    Ref[] refs;
    Complex[] values;

    Expr(Expr a, Op op, Expr b) {
        exprs = new Expr[] {a, b};
        refs = new Ref[] {};
        values = new Complex[] {};
        this.op = op;
    }

    Expr(Ref a, Op op, Expr b) {
        exprs = new Expr[] {b};
        refs = new Ref[] {a};
        values = new Complex[] {};
        this.op = op;
    }

    Expr(Expr a, Op op, Ref b) {
        exprs = new Expr[] {a};
        refs = new Ref[] {b};
        values = new Complex[] {};
        this.op = op;
    }

    Expr(Complex a, Op op, Expr b) {
        exprs = new Expr[] {b};
        refs = new Ref[] {};
        values = new Complex[] {a};
        this.op = op;
    }

    Expr(Expr a, Op op, Complex b) {
        exprs = new Expr[] {a};
        refs = new Ref[] {};
        values = new Complex[] {b};
        this.op = op;
    }

    Expr(Ref a, Op op, Ref b) {
        exprs = new Expr[] {};
        refs = new Ref[] {a, b};
        values = new Complex[] {};
        this.op = op;
    }

    Expr(Complex a, Op op, Ref b) {
        exprs = new Expr[] {};
        refs = new Ref[] {b};
        values = new Complex[] {a};
        this.op = op;
    }

    Expr(Ref a, Op op, Complex b) {
        exprs = new Expr[] {};
        refs = new Ref[] {a};
        values = new Complex[] {b};
        this.op = op;
    }

    Expr(Complex a, Op op, Complex b) {
        exprs = new Expr[] {};
        refs = new Ref[] {};
        values = new Complex[] {a, b};
        this.op = op;
    }

    Complex getValue(Complex[] complexes) {
        Complex value = null;
        switch (op) {
            case TIMES:
                value = new Complex(1, 0);
                for (Complex complex: values) {
                    value = value.times(complex);
                }
                for (Expr expr: exprs) {
                    value = value.times(expr.getValue(complexes));
                }
                for (Ref ref: refs) {
                    switch (ref) {
                        case Z:
                            value = value.times(complexes[0]);
                            break;
                        case C:
                            value = value.times(complexes[1]);
                            break;
                        default:
                            break;
                    }
                }
                return value;
            case PLUS:
                value = new Complex(0, 0);
                for (Complex complex: values) {
                    value = value.plus(complex);
                }
                for (Expr expr: exprs) {
                    value = value.plus(expr.getValue(complexes));
                }
                for (Ref ref: refs) {
                    switch (ref) {
                        case Z:
                            value = value.plus(complexes[0]);
                            break;
                        case C:
                            value = value.plus(complexes[1]);
                            break;
                        default:
                            break;
                    }
                }
                return value;
            default:
                return value;
        }
    }
}

