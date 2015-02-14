package com.mrpaloma.radiotape;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;


public class MainActivity extends BaseActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    Boolean updatePalinsestoAll = true;
    int indexSleepButton = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            oActivity = this;
            context = this.getBaseContext();

            // controllo la connessione dati
            Boolean isConnection = checkConnection(this);
            if (!(isConnection)) {
                Toast.makeText(getBaseContext(), "Error connection...Retry", Toast.LENGTH_LONG).show();
                EasyTrackerCustom.AddException(this, new Exception("Errore connessione dati"), EasyTrackerCustom.TRACK_EVENT_CONNESSIONEDATI);
            }

            // Check device for Play Services APK. If check succeeds, proceed with
            //  GCM registration.
            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this);
                regid = getRegistrationId(context);

                if (regid.isEmpty()) {
                    registerInBackground();
                }

                sCodiceAndroid = regid;
            } else {
                Log.i(CODE_LOG, "No valid Google Play Services APK found.");
            }

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // blocco la rotazione

            // Set up the action bar.
            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // set what the home action does
            CharSequence text = "";
            setTitle(text);

            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            // When swiping between different sections, select the corresponding
            // tab. We can also use ActionBar.Tab#select() to do this if we have
            // a reference to the Tab.
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }
            });

            // For each of the sections in the app, add a tab to the action bar.
            for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                // Create a tab with text corresponding to the page title defined by
                // the adapter. Also specify this Activity object, which implements
                // the TabListener interface, as the callback (listener) for when
                // this tab is selected.
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mSectionsPagerAdapter.getPageTitle(i))
                                .setTabListener(this));
            }

        } catch (Exception e) {
            EasyTrackerCustom.AddException(this, e, "MainActivity - onCreate");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            StartActivitySetting();
            return true;

        } else if (id == R.id.action_exit) {
            StopListen();

            finish();
            moveTaskToBack(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onStart() {
        super.onStart();

        // controllo se è stato avviato un servizio per inviare itinerario
        serviceIntentListen = new Intent(this, ServiceListen.class);
        StartListenService();

        EasyTrackerCustom.AddScreen(this, "Main");
    }

    protected void updateControl() {

        try {

            // controllo se presente la connessione dati
            if ((oActivity != null) && (mSectionsPagerAdapter != null)) {
                Fragment tbListien = mSectionsPagerAdapter.getItem(0);
                if (tbListien != null) {
                    View v = ((TabListen)tbListien).getFragmentView();
                    if (v != null) {
                        LinearLayout lytErrorConnection =(LinearLayout)v.findViewById(R.id.lytErrorConnection);
                        lytErrorConnection.setVisibility(View.GONE);

                        Boolean con = getIsConnection(oActivity);
                        if (!con) lytErrorConnection.setVisibility(View.VISIBLE);
                    }
                }
            }

            // controllo se devo aggiornare il palinsesto
            if (srvListen != null) {
                Palinsesto ptd = srvListen.getPalinsestoToday();
                if ((ptd != null) && (ptd.ITEMS.size() > 0)) {
                    Palinsesto.Giorno g = ptd.ITEMS.get(0);
                    if ((g != null) && (oActivity != null) && (mSectionsPagerAdapter != null)) {
                        Fragment tbListien = mSectionsPagerAdapter.getItem(0);
                        if (tbListien != null) {
                            View v = ((TabListen)tbListien).getFragmentView();
                            if (v != null) {
                                TextView txtTitolo =(TextView)v.findViewById(R.id.txtTitoloNow);
                                if (txtTitolo != null) txtTitolo.setText(g.getTitolo());

                                TextView txtDescrizione =(TextView)v.findViewById(R.id.txtDescrizioneNow);
                                if (txtDescrizione != null) txtDescrizione.setText(g.getDescrizione());

                                //ImageView imgProgramma =(ImageView)v.findViewById(R.id.iconPalinsesto);
                                //if (txtDescrizione != null) imgProgramma.setImageUrl(g.getImage());
                            }
                        }
                    }
                }

                // controllo se sto visualizzando i pulsanti corretti
                if ((mSectionsPagerAdapter != null) && (indexSleepButton == 20)) {
                    Fragment tbListien = mSectionsPagerAdapter.getItem(0);
                    if (tbListien != null) {
                        Button btnPlay = ((TabListen)tbListien).getBtnPlay();
                        Button btnPause = ((TabListen)tbListien).getBtnPause();

                        if (playingMusic) {
                            btnPlay.setVisibility(View.GONE);
                            btnPause.setVisibility(View.VISIBLE);
                        }
                        if (!playingMusic) {
                            btnPlay.setVisibility(View.VISIBLE);
                            btnPause.setVisibility(View.GONE);
                        }
                    }
                } else {
                    if (indexSleepButton > 40) indexSleepButton = 0;
                }
                indexSleepButton++;

                // palinsesto completo
                PalinsestoAll pta = srvListen.getPalinsestoAll();
                if ((pta != null) && (pta.ITEMS.size() > 0)) {
                    Fragment tbProgram = mSectionsPagerAdapter.getItem(1);
                    if (tbProgram != null) {
                        ListView lv = ((TabProgrammi)tbProgram).getListView();

                        TextView loading = (TextView)lv.findViewById(R.id.txtLoading);
                        if (loading != null) loading.setVisibility(View.GONE);

                        ListView list = (ListView)lv.findViewById(R.id.listPalinsesto);
                        if (list != null) list.setVisibility(View.VISIBLE);

                        if ((lv != null) && (!updatePalinsestoAll) && (((TabProgrammi)tbProgram).getLoadPalinsesto())) {
                            updatePalinsestoAll = true;
                            ((TabProgrammi)tbProgram).setLoadPalinsesto(false);
                        }

                        if ((lv != null) && (updatePalinsestoAll)) {
                            PalinsestoAdapter adapter = new PalinsestoAdapter(oActivity.getBaseContext(), R.layout.item_giornopalinsesto, pta.ITEMS);
                            adapter.setActivity(oActivity);

                            lv.setAdapter(adapter);

                            updatePalinsestoAll = false;
                        }
                    }
                }
            }

        }  catch (Exception ex) { EasyTrackerCustom.AddException(oActivity, ex, "MainActivity - updateControl"); }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mFragmentsList;
        public ArrayList<Fragment> getFragmentsList() {return mFragmentsList; }

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            createListFragment();
        }

        protected void createListFragment() {
            mFragmentsList = new ArrayList<Fragment>();

            mFragmentsList.add(new TabListen());
            mFragmentsList.add(new TabProgrammi());
            mFragmentsList.add(new TabWrite());
            mFragmentsList.add(new TabInfo());
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            Fragment returnFragment = mFragmentsList.get(position);

            return returnFragment;
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();

            Fragment fragment = (Fragment)mFragmentsList.get(position);

            if (fragment instanceof TabListen) {
                return getString(R.string.title_ascolta).toUpperCase(l);
            } else if (fragment instanceof TabWrite) {
                return getString(R.string.title_scrivi).toUpperCase(l);
            } else if (fragment instanceof TabProgrammi) {
                return getString(R.string.title_palinsesto).toUpperCase(l);
            } else if (fragment instanceof TabInfo) {
                return getString(R.string.title_info).toUpperCase(l);
            }

            return null;
        }
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }
    }

}
