package ohtu.beddit.activity;

import android.test.ActivityInstrumentationTestCase2;
import ohtu.beddit.activity.MainActivity;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class ohtu.beddit.activity.MainActivityTest \
 * ohtu.beddit.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super("ohtu.beddit", MainActivity.class);
    }

}