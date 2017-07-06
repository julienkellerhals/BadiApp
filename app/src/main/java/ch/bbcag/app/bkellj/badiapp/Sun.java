package ch.bbcag.app.bkellj.badiapp;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by bmuelb on 06.07.2017.
 */

class Sun extends FakeActivity{
    private ViewPager viewPager;
    private DataHolder dataHolder;
    private Date sunset, sunrise;

    public Sun(ViewPager viewPager, DataHolder dataHolder) {
        this.viewPager = viewPager;
        this.dataHolder = dataHolder;
    }

    public void show() {
        this.sunrise = (Date) dataHolder.get("sunriseDate");
        this.sunset  = (Date) dataHolder.get("sunsetDate" );

        TextView sunriseText = (TextView) viewPager.findViewById(R.id.sunriseText);
        TextView sunsetText  = (TextView) viewPager.findViewById(R.id.sunsetText );

        sunriseText.setText(sunrise.toString());
        sunsetText.setText(sunset.toString());
    }
}
