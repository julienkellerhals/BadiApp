package ch.bbcag.app.bkellj.badiapp;

import android.support.annotation.IntDef;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by bmuelb on 06.07.2017.
 */

class Sun extends FakeActivity{
    private ViewPager viewPager;
    private DataHolder dataHolder;
    private String sunset, sunrise;

    public Sun(ViewPager viewPager, DataHolder dataHolder) {
        this.viewPager = viewPager;
        this.dataHolder = dataHolder;
    }

    public void show() {
        this.sunrise = (String) dataHolder.get("sunrise");
        this.sunset  = (String) dataHolder.get("sunset" );

        TextView sunriseText = (TextView) viewPager.findViewById(R.id.sunriseText);
        TextView sunsetText  = (TextView) viewPager.findViewById(R.id.sunsetText );

        sunriseText.setText(sunrise);
        sunsetText.setText(sunset);
    }
}
