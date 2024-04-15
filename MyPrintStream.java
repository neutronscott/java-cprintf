import java.io.OutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.Objects;

class MyPrintStream extends PrintStream {
  private static final boolean DEBUG = false, DEBUG1 = false, DEBUG2 = false;
  private static final Pattern printfPattern = Pattern.compile(
     "%" +
     "(?<idx>(?<idxno>[0-9]*)\\$)?" +
     "(?<flags>[-<#+ 0,(]*)?" +
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
    String idx, width, precision;
    int args_i = 0;
    boolean shifted = false;

    if (DEBUG == true || DEBUG1 == true) System.err.printf("fmt='%s'\n", fmt);
    while (m.find()) {
      shifted = false;
      if (DEBUG == true || DEBUG1 == true) {
        System.err.printf("args_i=%d match=%s idx=%s flags=%s width=%s precision=%s conv=%s\n", args_i,
            m.group(), m.group("idx"), m.group("flags"), m.group("width"), m.group("precision"), m.group("conv"));
      }

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
        width = Objects.toString(args[args_i++], "");
        shifted = true;
      } else {
        width = Objects.toString(m.group("width"), "");
      }

      if (m.group("precision") != null &&
          m.group("precision").compareTo(".*") == 0) {
        precision = "." + Objects.toString(args[args_i++], "");
        shifted = true;
      } else {
        precision = Objects.toString(m.group("precision"), "");
      }

      // Java doesn't do empty precisions
      if (precision.compareTo(".") == 0) {
        precision = ".0";
      }

      // Java doesn't do precisions for non-floats?
      if (precision != "" && "ouxXdi".contains(m.group("conv"))) {
        width = precision.replace(".", "0");
        precision = "";
      }

      // cannot remove extra args, but can tell java to skip over them.
      if (m.group("flags") != null && m.group("flags").contains("<") == true) {
        idx = ""; // should be i think
      } else {
        if (m.group("idx") == null) {
          idx = (++args_i) + "$";
        } else {
          int idxno = Integer.parseInt(m.group("idxno"));
          if (idxno <= args_i) idxno += args_i;
          idx = idxno + "$";
        }
      }

      m.appendReplacement(sb, "%" + m.quoteReplacement(idx) + "${flags}" + width + precision + "${conv}");
    }
    m.appendTail(sb);

    if (DEBUG == true || DEBUG2 == true) {
      System.err.printf("printf(\"%s\", %s)\n", sb.toString().trim(), Arrays.toString(args));
    }

    return super.printf(sb.toString(), args);
  }
}
