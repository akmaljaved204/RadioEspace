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

import egingenierie.radioespace.utils.Constants;

public class NavigationDrawerFragment extends Fragment implements
        OnClickListener {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private Context context;
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    View mDrawerListView;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerListViewa;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private RelativeLayout relativeMain;
    private TextView txtMaPlayList, txtPodcasts, txtInfos, txtEmission,txtTopEspace,txtdifusses,
            txtEspaceTv,txtFreuency,txtContact,txtPlus;
    private SearchView search;
    Activity activity;

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
        txtMaPlayList= (TextView) mDrawerListView.findViewById(R.id.txtRadio);
        txtPodcasts= (TextView) mDrawerListView.findViewById(R.id.txtfavourite);
        txtInfos= (TextView) mDrawerListView.findViewById(R.id.txttopten);
        txtEmission= (TextView) mDrawerListView.findViewById(R.id.txtBrodcast);
        txtTopEspace= (TextView) mDrawerListView.findViewById(R.id.txtNews);
        txtdifusses= (TextView) mDrawerListView.findViewById(R.id.txtBonPlans);
        txtEspaceTv= (TextView) mDrawerListView.findViewById(R.id.txtProgramms);
        txtFreuency= (TextView) mDrawerListView.findViewById(R.id.txtSons);
        txtContact= (TextView) mDrawerListView.findViewById(R.id.txtVideo);
        txtPlus= (TextView) mDrawerListView.findViewById(R.id.txtconcors);
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
                search.setQueryHint("Recherchez sur ODS");
                search.setQuery("", false);
            }
        });

        txtMaPlayList.setOnClickListener(this);
        txtPodcasts.setOnClickListener(this);
        txtInfos.setOnClickListener(this);
        txtEmission.setOnClickListener(this);
        txtTopEspace.setOnClickListener(this);
        txtdifusses.setOnClickListener(this);
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

    public void setUp(int fragmentId, DrawerLayout drawerLayout,
                      RelativeLayout relativeLayout, Activity activity) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        relativeMain = relativeLayout;
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
        if (v == txtMaPlayList) {
            if (getActivity() instanceof RadioHomePage) {
                // ((RadioHomePage) activity).getRadioList("Radio");
                mDrawerLayout.closeDrawers();

            } else {
                getActivity().finish();
                mDrawerLayout.closeDrawers();
            }
        } else if (v == txtdifusses) {
/*
            if (!(getActivity() instanceof TitrusDifussesScreen)) {
                Intent intent = new Intent(getActivity(),
                        TitrusDifussesScreen.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();

        } else if (v == txttopten) {


            if (getActivity() instanceof FavoriteRadio) {
                ((FavoriteRadio) activity).getRadioList("TopTen");

            } else {
                Intent intent = new Intent(getActivity(), FavoriteRadio.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("From", "TopTen");
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }

            }
            mDrawerLayout.closeDrawers();
        } else if (v == txtfavourite) {
			

		*//*	if (getActivity() instanceof FovouriteHomeScreen) {
				((FavoriteRadio) activity).getRadioList("Favourite");
			} else {
				Intent intent = new Intent(getActivity(), FovouriteHomeScreen.class);*//*
            Intent intent = new Intent(getActivity(), FavoriteRadio.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            if (!(getActivity() instanceof RadioHomePage)) {
                getActivity().finish();
            }
            //}
            mDrawerLayout.closeDrawers();
        } else if (txtSons == v) {

            if (getActivity() instanceof NewsActivity) {
                ((NewsActivity) activity).getNewsRelatedData("Sons", "Menu");

            } else {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("From", "Menu");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("ActivityFor", "Sons");
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (v == txtVideos) {


            if (getActivity() instanceof NewsActivity) {
                ((NewsActivity) activity).getNewsRelatedData("Videos", "Menu");

            } else {
                Intent intent = new Intent(getActivity(), NewsActivity.class);

                intent.putExtra("From", "Menu");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("ActivityFor", "Videos");
                startActivity(intent);

                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (v == txtAgenda) {
            if (getActivity() instanceof NewsActivity) {
                ((NewsActivity) activity).getNewsRelatedData("Agenda", "Menu");
            } else {
                Intent intent = new Intent(getActivity(), NewsActivity.class);

                intent.putExtra("From", "Menu");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("ActivityFor", "Agenda");
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();

        } else if (imgHomePage == v) {
            if (!(getActivity() instanceof RadioHomePage)) {
                getActivity().finish();
            } else {
                mDrawerLayout.closeDrawers();
            }
        } else if (txtConcors == v) {
            if (getActivity() instanceof NewsActivity) {
                ((NewsActivity) activity)
                        .getNewsRelatedData("Concours", "Menu");
            } else {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("From", "Menu");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("ActivityFor", "Concours");
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (txtMonCompete == v) {
            if (!(getActivity() instanceof MonCompeteEmailFeedback)) {
                if (Constants.USER_ID.equals("")) {
                    Intent intent = new Intent(getActivity(), MonCompeteEmailFeedback.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    mDrawerLayout.closeDrawers();
                } else {
                    Intent intent = new Intent(getActivity(), MonCompeteHomeScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    mDrawerLayout.closeDrawers();
                }
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (txtFreuency == v) {


            if (!(getActivity() instanceof FrequenciesHomeScreen)) {
                Intent intent = new Intent(getActivity(),
                        FrequenciesHomeScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (txtNewLetter == v) {
            if (!(getActivity() instanceof NewsLetterEmailFeedback)) {
                Intent intent = new Intent(getActivity(), NewsLetterEmailFeedback.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (txtWebsitePage == v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.radioespace.com/"));
            startActivity(browserIntent);
            mDrawerLayout.closeDrawers();
        } else if (txtFacebookPage == v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/pages/G%C3%A9n%C3%A9rations/137991556245195?ref=ts"));
            startActivity(browserIntent);
            mDrawerLayout.closeDrawers();
        } else if (txtTwitterPage == v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/generations"));
            startActivity(browserIntent);
            mDrawerLayout.closeDrawers();
        } else if (txtGoolePlusPage == v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/+generations/videos"));
            startActivity(browserIntent);
            mDrawerLayout.closeDrawers();
        } else if (txtInstagramPage == v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/generationsfm/"));
            startActivity(browserIntent);
            mDrawerLayout.closeDrawers();
        } else if (txtMentionLegalePage == v) {
            if (!(getActivity() instanceof MentionLegaleScreen)) {
                Intent intent = new Intent(getActivity(), MentionLegaleScreen.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (txtEmission == v) {
            if (getActivity() instanceof EmissionActivity) {

            } else {
                Intent intent = new Intent(getActivity(),
                        EmissionActivity.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (search == v) {
            search.setFocusable(true);
        } else if (txtBrodcast == v) {
            if (getActivity() instanceof PodcastCategoryScreen) {

            } else {
                Intent intent = new Intent(getActivity(),
                        PodcastCategoryScreen.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (txtALAune == v) {
            if (getActivity() instanceof ALaUneHomeFragment) {

            } else {
                Intent intent = new Intent(getActivity(),
                        ALaUneHomeFragment.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (v == txtPlus) {
            if (getActivity() instanceof SocialSharingFragment) {

            } else {
                Intent intent = new Intent(getActivity(),
                        SocialSharingFragment.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (txtContact == v) {
            if (getActivity() instanceof AlertActivity) {

            } else {
                Intent intent = new Intent(getActivity(),
                        AlertActivity.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();
        } else if (txtBonPlans == v) {
            if (!(getActivity() instanceof BonPlansScreen)) {
                Intent intent = new Intent(getActivity(), BonPlansScreen.class);
                startActivity(intent);
                if (!(getActivity() instanceof RadioHomePage)) {
                    getActivity().finish();
                }
            }
            mDrawerLayout.closeDrawers();*/
        }
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);

    }
}