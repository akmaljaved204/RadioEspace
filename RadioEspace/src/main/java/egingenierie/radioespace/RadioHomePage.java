package egingenierie.radioespace;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import egingenierie.radioespace.adapter.AdapterForRadio;
import egingenierie.radioespace.db.AccesDataBase;
import egingenierie.radioespace.model.DataLoader;
import egingenierie.radioespace.model.Message;
import egingenierie.radioespace.model.Radio;
import egingenierie.radioespace.network.APIDataLoader;
import egingenierie.radioespace.network.APIForSearch;
import egingenierie.radioespace.network.RestClient;
import egingenierie.radioespace.popup.ReadMorePopUp;
import egingenierie.radioespace.radiostreeming.RadioPlayer;
import egingenierie.radioespace.utils.AppRater;
import egingenierie.radioespace.utils.Constants;
import egingenierie.radioespace.utils.Library;
import egingenierie.radioespace.utils.SocialSharing;
import fr.mediametrie.mesure.library.android.Estat;
import fr.mediametrie.mesure.library.android.EstatAudienceTagger;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class RadioHomePage extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnClickListener, OnItemClickListener, DataLoader {

    public NavigationDrawerFragment mNavigationDrawerFragment;
    private Button btnOpenDrawer;
    public List<Radio> lstForRadio = new ArrayList<Radio>();
    private ListView lstViewRadio;
    public Library library;
    public String mediaUrl;
    private boolean isAppRunning = true;
    private static RadioHomePage radioHomePage;
    private AccesDataBase accesDataBase;
    private RestClient restClient;
    public GoogleAnalytics analytics;
    public Tracker tracker;
    private AdView mAdView;
    private InterstitialAd interstitialAd;
    private AdapterForRadio radioAdapter;
    private TextView txtRadioName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.POPUPCONTEXT = this;
        radioHomePage = this;
        Estat.init(this);
        //Creation du tagger mediametrie
        EstatAudienceTagger audienceTagger = Estat.getAudience("298098214380");
        //envoie d'un hit signalant que la page a ete ouverte
        audienceTagger.sendHit("accueil");
        new AppRater(this);
        setContentView(R.layout.radiohomescreen);
        overridePendingTransition(0, 0);
        library = new Library(this);
        restClient = new RestClient();
        analytics = GoogleAnalytics.getInstance(getApplicationContext());
        analytics.setLocalDispatchPeriod(1800);
        tracker = analytics.newTracker(Constants.GOOGLE_ANALYTICS_KEY);
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        radioAdapter = new AdapterForRadio(this, android.R.layout.simple_list_item_1, lstForRadio);
        accesDataBase = AccesDataBase.getDataBaseAcces();
        Constants.ACTIVITY_CONTEXT = this;
        btnOpenDrawer = (Button) findViewById(R.id.btnOpenDrawer);
        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout),(RelativeLayout)findViewById(R.id.container),RadioHomePage.this);
        lstViewRadio = (ListView)findViewById(R.id.lstRadio);
        txtRadioName = (TextView)findViewById(R.id.txtTitleOfRadio);
        lstViewRadio.setOnItemClickListener(this);
        btnOpenDrawer.setOnClickListener(this);
        getRadioList();
        mAdView = (AdView)findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        mAdView.setAdListener(new AdListener() {
              @Override
              public void onAdLoaded() {
                  //mAdView.setVisibility(View.VISIBLE);
              }

              @Override
              public void onAdFailedToLoad(int error) {
                  mAdView.setVisibility(View.GONE);
              }
          }

        );
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (interstitialAd != null) {
                        interstitialAd.show();
                    }
                }
            }
        , 10000);
        tracker.setScreenName("Radio Espace Screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void loadIntersicialAd() {
        interstitialAd = new InterstitialAd(RadioHomePage.this);
        interstitialAd.setAdUnitId(Constants.GOOGLE_ADDMOB_INTERSTITIAL_KEY);
        AdRequest interstitialAdRequest = new AdRequest.Builder().addTestDevice("C6651F037C22A3E6155F4C0643510B7B").build();
        interstitialAd.loadAd(interstitialAdRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
                if (lstForRadio.get(0).isRadioPlaying()) {
                    if (RadioPlayer.getRadioPlayer().player != null) {
                        RadioPlayer.getRadioPlayer().player.mute();
                    }
                }
            }

            @Override
            public void onAdClosed() {
                if (lstForRadio.get(0).isRadioPlaying()) {
                    if (RadioPlayer.getRadioPlayer().player != null) {
                        RadioPlayer.getRadioPlayer().player.unMute();
                    }
                }
            }
        });
    }

    public void shareRadioDetail() {
        Constants.ACTIVITY_CONTEXT = this;
        String imageURL = lstForRadio.get(0).getStreamImage();
        if (imageURL.equals("")) {
            imageURL = mediaUrl + lstForRadio.get(0).getFile();
        }
        SocialSharing.getSocialSharing().shareonSocialMedia(RadioHomePage.this, lstForRadio.get(0).getArtistName(), lstForRadio.get(0).getSongName(), imageURL);
    }


    public void getRadioList() {
        lstForRadio.clear();
        if (library.haveNetworkConnection()) {
            new APIDataLoader(this, "webradio");
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void setAdapter() {
        lstViewRadio.setAdapter(radioAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == btnOpenDrawer) {
            mNavigationDrawerFragment.openDrawer();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    public void datOnSuccess(Object object) {
        try {
            JSONObject jsonObject = new JSONObject((String) object);
            mediaUrl = jsonObject.getString("media_url");
            JSONArray jsonArray = jsonObject.getJSONArray("content");
            lstForRadio.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                lstForRadio.add(new Radio(jsonArray.getJSONObject(i)));
            }
            lstForRadio.add(0, lstForRadio.get(0));
            txtRadioName.setText(lstForRadio.get(0).getTitle());
            RadioPlayer.getRadioPlayer().startRadio(lstForRadio.get(0).getFlux_mp3());
            lstForRadio.get(0).setRadioPlaying(true);
            mNavigationDrawerFragment.refreshMiniPlayer();
            setAdapter();
            loadSelectedRadioStreamData();
            loadLiveStreamData();
            loadStreamData();
            callApiAlertTimer();

        } catch (Exception e) {
        }
        loadIntersicialAd();
    }

    @Override
    public void datOnError(Message message) {

    }


    public void setSelectionOfRadio() {
        try {
            lstViewRadio.setSelection(0);
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            if (!lstForRadio.get(position).getFlux_mp3().equals(RadioPlayer.getRadioPlayer().radioURL)) {
                RadioPlayer.getRadioPlayer().startRadio(lstForRadio.get(position).getFlux_mp3());
                for (int i = 0; i < lstForRadio.size(); i++) {
                    if (lstForRadio.get(i).isRadioPlaying()) {
                        lstForRadio.get(i).setRadioPlaying(false);
                        break;
                    }
                }
                lstForRadio.get(position).setRadioPlaying(true);
                lstForRadio.set(0, lstForRadio.get(position));
                txtRadioName.setText(lstForRadio.get(position).getTitle().trim());
                refreshAdapter();
                loadSelectedRadioStreamData();
                setSelectionOfRadio();
                tracker.setScreenName(lstForRadio.get(position).getTitle());
                tracker.send(new HitBuilders.ScreenViewBuilder().build());
            }
        }
    }

    public void refreshAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lstViewRadio != null) {
                    ((AdapterForRadio)lstViewRadio.getAdapter()).notifyDataSetChanged();
                    mNavigationDrawerFragment.refreshMiniPlayer();
                }
            }
        });
    }

    public void OnFavoriteButtonClick(int position) {
        if (lstForRadio.get(position).isFavorite()) {
            messageBox(position);
        } else {
            lstForRadio.get(position).setFavorite(true);
            accesDataBase.makeRadioFavorite(lstForRadio.get(position));
            refreshAdapter();
        }
    }

    private void removeFromFavorite(int position) {
        lstForRadio.get(position).setFavorite(false);
        accesDataBase.removeFavorite(lstForRadio.get(position).getId());

        refreshAdapter();
    }

    public void messageBox(final int position) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle(getResources().getString(R.string.app_name));
        alertbox.setMessage("Désirez vous enlever cette radio de vos favoris ?");
        alertbox.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                removeFromFavorite(position);
            }
        });
        alertbox.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alertbox.show();

    }

    @Override
    protected void onDestroy() {
        RadioPlayer.getRadioPlayer().stopRadio();
        isAppRunning = false;
        super.onDestroy();
    }

    public static RadioHomePage getRadioHomePage() {
        return radioHomePage;
    }

    private void loadStreamData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isAppRunning) {
                    for (int i = 0; i < lstForRadio.size(); i++) {
                        try {
                            String loadStreamURL = "";
                            loadStreamURL = lstForRadio.get(i).getPlaylist();
                            String response = restClient.doHttpGet(lstForRadio.get(i).getPlaylist());
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String streamImage = "", artistName = "", songName = "";
                            streamImage = jsonObject.getString("cover");
                            artistName = jsonObject.getString("artist");
                            songName = jsonObject.getString("title");
                            if (loadStreamURL.equals(lstForRadio.get(i).getPlaylist())) {
                                if ((!lstForRadio.get(i).getArtistName().equals(artistName)) || (!lstForRadio.get(i).getSongName().equals(songName))) {
                                    lstForRadio.get(i).setStreamImage(streamImage);
                                    if (streamImage.equals("")) {
                                        lstForRadio.get(i).setStreamImage(mediaUrl + lstForRadio.get(i).getFile());
                                    }
                                    lstForRadio.get(i).setArtistName(jsonObject.getString("artist"));
                                    lstForRadio.get(i).setSongName(jsonObject.getString("title"));
                                    refreshAdapter();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void loadLiveStreamData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (isAppRunning) {
                    try {
                        String liveStreamURL = "";
                        liveStreamURL = lstForRadio.get(0).getPlaylist();
                        String response = restClient.doHttpGet(lstForRadio.get(0).getPlaylist());
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String streamImage = "", artistName = "", songName = "";
                        streamImage = jsonObject.getString("cover");
                        artistName = jsonObject.getString("artist");
                        songName = jsonObject.getString("title");
                        if (liveStreamURL.equals(lstForRadio.get(0).getPlaylist())) {
                            if ((!lstForRadio.get(0).getArtistName().equals(artistName)) || (!lstForRadio.get(0).getSongName().equals(songName))) {
                                lstForRadio.get(0).setStreamImage(streamImage);
                                if (streamImage.equals("")) {
                                    lstForRadio.get(0).setStreamImage(mediaUrl + lstForRadio.get(0).getFile());
                                }
                                lstForRadio.get(0).setArtistName(artistName);
                                lstForRadio.get(0).setSongName(songName);
                                refreshAdapter();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void loadSelectedRadioStreamData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String selectedStreamURL = "";
                    selectedStreamURL = lstForRadio.get(0).getPlaylist();
                    String response = restClient.doHttpGet(lstForRadio.get(0).getPlaylist());
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String streamImage = "", artistName = "", songName = "";
                    streamImage = jsonObject.getString("cover");
                    artistName = jsonObject.getString("artist");
                    songName = jsonObject.getString("title");
                    if (selectedStreamURL.equals(lstForRadio.get(0).getPlaylist())) {
                        if ((!lstForRadio.get(0).getArtistName().equals(artistName)) || (!lstForRadio.get(0).getSongName().equals(songName))) {
                            lstForRadio.get(0).setStreamImage(streamImage);
                            if (streamImage.equals("")) {
                                lstForRadio.get(0).setStreamImage(mediaUrl + lstForRadio.get(0).getFile());
                            }
                            lstForRadio.get(0).setArtistName(artistName);
                            lstForRadio.get(0).setSongName(songName);
                            refreshAdapter();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void restartRadio() {
        try {

            RadioPlayer.getRadioPlayer().startRadio(lstForRadio.get(0).getFlux_mp3());

        } catch (Exception ex) {
            System.out.print("");
        }

    }

    public void showErrorMessage() {

        if (library.haveNetworkConnection()) {
            restartRadio();
        } else {
            messageBox("Vérifiez votre connexion Internet");
        }
    }

    private void messageBox(final String massage) {
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(RadioHomePage.this);
                alertbox.setTitle(getResources().getString(R.string.app_name));
                alertbox.setMessage(massage);
                alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        RadioPlayer.getRadioPlayer().isRadioError = true;
                    }
                });

                alertbox.show();
            }
        });
    }

    public void callApiAlertTimer() {
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private static Handler customHandler = new Handler();
    private static Runnable updateTimerThread = new Runnable() {
        public void run() {
            try {
                new AsyncTaskForAlert().execute();
            } catch (Exception ex) {
                System.out.print("");
            }
        }
    };

    public static void startTimer() {
        try {
            customHandler.removeCallbacks(updateTimerThread);
        } catch (Exception ex) {
            System.out.print("");
        }
    }

    public static void startTimerMinute() {
        try {
            customHandler.removeCallbacks(updateTimerThread);
            customHandler.postDelayed(updateTimerThread, 60000);
            isMinute = true;
        } catch (Exception ex) {
            System.out.print("");
        }
    }

    public static void startTimerForScreens() {
        try {
            if (!neverDisplayAgain) {
                if (!isMinute) {
                    isDismiss = false;
                    customHandler.removeCallbacks(updateTimerThread);
                    customHandler.postDelayed(updateTimerThread, 6000);
                } else {
                    isDismiss = false;
                }
            }
        } catch (Exception ex) {
            System.out.print("");
        }
    }

    public static void strtDismsPopUp() {
        try {
            customHandler.removeCallbacks(updateTimerThread);
            customHandler.postDelayed(updateTimerThread, 6000);
        } catch (Exception ex) {
            System.out.print("");
        }
    }

    public static void neverShowPopUp() {
        try {
            neverDisplayAgain = true;
            customHandler.removeCallbacks(updateTimerThread);
        } catch (Exception ex) {
            System.out.print("");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startTimerForScreens();
        Constants.POPUPCONTEXT = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationDrawerFragment.refreshMiniPlayer();
    }

    private static ReadMorePopUp alertPopUp;
    private static String alertTitle = "", alertLink = "";
    public static boolean isDismiss = false, neverDisplayAgain = false, isMinute = false;

    public static class AsyncTaskForAlert extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                try {
                    String requestType = "alert_info";
                    String time = new Integer((int) (System.currentTimeMillis() / 1000L)).toString();
                    String sig = Constants
                            .base64Encode(Constants.calculateRFC2104HMACBinary(requestType + time, Constants.PRIVATE_KEY));
                    RestClient client = new RestClient(Constants.BASE_URL + requestType);
                    client.AddHeader("X-Public-Key", Constants.PUBLIC_KEY);
                    client.AddHeader("X-Request-Hash", sig);
                    client.AddHeader("X-Request-Timestamp", time);
                    client.AddHeader("Content-Type", "application/json; charset=utf-8");
                    client.Execute(RestClient.RequestMethod.GET);
                    String response = client.getResponse();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject object = jsonObject.getJSONObject("content");
                    alertTitle = object.getString("title");
                    alertLink = object.getString("link");
                } catch (Exception ex) {
                    System.out.print("");
                }

            } catch (Exception e) {
                System.out.println("");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!isDismiss) {
                alertPopUp = new ReadMorePopUp(Constants.POPUPCONTEXT);
                if (!alertLink.equals("")) {
                    alertPopUp.showPopUp(alertTitle, alertLink);
                }
                Constants.DONTTRPEATPOPUP = false;
                isDismiss = true;
                strtDismsPopUp();
            } else {
                if (!neverDisplayAgain) {
                    isDismiss = false;
                    alertPopUp.disMissPopup();
                    startTimer();
                } else {
                    neverShowPopUp();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        showConfirmationAlert();
    }

    public void showConfirmationAlert() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle(getResources().getString(R.string.app_name));
        alertbox.setMessage("Voulez-vous vraiment quitter cette application?");
        alertbox.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        alertbox.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        alertbox.show();
    }
}