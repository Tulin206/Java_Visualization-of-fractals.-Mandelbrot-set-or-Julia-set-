package Fractal;
public final class Complex {

    private final double re;   // real part
    private final double im;   // imaginary part
  
  
    // Constructors
    Complex (double re, double im){
      this.re = re;
      this.im = im;
    }
  
    Complex (double re){
      this(re, 0);
      // this.re = re;
      // this.im = 0;
    }
  
    Complex (){
      this(0);
    }
  
  
    // Constants
    static final Complex I = new Complex(0.0, 1.0);
    static final Complex ZERO = new Complex(0.0);
    static final Complex HALF = new Complex(0.5);
    static final Complex ONE = new Complex(1.0);
  
  

    
    public double getRe() {
      return re;
    }

    public double getIm() {
      return im;
    }

    // Complex number in Cartesian coordinates
    public double real (){
      return this.re;
    }
  
    public double imag (){
      return this.im;
    }
  
  
    // Complex number in Polar coordinates
    public double abs (){
      return Math.hypot(this.re, this.im);
      // return Math.sqrt(z.re*z.re + z.im*z.im);
    }
  
    public double arg (){
      // System.out.println("arg: " + Math.atan2(z.im, z.re));
      return Math.atan2(this.im, this.re);
    }
  
  
    // returning the Complex conjugate of the Complex argument
    public Complex conj(){
      return new Complex(this.re,-this.im);
    }
  
    // Basic mathematical operations with Complex numbers
    public Complex add(Complex other){
      return new Complex (this.re + other.re, this.im + other.im);
    }

    // public Complex add(Complex z1, Complex z2){
    //   return new Complex(z1.re + z2.re, z1.im + z2.im);
      // Complex temp = new Complex ();
      // temp.re = z1.re + z2.re;
      // temp.im = z1.im + z2.im;
  
      // return temp;
    // }

    public Complex sub(Complex other){
      return new Complex (this.re - other.re, this.im - other.im);
    }
  
    // public Complex sub(Complex z1, Complex z2){
    //   return new Complex(z1.re - z2.re, z1.im - z2.im);
      // Complex temp = new Complex (0.0,0.0);
      // temp.re = z1.re - z2.re;
      // temp.im = z1.im - z2.im;
  
      // return temp;
    // }

    public Complex mul(Complex other){
      return new Complex ((this.re * other.re)-(this.im * other.im), (this.re * this.im) + (this.im + other.re));
    }
  
    // public Complex mul(Complex z1, Complex z2){
    //   return new Complex((z1.re * z2.re)-(z1.im * z2.im), (z1.re * z2.im) + (z1.im + z2.re));
      // Complex temp = new Complex (0.0,0.0);
      // temp.re = (z1.re * z2.re)-(z1.im * z2.im);
      // temp.im = (z1.re * z2.im)+(z1.im+z2.re);
  
      // return temp;
    // }

    public Complex div(Complex other){
      double tempRe = ((this.re * other.re) + (this.im * other.im)) / (other.re * other.re + other.im * other.im);
      double tempIm = ((this.im * other.re) - (this.re + other.im)) / (other.re * other.re + other.im * other.im);
      return new Complex (tempRe, tempIm);
    }

  
    // public Complex div(Complex z1, Complex z2){
    //   double tempRe = ((z1.re * z2.re) + (z1.im * z2.im)) / (z2.re * z2.re + z2.im * z2.im);
    //   double tempIm = ((z1.im * z2.re) - (z1.re + z2.im)) / (z2.re * z2.re + z2.im * z2.im);
    //   return new Complex (tempRe, tempIm);
      // temp.re = ((z1.re * z2.re) + (z1.im * z2.im)) / (z2.re * z2.re + z2.im * z2.im);
      // temp.im = ((z1.im * z2.re) - (z1.re + z2.im)) / (z2.re * z2.re + z2.im * z2.im);
  
      // return temp;
    // }
  
  
  
  
    // More mathematical functions
     // Check for (almost) equality of the two passed double numbers
     private boolean almost_equal(double x, double y, double tol) {
      return Math.abs(x - y) <= tol * Math.abs(x + y);
    }
  
    // Check for (almost) equality of the two passed double numbers
    private boolean almost_equal(double x, double y) {
      return almost_equal(x, y, 1.e-15);
    }
  
    // Check for (almost) equality of the two passed Complex numbers
    public boolean equals(Complex z1, Complex z2) {
      return almost_equal(z1.re, z1.re) &&
             almost_equal(z1.im, z1.im);
    }
  
    // static boolean equals(Complex z1, Complex z2){
    //   // method to find out apprpximate equality of two Complex numbers
    //   return z1.re == z2.re && z1.im == z2.im;
    // }
  
  
    // Complex -> String --- as a pair of real numbers:  (re, im)
    public String toString() {
      return "(" + re + ", " + im + ")";
      // alternative representation:
      // Complex -> String --- "1.0 - i*3.0"
      // if (im < 0.0) {
      //   return "(" + re + " - i*" + -im + ")";
      // }
      // else {
      //   return "(" + re + " + i*" +  im + ")";
      // }
    }
  
  
    /**
     * Assertion printing to the standard output (terminal). Note, the standard
     * `assert` statement should be preferred in general, but results in a very verbose
     * output that is not needed for this simple example.
     *
     * @param passed   Result of a validation statement
     * @param message  String representing a message what is going wrong
     **/
    private static void assert_out(boolean passed, String message) {
      if ( ! passed ) {
        System.out.println("WARNING: Assertion raised: " + message);
      }
    }
  
  
    /* Test for validity of the basic arithmetic operations as well as of the other
     * mathematical functions defined above. If a test fails, a message is printed to
     * the terminal by using the class method `assert_out(...)`.
     */
    private static void test() {
      Complex z1 = new Complex( 3.0, -7.0);
      Complex z2 = new Complex(-5.0, 11.0);
  
      assert_out(z1.equals(z1, new Complex(3.0, -7.0)),
           "equals incorrectly defined!");
  
      assert_out(!z2.equals(z2, new Complex(3.0, -7.0)),
           "equals incorrectly defined!");
      assert_out(z1.equals(z1.add(z2), new Complex(-2.0,  4.0)),
           "Complex addition incorrectly defined!");
      assert_out(z1.equals(z1.sub(z2), new Complex( 8.0,-18.0)),
           "Complex subtraction incorrectly defined!");
      assert_out(z1.equals(z1.mul(z2), new Complex(62.0, 68.0)),
           "Complex multiplication incorrectly defined!");
      assert_out(z1.equals(z1.div(z2), new Complex(-46.0/73.0, 1.0/73.0)),
           "Complex division incorrectly defined!");
      assert_out(z1.equals(z1.conj(), new Complex(3.0, 7.0)),
           "Complex conjugation incorrectly defined!");
      assert_out(z1.abs() == 7.615773105863909,
           "Complex absolute value incorrectly defined!");
      assert_out(z2.arg() == 1.9974238199217726,
           "Complex angle incorrectly defined!");
    }
  
  
    /* Test for validity of arithmetic identities like associativity and commutativity
     * of the basic arithmetic operations and some of the other mathematical functions.
     */
    private static void test(Complex z1, Complex z2) {
      System.out.println("\nComplex test started");
      System.out.println("z1: " + z1 + " ... z2: " + z2);
      System.out.println("z1 == z2: " + z1.equals(z1,z2) +
                    " ... z2 == z1: " + z2.equals(z2,z1));
      System.out.println("z1.conj(): " + z1.conj() +
                    " ... z2.conj(z2): " + z2.conj());
  
      if ( !z1.equals(z1, z1.conj().conj())) {
        System.out.println("ERROR in conj");
      }
  
      if ( !z2.equals(z2, z2.conj().conj())) {
        System.out.println("ERROR in conj");
      }
  
      System.out.println("real(z1)(): " + z1.real() +
                    " ... imag(z1): " + z1.imag());
      System.out.println("real(z2)(): " + z2.real() +
                    " ... imag(z2): " + z2.imag());
  
      System.out.println("abs(z1): " + z1.abs() +
                    " ... arg(z1): " + z1.arg());
      System.out.println("abs(z1): " + z1.abs() +
                    " ... arg(z1): " + z1.arg());
  
      System.out.println("Add: " + z1.add(z2) + " ... " + z2.add(z1));
      System.out.println("Sub: " + z1.sub(z2) + " ... " + z2.sub(z1));
      System.out.println("Mul: " + z1.mul(z2) + " ... " + z2.mul(z1));
      System.out.println("Div: " + z1.div(z2) + " ... " + z2.div(z1));
  
      System.out.println("ARI: " + z1.add(z2).sub(z1).sub(z2) );
      System.out.println("ARI: " + z1.add(z2.sub(z1)).sub(z2) );
      System.out.println("ARI: " + z1.mul(z2).div(z1).div(z2) );
      System.out.println("ARI: " + z1.mul(z2.div(z1)).div(z2) );
  
      System.out.println("Complex test finished");
    }
  
  
    // Main entrance point of the Java program that runs some test cases.
    public static void main(String[] args) {
      test();
      Complex obj = new Complex();
  
      test(new Complex(2.0, -3.0),       new Complex(5.0, -7.0));
      test(new Complex(1.0),             new Complex(0.0, 1.0));
      test(new Complex(Math.PI, Math.E), new Complex(Math.PI, Math.E));
      test(new Complex(3.0, 4.0),        new Complex(4.0, 3.0));
      test(new Complex(0.0, 0.0),        new Complex(0.0, 0.0));
  
      if ( !obj.equals(new Complex(13.0), new Complex(13.0, 0.0)) ) {
        System.out.println("ERROR in Constructors");
      }
  
      // Check that a constant cannot be modified
      // I = I.add(new Complex(1.0, -1.0));   // Compiler error
      System.out.println("\nImaginary unit: " + I);
      // Check whether attributes of imaginary unit are mutable
      // I.re= 5.0;
      System.out.println("Imaginary unit? " + I + "\n");
    }
  
  }
  
  
