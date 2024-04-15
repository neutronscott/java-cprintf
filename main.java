import java.util.Calendar;
import java.util.GregorianCalendar;
import static java.util.Calendar.*;

class main {
  static public void main(String[] args) {
    MyPrintStream out = new MyPrintStream(System.out, true);
    Calendar c = new GregorianCalendar(1995, MAY, 23);

    // some tests
    System.out.printf("Expected '': ");
    out.printf("'%.s'\n", "foobar");

    System.out.printf("Expected '0.33': ");
    out.printf("'%.*f'\n", 2, 0.333333333);

    System.out.printf("Expected '            -42': ");
    out.printf("'% *d'\n", 15, -42);

    System.out.printf("Expected ' 42': ");
    out.printf("'%1$*.f'\n", 3, 42.0);

    System.out.printf("Expected '(42)oo': ");
    out.printf("'%(.foo'\n", -42.0);

    System.out.printf("Expected '12.0 Hot Pockets': ");
    out.printf("'%1$.1f %2$s %3$ss'\n", 12.0, "Hot", "Pocket");

    System.out.printf("Expected '3.14      ': ");
    out.printf("'%-*.*f'\n", 10, 2, 3.14159265);

    System.out.printf("Expected '+7.894561230000000e+08': ");
    out.printf("'%+#22.15e'\n", 789456123.0);

    System.out.printf("Expected '0000000000001234ABCD': ");
    out.printf("%.20X\n", 305441741);

    System.out.printf("Expected 'b a a b': ");
    out.printf("%2$s %s %<s %s\n", "a", "b", "c", "d");
    
    System.out.printf("Expected 'Duke's Birthday: 05 23,1995': ");
    out.printf("Duke's Birthday: %1$tm %<te,%<tY\n", c);

    System.out.printf("Expected ' 1 1 2': ");
    out.printf("%1$d %1$*d %d\n", 1, 2, 3);
    System.out.printf("But it's really not allowed by the spec so that's ok...?\n");

    System.out.printf("Expected ' 2 2 2 1': ");
    out.printf("%2$d %2$d %2$d %d\n", 1, 2, 3, 4);

    System.out.printf("Expected ' 2 2 2 2': ");
    out.printf("%2$d %2$*d %2$d %d\n", 1, 2, 3, 4);

  }
}
