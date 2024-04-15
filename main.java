class main {
  static public void main(String[] args) {
    MyPrintStream out = new MyPrintStream(System.out, true);

    // some tests
    System.out.printf("Expected \"\": ");
    out.printf("\"%.s\"\n", "foobar");

    System.out.printf("Expected \"0.33\": ");
    out.printf("\"%.*f\"\n", 2, 0.333333333);

    System.out.printf("Expected \"            -42\": ");
    out.printf("\"% *d\"\n", 15, -42);

    System.out.printf("Expected \" 42\": ");
    out.printf("\"%1$*.f\"\n", 3, 42.0);

    System.out.printf("Expected \"(42)oo\": ");
    out.printf("\"%(.foo\"\n", -42.0);

    System.out.printf("Expected \"12.0 Hot Pockets\": ");
    out.printf("\"%1$.1f %2$s %3$ss\"\n", 12.0, "Hot", "Pocket");

    System.out.printf("Expected \"3.14      \": ");
    out.printf("\"%-*.*f\"\n", 10, 2, 3.14159265);
  }
}
