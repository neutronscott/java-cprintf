import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.Objects;

class MyPrintStream extends PrintStream {
  private static final Pattern printfPattern = Pattern.compile(
     "%" +
     "(?<idx>[0-9]*\\$)?" +
     "(?<flags>[-#+ 0,(]*)?" +
     "(?<width>\\*|[0-9]*)?" +
     "(?<precision>\\.\\*|\\.[0-9]*)?" +
     "(?<conv>[bBhHsScCdoxXeEfgGaAtT%n])"
  );
  public MyPrintStream(OutputStream out, boolean autoFlush) {
    super(out, autoFlush);
  }

  public PrintStream printf(String fmt, Object... args) {
    Matcher m = printfPattern.matcher(fmt);
    StringBuffer sb = new StringBuffer();
    Object[] new_args = new Object[args.length];
    String width, precision;
    int i = 0, j = 0;

    while (m.find()) {
//      System.err.printf("i%d j%d match=%s idx=%s flags=%s width=%s precision=%s conv=%s\n", i, j,
//          m.group(), m.group("idx"), m.group("flags"), m.group("width"), m.group("precision"), m.group("conv"));

      // conv is required, shouldn't have to null check...
      if (m.group("conv") == null)
        continue;

      if (m.group("conv").compareTo("%") == 0 ||
          m.group("conv").compareTo("n") == 0) {
        // these have no arguments
        continue;
      }

      if (m.group("width") != null &&
          m.group("width").compareTo("*") == 0) {
        width = Objects.toString(args[j], "");
        j++;
      } else {
        width = Objects.toString(m.group("width"), "");
      }

      if (m.group("precision") != null &&
          m.group("precision").compareTo(".*") == 0) {
        precision = "." + Objects.toString(args[j], "");
        j++;
      } else {
        precision = Objects.toString(m.group("precision"), "");
      }

      if (precision.compareTo(".") == 0) {
        // Java doesn't do empty precisions
        precision = ".0";
      }

      m.appendReplacement(sb, "%${idx}${flags}" + width + precision + "${conv}");
      new_args[i++] = args[j++];
    }
    m.appendTail(sb);

    // probably doesn't really matter that we pass some extra nulls but ...?
    Object[] newest_args = new Object[i];
    for (j = 0; j < i; j++) {
      newest_args[j] = new_args[j];
    }

//    System.err.printf("printf(\"%s\", %s)\n", sb.toString().trim(), Arrays.toString(newest_args));
    return super.printf(sb.toString(), newest_args);
  }
}
