package com.ryan.ryan.abxstewards;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    String stringToBetyped;
    int[] age = {33, 37, 56, 35, 59, 26, 29, 27, 21, 63, 22, 35, 27, 38, 24, 33};
    int[] ethnicity = {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] fever = {1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0};
    int[] presence_of_giddiness = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] shortness_of_breath_history = {0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1};
    int[] history_of_cancer = {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] highest_pulse_rate = {61, 99, 80, 94, 90, 91, 108, 69, 87, 90, 78, 82, 94, 98, 98, 79};
    double[] highest_body_temp = {36.3, 36.0, 36.7, 36.9, 36.1, 37.4, 37.1, 36.1, 36.7, 36.5, 37.4, 36.6, 39.1, 36.4, 36.9, 36.8};

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

//    @Before
//    public void init() {
//        activityRule.getActivity()
//                .getSupportFragmentManager().beginTransaction();
//    }

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso";
    }

    @Test
    public void changeText_sameActivity() {
        for(int i = 0 ; i < age.length ; i++){
            // Q2 Age
            onView(withId(R.id.edurtiQ2TextField))
                    .perform(typeText(String.valueOf(age[i])), closeSoftKeyboard());

            // Q4 Ethnicity
            if(ethnicity[i] == 1){
                onView(withId(R.id.edurtiQ4Spinner))
                        .perform(click());

                onData(hasToString(startsWith("INDIAN")))
                        .perform(click());

            }

            // Q5 History Of Cancer
            if(history_of_cancer[i] == 1){
                onView(withId(R.id.edurtiQ5TglBtn)).perform(click());

            }

            // Q6 Fever
            if(fever[i] == 1){
                onView(withId(R.id.edurtiQ6TglBtn)).perform(click());

            }

            // Q8 Shortness Of Breath
            if(shortness_of_breath_history[i] == 1){
                onView(withId(R.id.edurtiQ8TglBtn)).perform(click());

            }

            // Q9 Giddiness
            if(presence_of_giddiness[i] == 1){
                onView(withId(R.id.edurtiQ9TglBtn)).perform(click());

            }

            // Q10 Age
//            onView(withId(R.id.edurtiQ10TextField))
//                    .perform(typeText(Double.toString(highest_body_temp[i])));

            onView(withId(R.id.edurtiQ10TextField))
                    .perform(typeText(String.valueOf(highest_body_temp[i])), closeSoftKeyboard());

            // Q11 Age
            onView(withId(R.id.edurtiQ11TextField))
                    .perform(typeText(String.valueOf(highest_pulse_rate[i])), closeSoftKeyboard());

            // Q13 initial 1
            onView(withId(R.id.edurtiQ13Initial1))
                    .perform(typeText("U"));

            // Q13 initial 2
            onView(withId(R.id.edurtiQ13Initial2))
                    .perform(typeText("N"));

            // Q13 initial 3
            onView(withId(R.id.edurtiQ13Initial3))
                    .perform(typeText("I"));

            // Q13 initial 4
            onView(withId(R.id.edurtiQ13Initial4))
                    .perform(typeText("T"), closeSoftKeyboard());

            // Submit button
            onView(withId(R.id.edurtiSubmitBtn)).perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Results fragment dismiss button
            onView(withId(R.id.submitDismissButton)).perform(click());

        }

    }

}
