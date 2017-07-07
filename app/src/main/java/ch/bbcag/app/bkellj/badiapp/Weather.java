package ch.bbcag.app.bkellj.badiapp;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * Created by bmuelb on 05.07.2017.
 */

class Weather extends FakeActivity {
    private static String TAG = Weather.class.getName();
    private String city;
    private String country = "ch";
    private ProgressDialog mDialog;
    private ViewPager viewPager;
    private DataHolder dataHolder;

    public Weather(String city, ViewPager viewPager, DataHolder dataHolder) {
        this.city = city;
        this.viewPager = viewPager;
        this.dataHolder = dataHolder;
    }

    public void show() {
        mDialog = ProgressDialog.show(viewPager.getContext(), "Lade Wetter", "Bitte warten...");
        if (city == null) {
            //city = null returns values for Khetwādi in India, obviously
            mDialog.dismiss();
            return;
        } else {
            getWeather("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&APPID=f658806f4a85bcda44d18365457f3a6b&units=metric");
        }
    }

    public void getWeather(String url) {
        Log.i(TAG, url);
        final ArrayAdapter<String> temps = new ArrayAdapter<String>(viewPager.getContext(), android.R.layout.simple_list_item_1);

        new AsyncTask<String, String, String>() {
            //Der AsyncTask verlangt die implementation der Methode doInBackground.
            //Nachdem doInBackground ausgeführt wurde, startet automatisch die Methode onPostExecute
            //mit den Daten die man in der Metohde doInBackground mit return zurückgegeben hat (hier msg).
            @Override
            protected String doInBackground(String[] weather) {
                //In der variable msg soll die Antwort gespeichert werden.
                String msg = "";

                if (!isInternetAvailable()) {
                    //no internet connection, return null and handel in postExecute
                    Log.i(TAG, "No internet :(");
                    return null;
                }

                try {
                    URL url = new URL(weather[0]);
                    //Hier bauen wir die Verbindung auf:
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //Lesen des Antwortcodes der Webseite:
                    int code = conn.getResponseCode();
                    //Hier lesen wir die Nachricht der Webseite
                    msg = IOUtils.toString(conn.getInputStream());
                    //und Loggen den Statuscode in der Konsole:
                    Log.i(TAG, Integer.toString(code));
                } catch (Exception e) {
                    Log.v(TAG, e.toString());
                }
                return msg;
            }

            public void onPostExecute(String result) {
                //Nun können wir den Lade Dialog wieder ausblenden (die Daten sind ja gelesen)
                mDialog.dismiss();

                if (result == null) {
                    noInternetInfo(viewPager);
                    return;
                }

                //In result werden zurückgelieferten Daten der Methode doInBackground (return msg;) übergeben.
                // Hier ist also unser Resultat der Seite z.B. http://www.wiewarm.ch/api/v1/bad.json/55
                // In einem Browser IE, Chrome usw. sieht man schön das Resulat als JSON formatiert.
                // JSON Daten können wir aber nicht direkt ausgeben, also müssen wir sie umformatieren.
                try {

                    if (Objects.equals(result, "")) {
                        //Fehlermeldung für Benutzer
                        Log.wtf(TAG, "result empty");
                    }
                    // Zum Verarbeiten bauen wir die Methode parseBadiTemp und speichern das Resulat in einer Liste.
                    JsonParser jsonParser = new JsonParser();
                    List<String> weatherInfos = jsonParser.parseWeather(result);

                    //read the sunset & sunrise data out of the list and save it
                    dataHolder.save("sunrise", weatherInfos.get(3));
                    dataHolder.save("sunset", weatherInfos.get(4));

                    //remove them again because we don't want them displayed here
                    weatherInfos.remove(dataHolder.get("sunrise"));
                    weatherInfos.remove(dataHolder.get("sunset"));

                    //Jetzt müssen wir nur noch alle Elemente der Liste badidetails hinzufügen.
                    // Dazu holen wir die ListView badidetails vom GUI
                    ListView weatherDetails = (ListView) viewPager.findViewById(R.id.weather);
                    //und befüllen unser ArrayAdapter den wir am Anfang definiert haben (braucht es zum befüllen eines ListViews)
                    temps.addAll(weatherInfos);
                    //Mit folgender Zeile fügen wir den befüllten ArrayAdapter der ListView hinzu:
                    weatherDetails.setAdapter(temps);
                } catch (JSONException e) {
                    Log.v(TAG, e.toString());
                }
            }



        }.execute(url);
    }
}
