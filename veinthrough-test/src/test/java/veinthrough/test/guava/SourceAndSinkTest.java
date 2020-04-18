package veinthrough.test.guava;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * Guava source/sink abstract some common(general-purpose) operations
 * from different kinds of char/byte stream.
 * source operations: read()/hash()/readLines()/copyTo()/size()/isEmpty()/contentEquals()
 * sink operations: write()/writeFrom()/writeLines()
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. char source, readLines().
 * 2. char source, read().
 * 3. byte source, hash().
 * 4. byte source, copyTo() and byte sink.
 * </pre>
 */
@RequiredArgsConstructor
@Slf4j

@SuppressWarnings("UnstableApiUsage")
public class SourceAndSinkTest extends AbstractUnitTester {
    private static final String fileName = "source_sink_test.txt";

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    // char source, readLines()
    public void readLinesTest() {
        File file = new File(fileName);
        try {
            ImmutableList<String> lines = Files.asCharSource(file, Charsets.UTF_8)
                    .readLines();
            Iterator<String> iterator = lines.iterator();
            int line = 0;
            while (iterator.hasNext()) {
                log.info(methodLog(
                        String.format("Line %d: %s", ++line, iterator.next())));
            }
        } catch (IOException e) {
            log.info(exceptionLog(e));
        }
    }

    @Test
    // char source, read()
    public void readTest() {
        File file = new File(fileName);
        try {
            Multiset<String> wordOccurrences = HashMultiset.create(
                    Splitter.on(CharMatcher.WHITESPACE)
                            .trimResults()
                            .omitEmptyStrings()
                            .split(Files.asCharSource(file, Charsets.UTF_8).read()));
            for (String element : wordOccurrences.elementSet()) {
                log.info(methodLog(
                        String.format("%s : %d", element, wordOccurrences.count(element))));
            }
        } catch (IOException e) {
            log.info(exceptionLog(e));
        }
    }

    // byte source, hash()
    @Test
    public void hashTest() {
        File file = new File(fileName);
        try {
            HashCode hash = Files.asByteSource(file).hash(Hashing.sha1());
            System.out.println(hash.toString());
        } catch (IOException e) {
            log.info(exceptionLog(e));
        }
    }

    // byte source, copyTo()
    // byte sink
    @Test
    public void sinkTest() {
        File file = new File(fileName);
        try {
            Resources.asByteSource(new URL(
                    "https://issues.apache.org/jira/si/jira.issueviews:issue-xml/IMPALA-2983/IMPALA-2983.xml"))
                    .copyTo(Files.asByteSink(file));
        } catch (IOException e) {
            log.info(exceptionLog(e));
        }
    }
}
