package veinthrough.test.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * Test for Preferences.
 * @see PropertiesTest
 * <p>---------------------------------------------------------
 * <pre>
 * Properties:
 * 1. 没有标准的为配置文件命名的规则，容易造成配置文件名冲突。
 * 2. （似乎）只能处理字符串类型
 * Preferences:
 * 1. 可以处理其他类型
 * 2. 提供了一个与平台无关的中心知识库，类似注册表，其实就是用注册表实现的
 * 3. 可以像日志一样每个类可以有一个单独的节点
 * 4. 可以有change listener
 * 5. 不及时清理/维护，会导致中心知识库膨胀
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Warning/Error:
 * 1. WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002.
 *  Windows RegCreateKeyEx(...) returned error code 5.
 *  解决办法：运行注册表regedit.exe，进入HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft，
 *  右击JavaSoft目录，选择新建->项（key），命名为Prefs
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Constructs/APIs:
 *  Preferences systemNodeForPackage(Class<?> c)
 *  Preferences userNodeForPackage(Class<?> c)
 *  addPreferenceChangeListener(PreferenceChangeListener pcl)
 *  addNodeChangeListener(NodeChangeListener ncl)
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. get/put
 * 2. load/store
 * 3. get时可以给一个默认值来实现默认属性
 * </pre>
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class PreferencesTest extends AbstractUnitTester {
    private static final String DIR_NAME = "preferences";
    private static final String FILE_NAME = "preferences_test.xml";
    private static final int DEFAULT_LEFT = 0;
    private static final int DEFAULT_TOP = 0;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;
    private static final String DEFAULT_TITLE = "";

    private int x;
    private int y;
    private int w;
    private int h;
    private String t;

    private Preferences preference = Preferences.userNodeForPackage(getClass());

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void preferencesTest() throws IOException {
        String userDir = System.getProperty("user.home");
        String directory = userDir + "\\" + DIR_NAME;
        File propertiesFile = new File(directory, FILE_NAME);
        if (!propertiesFile.exists()) {
            // noinspection ResultOfMethodCallIgnored
            propertiesFile.createNewFile();
        }

        retrieve();
        modify();
        store(propertiesFile);
        load(propertiesFile);
        retrieve();
    }

    private void retrieve() {
        x = preference.getInt("left", DEFAULT_LEFT);
        y = preference.getInt("top", DEFAULT_TOP);
        w = preference.getInt("width", DEFAULT_WIDTH);
        h = preference.getInt("height", DEFAULT_HEIGHT);
        t = preference.get("title", DEFAULT_TITLE);
        log.info(methodLog(
                "left", "" + x,
                "top", "" + y,
                "width", "" + w,
                "height", "" + h,
                "title", "" + t));
    }

    private void load(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            // load properties
            Preferences.importPreferences(fis);
        } catch (IOException | InvalidPreferencesFormatException e) {
            log.error(exceptionLog(e));
        }
    }

    // can only put string
    private void modify() {
        x += 1;
        preference.put("left", "" + x);
        y += 1;
        preference.put("top", "" + y);
        w += 1;
        preference.put("width", "" + w);
        h += 1;
        preference.put("height", "" + h);
        t += "-";
        preference.put("title", t);
    }

    private void store(File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            preference.exportSubtree(fos);
        } catch (IOException | BackingStoreException e) {
            log.error(exceptionLog(e));
        }
    }
}
