package egingenierie.radioespace;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import egingenierie.radioespace.radiostreeming.RadioPlayer;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class NavigationDrawerFragment extends Fragment implements
        OnClickListener {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private Context context;
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    View mDrawerListView;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerListViewa,rlMiniPlayer;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private RelativeLayout relativeMain;
    private RelativeLayout txtMaPlayList, txtPodcasts, txtInfos, txtEmission,txtTopEspace,txtDifusses,
            txtEspaceTv,txtFreuency,txtContact,txtPlus;
    private SearchView search;
    Activity activity;
    private RadioHomePage radioHomePage;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, true);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState
                    .getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        selectItem(mCurrentSelectedPosition);
    }

    public void openDrawer() {
        if (isDrawerOpen()) {

            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mDrawerListView = inflater.inflate(R.layout.fragment_navigation_drawer,container, false);
        search = (SearchView) mDrawerListView.findViewById(R.id.srchFromNews);
        search.clearFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txtMaPlayList= (RelativeLayout) mDrawerListView.findViewById(R.id.rlMaPlaylist);
        txtPodcasts= (RelativeLayout) mDrawerListView.findViewById(R.id.rlPodcast);
        rlMiniPlayer=(RelativeLayout)mDrawerListView.findViewById(R.id.rlMiniPlayerContainer);
        txtInfos= (RelativeLayout) mDrawerListView.findViewById(R.id.rlInfos);
        txtEmission= (RelativeLayout) mDrawerListView.findViewById(R.id.rlEmissions);
        txtTopEspace= (RelativeLayout) mDrawerListView.findViewById(R.id.rlTopEspace);
        txtDifusses= (RelativeLayout) mDrawerListView.findViewById(R.id.rlTitresdiffuses);
        txtEspaceTv= (RelativeLayout) mDrawerListView.findViewById(R.id.rlEspaceTV);
        txtFreuency= (RelativeLayout) mDrawerListView.findViewById(R.id.rlFrequences);
        txtContact= (RelativeLayout) mDrawerListView.findViewById(R.id.rlContact);
        txtPlus= (RelativeLayout) mDrawerListView.findViewById(R.id.rlPlus);
        search.setOnClickListener(this);
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) search.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.parseColor("#f7b8b1"));
        search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                /*if (getActivity() instanceof RechercheScreenFragment) {
                    ((RechercheScreenFragment) activity).searchFromSideMenu(query);
                } else {
                    search.clearFocus();
                    Intent intent = new Intent(getActivity(), RechercheScreenFragment.class);
                    intent.putExtra("searchedText", query);
                    startActivity(intent);
                    search.setQuery("", false);
                    if (!(getActivity() instanceof RadioHomePage)) {
                        getActivity().finish();
                    }
                }*/
                /*Intent intent = new Intent(getActivity(),RechercheScreenFragment.class);
				intent.putExtra("searchedText",query);
				startActivity(intent);*/
                mDrawerLayout.closeDrawers();
                return false;
            }

        });
        int searchCloseButtonId = search.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) this.search.findViewById(searchCloseButtonId);
        // Set on click listener
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setQueryHint("Rechercher");
                search.setQuery("", false);
            }
        });
        rlMiniPlayer.setOnClickListener(this);
        txtMaPlayList.setOnClickListener(this);
        txtPodcasts.setOnClickListener(this);
        txtInfos.setOnClickListener(this);
        txtEmission.setOnClickListener(this);
        txtTopEspace.setOnClickListener(this);
        txtDifusses.setOnClickListener(this);
        txtEspaceTv.setOnClickListener(this);
        txtFreuency.setOnClickListener(this);
        txtContact.setOnClickListener(this);
        txtPlus.setOnClickListener(this);

        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null
                && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, RelativeLayout relativeLayout, Activity activity) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        relativeMain = relativeLayout;
        radioHomePage=RadioHomePage.getRadioHomePage();
        context=activity;
        initializeMiniPlayer();
        if(!(activity instanceof RadioHomePage)){
            refreshMiniPlayer();
        }
        this.activity = activity;
        mDrawerLayout.setDrawerShadow(R.drawable.transparent_image,
                GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                R.drawable.menu_icon, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideKeyBoard();
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float xPositionOpenDrawer = mDrawerListView.getWidth();
                float xPositionWindowContent = (slideOffset * xPositionOpenDrawer);
                relativeMain.setX(xPositionWindowContent);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyBoard();
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {

                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
                            .apply();
                }

                getActivity().invalidateOptionsMenu(); // calls
                // onPrepareOptionsMenu()
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public View getActionBarView() {

        Window window = getActivity().getWindow();
        final View decorView = window.getDecorView();
        final String packageName = "android";
        final int resId = this.getResources().getIdentifier(
                "action_bar_container", "id", packageName);
        final View actionBarView = decorView.findViewById(resId);
        return actionBarView;
    }

    private void selectItem(int position) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static interface NavigationDrawerCallbacks {

        void onNavigationDrawerItemSelected(int position);
    }

    @Override
    public void onClick(View v) {
        if(v==rlMiniPlayer){
            mDrawerLayout.closeDrawers();
            if (!(getActivity() instanceof RadioHomePage)) {
                getActivity().finish();
            }
        }else if (v == txtMaPlayList) {
            if (getActivity() instanceof RadioHomePage) {
                // ((RadioHomePage) activity).getRadioList("Radio");
                mDrawerLayout.closeDrawers();

            } else {
                getActivity().finish();
                mDrawerLayout.closeDrawers();
            }
        } else if (v == txtPodcasts) {
            if (!(getActivity() instanceof PodcastCategoryScreen)) {
                Intent intent = new Intent(getActivity(),PodcastCategoryScreen.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        }  else if (txtEmission == v) {
            if (!(getActivity() instanceof EmissionActivity)) {
                Intent intent = new Intent(getActivity(),EmissionActivity.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        }else  if (txtFreuency == v) {
            if (!(getActivity() instanceof FrequenciesHomeScreen)) {
                Intent intent = new Intent(getActivity(), FrequenciesHomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        }else if (txtInfos == v) {
            if (!(getActivity() instanceof InfosScreen)) {
                Intent intent = new Intent(getActivity(), InfosScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        }else if (v == txtPlus) {

        }
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

    }
    private TextView txtArtistName,txtSongName,txtRadioTitle;
    private ImageView imageViewBlure,imgRadioStreem,btnPlayPause;

    private void initializeMiniPlayer(){
        txtRadioTitle= (TextView)mDrawerListView.findViewById(R.id.txtRadioName);
        txtArtistName = (TextView)mDrawerListView.findViewById(R.id.txtArtistName);
        txtSongName = (TextView)mDrawerListView.findViewById(R.id.txtSongName);
        txtArtistName.setTypeface(radioHomePage.library.robotoBold);
        txtSongName.setTypeface(radioHomePage.library.robotoLight);
        imageViewBlure= (ImageView)mDrawerListView.findViewById(R.id.imageViewBlure);
        imgRadioStreem= (ImageView)mDrawerListView.findViewById(R.id.imgStreamImage);
        btnPlayPause= (ImageView)mDrawerListView.findViewById(R.id.imagePayPause);
        btnPlayPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioHomePage.lstForRadio.get(0).isRadioPlaying()){
                    btnPlayPause.setBackgroundResource(R.drawable.radio_player_play_btn);
                    radioHomePage.lstForRadio.get(0).setRadioPlaying(false);
                    RadioPlayer.getRadioPlayer().pauseRadio();
                    radioHomePage.refreshAdapter();

                }else{
                    btnPlayPause.setBackgroundResource(R.drawable.radio_player_pause_btn);
                    radioHomePage.lstForRadio.get(0).setRadioPlaying(true);
                    RadioPlayer.getRadioPlayer().playRadio();
                    radioHomePage.refreshAdapter();

                }
            }
        });
    }
    public void refreshMiniPlayer(){
        if(radioHomePage.lstForRadio.size()>0){
            txtRadioTitle.setText(radioHomePage.lstForRadio.get(0).getTitle().trim());
            txtArtistName.setText(radioHomePage.lstForRadio.get(0).getArtistName());
            txtSongName.setText(radioHomePage.lstForRadio.get(0).getSongName());
            if(radioHomePage.lstForRadio.get(0).isRadioPlaying()){
                btnPlayPause.setBackgroundResource(R.drawable.radio_player_pause_btn);
            }else{
                btnPlayPause.setBackgroundResource(R.drawable.radio_player_play_btn);
            }
            try {
                Picasso.with(context)
                        .load(radioHomePage.lstForRadio.get(0).getStreamImage())
                        .fit().centerInside()
                        .transform(new BlurTransformation(context, 20, 1))
                        .placeholder(getImageResource(radioHomePage.lstForRadio.get(0).getId()))
                        .into(imageViewBlure);
            }catch (Exception ex){
                System.out.print("");
            }

            try {
                Picasso.with(context).load(radioHomePage.lstForRadio.get(0).getStreamImage())
                        .fit().centerInside()
                        .placeholder(getImageResource(radioHomePage.lstForRadio.get(0).getId()))
                        .into(imgRadioStreem);
            }catch (Exception ex){
                System.out.print("");
            }

        }
    }
    private int getImageResource(int imagename) {
        try {
            String uri = "drawable/espace_" + imagename;
            int imageResource = context.getResources().getIdentifier(uri,
                    null, context.getPackageName());
            if (imageResource > 0) {
                return imageResource;
            } else {
                return R.drawable.icon_espace;
            }
        } catch (Exception e) {
            return R.drawable.icon_espace;
        }
    }
}