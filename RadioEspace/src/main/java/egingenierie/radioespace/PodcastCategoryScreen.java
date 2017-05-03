package egingenierie.radioespace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import egingenierie.radioespace.adapter.AdapterForPodcastCategory;
import egingenierie.radioespace.model.DataLoader;
import egingenierie.radioespace.model.Message;
import egingenierie.radioespace.model.PodcastCategory;
import egingenierie.radioespace.network.APIDataLoaderPodcastCategory;
import egingenierie.radioespace.utils.Constants;
import egingenierie.radioespace.utils.Library;

public class PodcastCategoryScreen extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, OnClickListener,DataLoader,OnItemClickListener {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private ListView lvPodcast;
	private List<PodcastCategory> listPodcast = new ArrayList<PodcastCategory>();
	private Button btnOpenMenu;
	private Library library;
	private boolean isActivityLive = true;
	private AdView mAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.podcast_category_screen);
		library = new Library(this);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout),
				(RelativeLayout) findViewById(R.id.container),
				PodcastCategoryScreen.this);
		btnOpenMenu = (Button) findViewById(R.id.btnOpenDrawer);
		lvPodcast= (ListView) findViewById(R.id.lstPodcast);
		btnOpenMenu.setOnClickListener(this);
		lvPodcast.setOnItemClickListener(this);
		if(library.haveNetworkConnection()){
			new APIDataLoaderPodcastCategory(this,"podcast_by_category");
		}else {
			Toast.makeText(this, "No Internet Connection ", Toast.LENGTH_LONG).show();
		}
		mAdView = (AdView) findViewById(R.id.adView);
		mAdView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				mAdView.setVisibility(View.VISIBLE);
				hideBanners();
			}
			@Override
			public void onAdFailedToLoad(int error) {
				mAdView.setVisibility(View.GONE);
			}
		});
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		RadioHomePage.getRadioHomePage().tracker.setScreenName("Podcasts Screen ");
	    RadioHomePage.getRadioHomePage().tracker.send(new HitBuilders.ScreenViewBuilder().build());
	}
	public void hideBanners() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					mAdView.setVisibility(View.GONE);
				} catch (Exception e) {}
			}
		}, 8000);
	}
	@Override
	public void onClick(View v) {
		if (v == btnOpenMenu) {
			mNavigationDrawerFragment.openDrawer();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try {
			Intent intent = new Intent(this,PodcastHomeScreen.class);
			intent.putExtra("podcastDetail", listPodcast.get(position));
			intent.putExtra("mediaUrl",mediaURL);
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
		}
		}	

	public void commentedCode(){
		
	}
	String mediaURL = "" ;
	@Override
	public void datOnSuccess(Object object) {
		try {
			String temp=(String)object;
			JSONObject jsonObject=new JSONObject(temp);
			 mediaURL=jsonObject.getString("media_url");
			JSONArray jsonArray=jsonObject.getJSONArray("content");
			for(int i=0;i<jsonArray.length();i++){
				listPodcast.add(new PodcastCategory(jsonArray.getJSONObject(i)));
			}
			lvPodcast.setAdapter(new AdapterForPodcastCategory(this,R.layout.podcast_category_item, listPodcast,mediaURL));
		} catch (Exception e) {
		}
	}


	@Override
	public void datOnError(Message message) {
		library.messageBox(message.getMessage());
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		
	}
	
	@Override
	protected void onResume() {
		Constants.POPUPCONTEXT = this ;
		RadioHomePage.startTimerForScreens();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		isActivityLive = false;
		super.onDestroy();
	}
}