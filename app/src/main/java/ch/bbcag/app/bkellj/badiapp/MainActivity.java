package ch.bbcag.app.bkellj.badiapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private String badiId, badiName, city;
    private DataHolder dataHolder;

    ArrayAdapter badiliste;
    private final static String AARBERG = "Schwimmbad Aarberg (BE)";
    private final static String ADELBODEN = "Schwimmbad Gruebi Adelboden (BE)";
    private final static String BERN = "Stadtberner Baeder Bern (BE)";


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    private String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataHolder = new DataHolder();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerList = (ListView) findViewById(R.id.navList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        //ImageView img = (ImageView) findViewById(R.id.badilogo);
        //img.setImageResource(R.drawable.badi);
        addBadisToList();
        addDrawerItems();
        setupDrawer();


        //TODO:
        badiId = "40";
        badiName = "NAME";
        city = "Bern";

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        ViewPager.OnPageChangeListener listener =  new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                if (position == 0) {
                    BadiDetails badiDetails = new BadiDetails(badiId, badiName, mViewPager);
                    badiDetails.show();
                }
                if (position == 1) {
                    Weather weather = new Weather(city, mViewPager, dataHolder);
                    weather.show();
                }
                if (position == 2) {
                    Sun sun = new Sun(mViewPager, dataHolder);
                    sun.show();
                }
                //home.startActivity(intent);
                //PlaceholderFragment home = (PlaceholderFragment) mSectionsPagerAdapter.getItem(position);
                //home.becameVisible(mViewPager.getContext(), position);
            }
        };
        listener.onPageSelected(0); // manually set first page
        mViewPager.addOnPageChangeListener(listener);

        initButtons();
    }

    public void initButtons() {
        Button left = (Button) findViewById(R.id.btnLeft);
        Button middle = (Button) findViewById(R.id.btnMiddle);
        Button right = (Button) findViewById(R.id.btnRight);

        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });


        middle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem());
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });
    }

    private void addBadisToList() {
        //ListView badis = (ListView) findViewById(R.id.badiliste);
        badiliste = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //badis.setAdapter(badiliste);

        //Definition einer anonymen Klicklistener Klasse
        /*

        nichtmehr benutzt

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
        }; */
        //badis.setOnItemClickListener(mListClickedHandler);
    }

    private List<String> kantonListe = Arrays.asList(new String[]{"BE", "GR", "ZH", "TG", "AG", "BS", "BL", "SZ", "GL", "SG", "SO", "AR", "NW", "FR", "TI", "LU", "ZG", "OW", "VS"});

    private void addDrawerItems() {
        /* WORKING LIST
        List<String> initalList = new ArrayList<>();
        initalList.add("Wähle Kanton");
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, initalList);*/
        //mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, badiliste);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>(kantonListe));
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(drawerItemClickListener);
    }

    //Drawer Click

    private AdapterView.OnItemClickListener drawerItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final ArrayList<ArrayList<String>> allBadis = BadiData.allBadis(getApplicationContext());
            List<Integer> openPos = new ArrayList<Integer>();

            for (int x = position + 1; x < kantonListe.size(); x++) {
                openPos.add(position);
                mAdapter.remove(kantonListe.get(x));
            }
            for (ArrayList<String> b : allBadis) {
                if (kantonListe.get(position).equals(b.get(6))) {
                    String badi = (b.get(5) + " - " + b.get(8));
                    mAdapter.add(badi);
                }
            }
            for (int x = position + 1; x < kantonListe.size(); x++) {
                mAdapter.add(kantonListe.get(x));
            }


        }
    };

    /*              Working list
    private AdapterView.OnItemClickListener drawerItemClickListener = new AdapterView.OnItemClickListener() {

        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0 && mAdapter.getCount() == 1) {
                // kontone hinzufügen
                for (String kanton : kantonListe) {
                    mAdapter.add(kanton);
                }
            } else if (position == 0 && mAdapter.getCount() == 1 + kantonListe.length) {
                for (String kanton : kantonListe) {
                    mAdapter.remove(kanton);
                }
            }
        }
    }; */


    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }*/

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = home(rootView, container, inflater);
                    break;
                case 2:
                    rootView = weather(rootView, container, inflater);
                    break;
                case 3:
                    rootView = sun(rootView, container, inflater);
                    break;
                case 4:
                    rootView = settings(rootView, container, inflater);
                    break;
            }
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            //weather(rootView, container, inflater);

            return rootView;
        }

        public void becameVisible(Context context, int position) {
            //is called when the page becomes visible
            if (position == 1){
                /*Intent intent = new Intent(context, BadiDetailsActivity.class); //Fragment doesn't actually exist yet, so this doesn't work
                intent.putExtra("badi", "71");
                intent.putExtra("name", "COOL");
                startActivity(intent);*/
            }
        }

        public View home(View view, ViewGroup container, LayoutInflater inflater) {
            view = inflater.inflate(R.layout.activity_badi_details, container, false);
            view.setBackgroundResource(R.color.cool);
            //Button mid = (Button) getActivity().findViewById(R.id.btnMiddle);
            //if (mid != null) mid.setText("HOME");
            return view;
        }

        public View weather(View view, ViewGroup container, LayoutInflater inflater) {
                view = inflater.inflate(R.layout.activity_weather, container, false);
                view.setBackgroundResource(R.color.colorAccent);

                return view;
        }

        public View sun(View view, ViewGroup container, LayoutInflater inflater) {
            view = inflater.inflate(R.layout.activity_sun, container, false);
            view.setBackgroundResource(R.color.colorPrimary);
            //Button mid = (Button) getActivity().findViewById(R.id.btnMiddle);
            //if (mid != null) mid.setText("SUN");
            return view;
        }

        public View settings(View view, ViewGroup container, LayoutInflater inflater) {
            view.setBackgroundResource(R.color.cool);
            //Button mid = (Button) getActivity().findViewById(R.id.btnMiddle);
            //if (mid != null) mid.setText("SETTINGS");
            return view;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int pagesCount = 4;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show total pages.
            return pagesCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position <= pagesCount) return "SECTION "+position+1+"";
            else return null;
        }

        public String getText(int position) {
            switch (position) {
                case 0:
                    return "HOME";
                    //break;
                case 1:
                    return "WEATHER";
                    //break;
                case 2:
                    return "SUN";
                    //break;
                case 3:
                    return "SETTINGS";
                    //break;
                default:
                    return null;
            }
        }

        public void startIntent(Intent intent) {
            startActivity(intent);
        }
    }
}
