public class Complex {
    double a;
    double b;

    Complex(double a, double b) {
        this.a = a;
        this.b = b;
    }

    Complex plus(Complex c) {
        Complex out = new Complex(a + c.a, b + c.b);
        return out;
    }

    Complex times(Complex c) {
        Complex out = new Complex(a*c.a - b*c.b, a*c.b + b*c.a);
        return out;
    }

    Complex times(double c) {
        Complex out = new Complex(a*c, b*c);
        return out;
    }

    double abs() {
        return Math.sqrt(a*a + b*b);
    }
}
