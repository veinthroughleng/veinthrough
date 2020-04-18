package veinthrough.api.util;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * @author veinthrough
 * <p>
 * Make log string for method.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodLog {
    private static final String VARIABLES_PRE = "Variables: {";
    private static final String SPACE_PRE = "    ";
    private static final String DELIM_MESSAGE = ", ";
    private static final String DELIM_NEXT_LINE = ",\n" + SPACE_PRE;
    private static final String DELIM_NEXT_VARIABLE = DELIM_NEXT_LINE +
            String.format("%-" + VARIABLES_PRE.length() + "s", " ");
    //    private static final String DELIM_END_LINE = ".";
    private static final String END = "\n";
    private static final int STACK_TRACE_CURRENT = 1;
    // getMethodString -> [0]
    // methodLog -> [1]
    // upper -> [2]
    private static final int STACK_TRACE_UPPER = STACK_TRACE_CURRENT + 2;

    public static String methodLog(String... vars) {
        if (vars.length == 0) {
            return getMethodString() + END;
        } else if (vars.length == 1) {
            return getMethodString()
                    + DELIM_MESSAGE + vars[0]
                    + END;
        } else if (vars.length % 2 != 0) {
            return getMethodString()
                    + DELIM_MESSAGE + vars[0] + DELIM_NEXT_LINE
                    + getVarsString(Arrays.copyOfRange(vars, 1, vars.length))
                    + END;
        } else {
            return getMethodString() + DELIM_NEXT_LINE
                    + getVarsString(vars)
                    + END;
        }
    }

    public static String methodLog(int step, String... vars) {
        if (vars.length == 0) {
            return getMethodString() + getStepString(step) + END;
        } else if (vars.length == 1) {
            return getMethodString() + getStepString(step)
                    + DELIM_MESSAGE + vars[0]
                    + END;
        } else if (vars.length % 2 != 0) {
            return getMethodString()
                    + getStepString(step) + DELIM_MESSAGE + vars[0] + DELIM_NEXT_LINE
                    + getVarsString(Arrays.copyOfRange(vars, 1, vars.length))
                    + END;
        } else {
            return getMethodString()
                    + getStepString(step) + DELIM_NEXT_LINE
                    + getVarsString(vars)
                    + END;
        }
    }public static String exceptionLog(Throwable e) {
        return methodLog(exceptionString(e));
    }

    public static String exceptionLog(Throwable e, String... vars) {
        return methodLog(
                (String[]) ImmutableList.builder()
                        .add(exceptionString(e))
                        .add(vars)
                        .build()
                        .toArray());
    }

    public static String exceptionLog(int step, Throwable e, String... vars) {
        return methodLog(step,
                (String[]) ImmutableList.builder()
                        .add(exceptionString(e))
                        .add(vars)
                        .build()
                        .toArray());
    }

    private static String exceptionString(Throwable e) {
        return e.getClass().getSimpleName() + ": " + e.getMessage();
    }

    public static String getMethodString() {
        return String.format("[IN]%s()",
                Thread.currentThread().getStackTrace()[STACK_TRACE_UPPER].getMethodName());
    }

    private static String getStepString(int step) {
        return String.format("[STEP %03d]", step);
    }

    private static String getVarsString(String... vars) {
        int length = vars.length;
        if (length != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Variables: {");
            for (int i = 0; i < length; i += 2) {
                sb.append(vars[i]).append(": ");
                if (i + 1 < length) sb.append(vars[i + 1]);
                if (i + 2 < length) sb.append(DELIM_NEXT_VARIABLE);
            }
            sb.append("}");
            return sb.toString();
        } else {
            return "";
        }
    }
}
