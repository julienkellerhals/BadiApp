package ch.bbcag.app.bkellj.badiapp;

import org.json.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by bmuelb on 07.07.2017.
 */

public class JsonParser {


        public List<String> parseWeather(String jsonString) throws JSONException {
            ArrayList<String> resultList = new ArrayList<String>();
            JSONObject jsonObject = new JSONObject(jsonString);
            String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            int temp_min = jsonObject.getJSONObject("main").getInt("temp_min");
            int temp_max = jsonObject.getJSONObject("main").getInt("temp_max");

            int sunrise = jsonObject.getJSONObject("sys").getInt("sunrise");
            int sunset = jsonObject.getJSONObject("sys").getInt("sunset");

            Date sunriseDate = new Date((long)sunrise*1000);
            Date sunsetDate  = new Date((long)sunset*1000 );

            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            format.setTimeZone(TimeZone.getDefault());

            String sunriseFormated = format.format(sunriseDate);
            String sunsetFormated = format.format(sunsetDate);

            //dataHolder.save("sunrise", sunriseFormated);
            //dataHolder.save("sunset" , sunsetFormated );

            resultList.add(description);
            resultList.add(String.valueOf(temp_min));
            resultList.add(String.valueOf(temp_max));
            resultList.add(sunriseFormated);
            resultList.add(sunsetFormated);

            return resultList;
        }

        public List<String> parseBadiTemp(String jsonString) throws JSONException {
                {
                        //Wie bereits erwähnt können JSON Daten nicht direkt einem ListView übergeben werden.
                        // Darum parsen ("lesen") wir die JSON Daten und bauen eine ArrayListe, die kompatibel
                        // mit unserem ListView ist.
                        ArrayList<String> resultList = new ArrayList<String>();
                        JSONObject jsonObj = new JSONObject(jsonString);
                        JSONObject becken = jsonObj.getJSONObject("becken");
                        //Das ist unser Pointer um aus den JSON Daten alle Datensätze herauszulesen
                        Iterator keys = becken.keys();
                        //Hier holen wir Element für Element aus dem JSON Stream:
                        // Was wo drin steckt, definiert die API der Datenquelle.
                        // Für wiewarm.ch muss man es wie folgt machen:
                        while (keys.hasNext()) {
                                String key = (String) keys.next();
                                JSONObject subObj = becken.getJSONObject(key);
                                //Wenn man die Antwort der Webseite anschaut, steckt im Element "beckenname",
                                // der Name des Schwimmbeckens
                                String name = subObj.getString("beckenname");
                                //und unter temp ist die Temperatur angegeben
                                String temp = subObj.getString("temp");
                                //Sobald wir die Daten haben, fügen wir sie unserer Liste hinzu:
                                resultList.add(name + ": " + temp + " Grad Celsius");
                        }
                        return resultList;
                }
        }
}
