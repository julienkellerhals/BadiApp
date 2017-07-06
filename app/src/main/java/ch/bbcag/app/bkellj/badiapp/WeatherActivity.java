package ch.bbcag.app.bkellj.badiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class WeatherActivity extends AppCompatActivity {
    private static String TAG = WeatherActivity.class.getName();
    private String city;
    private String country = "ch";
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        city = intent.getStringExtra("city");

        mDialog = ProgressDialog.show(this, "Lade Wetter", "Bitte warten...");
        getWeather("http://api.openweathermap.org/data/2.5/weather?q="+city+","+country+"&APPID=f658806f4a85bcda44d18365457f3a6b&units=metric");
    }

    private void getWeather(String url) {
        Log.wtf(TAG, url);
        final ArrayAdapter<String> temps = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        new AsyncTask<String, String, String>() {
            //Der AsyncTask verlangt die implementation der Methode doInBackground.
            //Nachdem doInBackground ausgeführt wurde, startet automatisch die Methode onPostExecute
            //mit den Daten die man in der Metohde doInBackground mit return zurückgegeben hat (hier msg).
            @Override
            protected String doInBackground(String[] weather) {
                //In der variable msg soll die Antwort gespeichert werden.
                String msg = "";
                try {
                    URL url = new URL(weather[0]);
                    //Hier bauen wir die Verbindung auf:
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //Lesen des Antwortcodes der Webseite:
                    int code = conn.getResponseCode();

                    Log.wtf(TAG, "response code: "+code);

                    //Nun können wir den Lade Dialog wieder ausblenden (die Daten sind ja gelesen)
                    mDialog.dismiss();
                    //Hier lesen wir die Nachricht der Webseite
                    msg = IOUtils.toString(conn.getInputStream());
                    //und Loggen den Statuscode in der Konsole:
                    Log.i(TAG, Integer.toString(code));
                    Log.wtf(TAG, "backgroudn2");
                } catch (Exception e) {
                    Log.v(TAG, e.toString());
                }
                return msg;
            }

            public void onPostExecute(String result) {
                //In result werden zurückgelieferten Daten der Methode doInBackground (return msg;) übergeben.
                // Hier ist also unser Resultat der Seite z.B. http://www.wiewarm.ch/api/v1/bad.json/55
                // In einem Browser IE, Chrome usw. sieht man schön das Resulat als JSON formatiert.
                // JSON Daten können wir aber nicht direkt ausgeben, also müssen wir sie umformatieren.
                try {

                    if (result == null || result == "") {
                        Scanner read = new Scanner(getResources().openRawResource(R.raw.w));
                        result = read.nextLine();

                        Log.wtf(TAG, "result empty");
                    }
                    // Zum Verarbeiten bauen wir die Methode parseBadiTemp und speichern das Resulat in einer Liste.
                    List<String> weatherInfos = parseWeather(result);

                    //Jetzt müssen wir nur noch alle Elemente der Liste badidetails hinzufügen.
                    // Dazu holen wir die ListView badidetails vom GUI
                    ListView weatherDetails = (ListView) findViewById(R.id.weather);
                    //und befüllen unser ArrayAdapter den wir am Anfang definiert haben (braucht es zum befüllen eines ListViews)
                    temps.addAll(weatherInfos);
                    //Mit folgender Zeile fügen wir den befüllten ArrayAdapter der ListView hinzu:
                    weatherDetails.setAdapter(temps);
                } catch (JSONException e) {
                    Log.v(TAG, e.toString());
                }
            }

            private List<String> parseWeather(String jsonString) throws JSONException {
                ArrayList<String> resultList = new ArrayList<String>();
                JSONObject jsonObject = new JSONObject(jsonString);
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
                int temp_min = jsonObject.getJSONObject("main").getInt("temp_min");
                int temp_max = jsonObject.getJSONObject("main").getInt("temp_max");

                int sunrise = jsonObject.getJSONObject("sys").getInt("sunrise");
                int sunset = jsonObject.getJSONObject("sys").getInt("sunset");

                //TODO: save this some way
                Date sunriseDate = new Date(sunrise);
                Date sunsetDate = new Date(sunset);

                resultList.add(description);
                resultList.add(String.valueOf(temp_min));
                resultList.add(String.valueOf(temp_max));

                return resultList;
            }



        }.execute(url);
    }

}
