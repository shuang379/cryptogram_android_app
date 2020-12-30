package edu.gatech.seclass.crypto6300;

import org.junit.Before;
import org.junit.Test;
import android.support.test.rule.ActivityTestRule;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;


import static org.junit.Assert.*;

public class UserLoginActivityTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void onClick() {
        String userName =  "admin";
        String userEmail = "admin";
        onView(withId(R.id.textInputEdit)).perform(replaceText(randomUsername));
    }
}