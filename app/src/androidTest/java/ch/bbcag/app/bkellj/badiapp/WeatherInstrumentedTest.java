package ch.bbcag.app.bkellj.badiapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.ListView;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


/**
 * Created by bmuelb on 07.07.2017.
 *
 * Funktioniert nicht
 */

public class WeatherInstrumentedTest extends ActivityUnitTestCase<MainActivity>{
    final CountDownLatch signal = new CountDownLatch(1);

    public WeatherInstrumentedTest() {
        super(MainActivity.class);
    }

    @Test
    public void testGetWeather() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(), null, null);
            }
        });

        super.runTestOnUiThread(new Runnable() {
            ViewPager viewPager = new ViewPager(getActivity());

            final Weather weather = new Weather("test", viewPager, new DataHolder());

            @Override
            public void run() {
                weather.getWeather("http://api.openweathermap.org/data/2.5/weather?q=Bern,ch&APPID=f658806f4a85bcda44d18365457f3a6b&units=metric");
            }
        });

        signal.await(30, TimeUnit.SECONDS);
    }



}
