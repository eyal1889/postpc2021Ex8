package com.example.ex8;


import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import androidx.test.core.app.ApplicationProvider;
import androidx.work.Configuration;
import androidx.work.impl.utils.SynchronousExecutor;
import androidx.work.testing.WorkManagerTestInitHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

//import android.content.res.Configuration;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, application = Application.class)

@LooperMode(LooperMode.Mode.PAUSED)
public class CalcTest extends TestCase {
    private ActivityController<MainActivity> activityController;
    private RootHolderImpl mockHolder;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        Configuration config = new Configuration.Builder()
                .setMinimumLoggingLevel(Log.DEBUG)
                .setExecutor(new SynchronousExecutor())
                .build();

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(
                context, config);


        mockHolder = Mockito.mock(RootHolderImpl.class);
        activityController = Robolectric.buildActivity(MainActivity.class);


        MainActivity activityUnderTest = activityController.get();
        activityUnderTest.holder = mockHolder;


    }

    @Test
    public void when_activity_starts_then_userInput_is_empty() {
        activityController.create().visible();
        MainActivity activityNewOrderTest = activityController.get();
        EditText description = activityNewOrderTest.findViewById(R.id.editTextInsertTask);
        String userInput = description.getText().toString();

        assertTrue(userInput.isEmpty());
    }

    @Test
    public void when_activity_starts_and_no_prev_calcs_then_holder_is_empty() {
        activityController.create().visible();
        MainActivity activityNewOrderTest = activityController.get();
        assertEquals(activityNewOrderTest.holder.getCurrentItems().size(),0);
    }


    @Test
    public void when_activity_starts_and_user_types_number_then_button_enabled() {
        activityController.create().visible();
        MainActivity activityNewOrderTest = activityController.get();
        EditText num = activityNewOrderTest.findViewById(R.id.editTextInsertTask);
        FloatingActionButton b =
                activityNewOrderTest.findViewById(R.id.buttonCreateTodoItem);
        num.setText("5");
        assertTrue(b.isEnabled());
    }


}
