package veinthrough.test.string;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * How to split string.
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. use split(regex), 参数支持正则表达式, 会处理每一个分隔符, 连续的分隔符中间会有""
 * 2. use StringTokenizer, 参数只是正常的String, 不支持正则表达式
 *   (1) true: 返回每个分割的字符串和分隔符
 *   (2) false; 丢弃所有分隔符, 只返回每个分割的字符串
 */
@Slf4j
public class StringSplitTest extends AbstractUnitTester {
    private static final String str = "Aa|||D|E||";
    // "\\|"支持正则表达式, "|"不支持
    private static final String delim = "|";
    private static final String delimRegex = "\\|";
    private static final String empty = "";

    @Override
    public void test() {
    }

    @Test
    // 5个: "Aa", "", "", "D", "E"
    public void splitTest() {
        // "a", "", "", "D", "E"
        log.info(methodLog(
                // "\\|"支持正则表达式, "|"不支持
                printResult(str.split(delimRegex))));
    }

    @Test
    // 7个: "Aa", "", "", "D", "E", "", ""
    public void trueTokenizerTest() {
        List<String> strList = new ArrayList<>();
        // Unless you ask StringTokenizer to give you the tokens,
        // it silently discards multiple null tokens.
        StringTokenizer st = new StringTokenizer(str, delim, true);
        int i = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            // 处理分隔符
            if (token.equals(delim)) {
                // i表示token(包括分隔的字符串和分隔符)的数量, list.size()表示
                // (1) consecutive delimiters
                if (strList.size() < ++i) {
                    strList.add(empty);
                }
                // (2) the last is delim
                if (!st.hasMoreTokens()) {
                    strList.add(empty);
                }
            } else {
                strList.add(token);
            }
        }
        log.info(methodLog(
                printResult(strList.toArray(new String[0]))));
    }

    @Test
    // 3个: "Aa", "D", "E"
    public void falseTokenizerTest() {
        List<String> strList = new ArrayList<>();
        // StringTokenizer( str, delim, false) is the same as StringTokenizer( str, delim);
        StringTokenizer st = new StringTokenizer(str, delim, false);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            strList.add(token);
        }
        log.info(methodLog(
                printResult(strList.toArray(new String[0]))));
    }

    private String printResult(String[] strs) {
        // noinspection OptionalGetWithoutIsPresent
        return Stream.of(strs)
                .map(str -> ", " + str)
                .reduce((str1, str2) -> str1 + str2)
                .get()
                // delete the first ", "
                .substring(1);
    }
}