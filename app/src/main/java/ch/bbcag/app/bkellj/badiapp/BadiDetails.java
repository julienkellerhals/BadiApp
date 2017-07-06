package ch.bbcag.app.bkellj.badiapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by bmuelb on 05.07.2017.
 */

public class BadiDetails extends FakeActivity {
    private String TAG = BadiDetails.class.getName();
    private String badiId;
    private String name;
    private ProgressDialog mDialog;
    private ViewPager viewPager;

    public BadiDetails(String badiId, String name, ViewPager viewPager) {
        this.badiId = badiId;
        this.name = name;
        this.viewPager = viewPager;
    }

    public void show() {
        mDialog = ProgressDialog.show(viewPager.getContext(), "Lade Badi-Infos", "Bitte warten...");
        //Danach wollen wir die Badidaten von der Webseite wiewarm.ch holen und verarbeiten:
        getBadiTemp("http://www.wiewarm.ch/api/v1/bad.json/" + badiId);
    }

    private void getBadiTemp(String url) {
        //Den ArrayAdapter wollen wir später verwenden um die Temperaturen zu speichern
        // angezeigt sollen sie im Format der simple_list_item_1 werden (einem Standard Android Element)
        final ArrayAdapter<String> temps = new ArrayAdapter<String>(viewPager.getContext(), android.R.layout.simple_list_item_1);

        //Android verlangt, dass die Datenverarbeitung von den GUI Prozessen getrennt wird.
        // Darum starten wir hier einen asynchronen Task (quasi einen Hintergrundprozess).

        new AsyncTask<String, String, String>() {
            //Der AsyncTask verlangt die implementation der Methode doInBackground.
            //Nachdem doInBackground ausgeführt wurde, startet automatisch die Methode onPostExecute
            //mit den Daten die man in der Metohde doInBackground mit return zurückgegeben hat (hier msg).
            @Override
            protected String doInBackground(String[] badi) {
                //In der variable msg soll die Antwort der Seite wiewarm.ch gespeichert werden.
                String msg = "";

                if (!isInternetAvailable()) {
                    //no internet connection, return null and handel in postExecute
                    Log.i(TAG, "No internet :(");
                    return null;
                }

                try {
                    URL url = new URL(badi[0]);
                    //Hier bauen wir die Verbindung auf:
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //Lesen des Antwortcodes der Webseite:
                    int code = conn.getResponseCode();
                    //Nun können wir den Lade Dialog wieder ausblenden (die Daten sind ja gelesen)
                    mDialog.dismiss();
                    //Hier lesen wir die Nachricht der Webseite wiewarm.ch für Badi XY:
                    msg = IOUtils.toString(conn.getInputStream());
                    //und Loggen den Statuscode in der Konsole:
                    Log.i(TAG, Integer.toString(code));
                } catch (Exception e) {
                    Log.v(TAG, e.toString());
                }
                return msg;
            }

            public void onPostExecute(String result) {
                if (result == null) {
                    mDialog.dismiss();
                    noInternetInfo(viewPager);
                    return;
                }

                //In result werden zurückgelieferten Daten der Methode doInBackground (return msg;) übergeben.
                // Hier ist also unser Resultat der Seite z.B. http://www.wiewarm.ch/api/v1/bad.json/55
                // In einem Browser IE, Chrome usw. sieht man schön das Resulat als JSON formatiert.
                // JSON Daten können wir aber nicht direkt ausgeben, also müssen wir sie umformatieren.
                try {
                    // Zum Verarbeiten bauen wir die Methode parseBadiTemp und speichern das Resulat in einer Liste.
                    List<String> badiInfos = parseBadiTemp(result);
                    //Jetzt müssen wir nur noch alle Elemente der Liste badidetails hinzufügen.
                    // Dazu holen wir die ListView badidetails vom GUI
                    ListView badidetails = (ListView) viewPager.findViewById(R.id.badidetails);
                    //und befüllen unser ArrayAdapter den wir am Anfang definiert haben (braucht es zum befüllen eines ListViews)
                    temps.addAll(badiInfos);
                    //Mit folgender Zeile fügen wir den befüllten ArrayAdapter der ListView hinzu:
                    badidetails.setAdapter(temps);
                } catch (JSONException e) {
                    Log.v(TAG, e.toString());
                }
            }

            private List<String> parseBadiTemp(String jonString) throws JSONException {
                {
                    //Wie bereits erwähnt können JSON Daten nicht direkt einem ListView übergeben werden.
                    // Darum parsen ("lesen") wir die JSON Daten und bauen eine ArrayListe, die kompatibel
                    // mit unserem ListView ist.
                    ArrayList<String> resultList = new ArrayList<String>();
                    JSONObject jsonObj = new JSONObject(jonString);
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

        }.execute(url);

    }


}
