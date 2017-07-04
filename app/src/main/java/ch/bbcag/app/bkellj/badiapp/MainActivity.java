package ch.bbcag.app.bkellj.badiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter badiliste;
    private final static String AARBERG = "Schwimmbad Aarberg (BE)";
    private final static String ADELBODEN = "Schwimmbad Gruebi Adelboden (BE)";
    private final static String BERN = "Stadtberner Baeder Bern (BE)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ImageView img = (ImageView) findViewById(R.id.badilogo);
        //img.setImageResource(R.drawable.badi);
        addBadisToList();
    }

    private void addBadisToList() {
        //ListView badis = (ListView) findViewById(R.id.badiliste);
        badiliste = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        badiliste.add(AARBERG);
        badiliste.add(ADELBODEN);
        badiliste.add(BERN);
        //badis.setAdapter(badiliste);

        //Definition einer anonymen Klicklistener Klasse
        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), BadiDetailsActivity.class);
                String selected = parent.getItemAtPosition(position).toString();
                //Kleine Infobox anzeigen
                Toast.makeText(MainActivity.this, selected, Toast.LENGTH_SHORT).show();
                //Intent mit Zusatzinformationen - hier die Badi Nummer
                switch (selected) {
                    case AARBERG:
                        intent.putExtra("badi", "71");
                        break;
                    case ADELBODEN:
                        intent.putExtra("badi", "27");
                        break;
                    case BERN:
                        intent.putExtra("badi", "6");
                        break;
                }
                intent.putExtra("name", selected);
                startActivity(intent);
            }
        };
        //badis.setOnItemClickListener(mListClickedHandler);
    }
}
