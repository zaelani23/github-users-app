package com.zaelani.submission3.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.zaelani.submission3.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {
    @Before
    fun setup(){
        ActivityScenario.launch(HomeActivity::class.java)
    }

    @Test
    fun assertGetMenu(){
        onView(withId(R.id.setting)).check(matches(isDisplayed()))

        onView(withId(R.id.favorite)).check(matches(isDisplayed()))

        onView(withId(R.id.search)).check(matches(isDisplayed()))
    }

    @Test
    fun assertGetMenuClickable(){
        onView(withId(R.id.setting)).check(matches(isClickable()))

        onView(withId(R.id.favorite)).check(matches(isClickable()))

        onView(withId(R.id.search)).check(matches(isClickable()))
    }
}