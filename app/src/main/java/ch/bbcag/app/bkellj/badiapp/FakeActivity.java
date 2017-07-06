package ch.bbcag.app.bkellj.badiapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.net.InetAddress;

/**
 * Created by bmuelb on 05.07.2017.
 */

abstract class FakeActivity {
    private String TAG = FakeActivity.class.getName();
    public abstract void show(); //does everything to display stuff

    public boolean isInternetAvailable() {
        try {
            InetAddress test = InetAddress.getByName("google.com");
            Log.i(TAG, test.toString());
            return !test.equals("");
        } catch (Exception e) {
            return false;
        }
    }

    public void noInternetInfo(ViewPager viewPager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewPager.getContext());
        builder.setTitle(R.string.no_inet);
        builder.setMessage(R.string.no_inet_message);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        builder.show();
    }
}
