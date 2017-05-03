package egingenierie.radioespace;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import egingenierie.radioespace.adapter.AdapterForInfos;
import egingenierie.radioespace.model.NewsItems;
import egingenierie.radioespace.model.Podcast;
import egingenierie.radioespace.network.APIDataLoaderHome;
import egingenierie.radioespace.network.APILoadSinglePodcast;
import egingenierie.radioespace.utils.Constants;
import egingenierie.radioespace.utils.Library;
import egingenierie.radioespace.utils.SocialSharing;
import egingenierie.radioespace.view.InfoFilterView;

public class InfosScreen extends Activity
		implements OnClickListener, NavigationDrawerFragment.NavigationDrawerCallbacks {
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private TextView txtPodcastTitle,txtPodcastSubTitle,txtTotalTime,txtPlayingTime;
	private ImageView btnShare,btnPlayPause;
	private ProgressBar progressBar;
	private Library library;
	private ListView listview;
	private List<NewsItems> newsList = new ArrayList<>();
	private int pageNumber = 0;
	public Tracker tracker;
	public GoogleAnalytics analytics;
	private Button btnOpenDrawer ;
	private LinearLayout filterContainer;
	public boolean isHaveMore=false;
	private AdView mAdView;
	public int selectedCtgry = 1 ;
	private Podcast podcast;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infos_home_screen);
		Constants.POPUPCONTEXT = this ;
		library = new Library(this);
		if (library.haveNetworkConnection()) {
			new APILoadSinglePodcast(this, "podcast/false/false/0");
		} else {
			Toast.makeText(this, "No Internet Available", Toast.LENGTH_LONG).show();
		}
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),(RelativeLayout) findViewById(R.id.container), InfosScreen.this);
		filterContainer= (LinearLayout) findViewById(R.id.lLDaysContainer);
		btnOpenDrawer = (Button) findViewById(R.id.btnOpenDrawer);
		txtPodcastTitle = (TextView) findViewById(R.id.txtTitle);
		txtPodcastSubTitle= (TextView) findViewById(R.id.txtName);
		txtTotalTime= (TextView) findViewById(R.id.textView2);
		txtPlayingTime= (TextView) findViewById(R.id.textView1);
		btnShare= (ImageView) findViewById(R.id.imageViewShareIcon);
		btnPlayPause= (ImageView) findViewById(R.id.btnPlaypauseOnMediaPlayer);
		progressBar= (ProgressBar) findViewById(R.id.pgrsForPlayerStart);
		listview = (ListView) findViewById(R.id.lstAlaUne);
		analytics = GoogleAnalytics.getInstance(getApplicationContext());
		analytics.setLocalDispatchPeriod(1800);
		tracker = analytics.newTracker(Constants.GOOGLE_ANALYTICS_KEY);
		tracker.enableExceptionReporting(true);
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);
		tracker.setScreenName("Info Screen");
		tracker.send(new HitBuilders.ScreenViewBuilder().build());
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
			try {
				if(newsList.get(position).isExpanded()){
					newsList.get(position).setExpanded(false);
				}else{
					newsList.get(position).setExpanded(true);
				}
				notifyAdapter();
			} catch (Exception e) {
				System.out.println("");
			}
			}
		});
		btnOpenDrawer.setOnClickListener(this);
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
		changFilter();
		InfoFilterView filterView=new InfoFilterView(this,1,"LOCALE");
		filterView.setOnClickListener(this);
		filterContainer.addView(filterView);
		filterView=new InfoFilterView(this,2,"TRAFIC");
		filterView.setOnClickListener(this);
		filterContainer.addView(filterView);
		filterView=new InfoFilterView(this,3,"PEOPLE");
		filterView.setOnClickListener(this);
		filterContainer.addView(filterView);
		filterView=new InfoFilterView(this,4,"METEO");
		filterView.setOnClickListener(this);
		filterContainer.addView(filterView);
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
	public void shareNews(int position){
		Constants.ACTIVITY_CONTEXT = this ;
		String urlOfArticle = "http://www.radioespace.com/" + newsList.get(position).getType() + "/" + newsList.get(position).getCategory()+ "/" + newsList.get(position).getNewsId()+ "/" + newsList.get(position).getSlug();
		SocialSharing.getSocialSharing().shareonSocialMediaArticle(InfosScreen.this,newsList.get(position).getTitle() , newsList.get(position).getMediaUrl() + newsList.get(position).getPicture(),urlOfArticle);
	}

	@Override
	public void onClick(View v) {
		if (v == btnOpenDrawer) {
			mNavigationDrawerFragment.openDrawer();
		}else if(v instanceof InfoFilterView){
			InfoFilterView filterView=(InfoFilterView)v;
			if(filterView.getSelectedFilter()!=selectedCtgry){
				((InfoFilterView)filterContainer.getChildAt(selectedCtgry-1)).setUnSelected();
				filterView.setSelected();
				selectedCtgry=filterView.getSelectedFilter();
				changFilter();
			}
		}
	}
	private void changFilter(){
		pageNumber=0;
		String loadURL="news/false/false"+pageNumber;
		if(selectedCtgry==1){
			loadURL="news/false/false"+pageNumber;
		}else if(selectedCtgry==2){
			loadURL="traffic";
		}else if(selectedCtgry==3){
			loadURL="news/people/false/"+pageNumber;
		}else if(selectedCtgry==4){
			loadURL="weather";
		}
		try {
			if (library.haveNetworkConnection()) {
				new APIDataLoaderHome(this, loadURL);
			} else {
				Toast.makeText(this, "No Internet Available", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			System.out.print("");
		}

	}
	public void onPodcastReceive(Podcast podcastL) {
		podcast=podcastL;
		if(podcast!=null){
			txtPodcastTitle.setText(podcast.getTitle());
			txtPodcastSubTitle.setText(podcast.getSubtitle());
		}
	}

	public void onDataReceive(ArrayList<NewsItems> newses ) {
		try {
			if(newses.size()>19){
				isHaveMore=true;
			}else{
				isHaveMore=false;
			}
			if(pageNumber==0){
				newsList.clear();
				newsList=newses;
				loadAdaptor();
			}else{
				for(int i=0;i<newses.size();i++){
					newsList.add(newses.get(i));
				}
				notifyAdapter();
			}
		}catch (Exception ex){
			System.out.print("");
		}
	}


	private void loadAdaptor() {
		listview.setAdapter(new AdapterForInfos(this, R.layout.infos_item,newsList));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void notifyAdapter() {
		((AdapterForInfos) listview.getAdapter()).notifyDataSetChanged();
	}


	public void loadMoreFApi() {
		if (library.haveNetworkConnection()) {
			pageNumber++;
			if(selectedCtgry==1){
				new APIDataLoaderHome(this, "news/false/false/"+pageNumber);
			}else if(selectedCtgry==3){
				new APIDataLoaderHome(this, "news/people/false/"+pageNumber);
			}
		} else {
			Toast.makeText(this, "No Internet Available", Toast.LENGTH_LONG).show();
		}
	}


	@Override
	public void onNavigationDrawerItemSelected(int position) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		Constants.POPUPCONTEXT = this ;
		RadioHomePage.startTimerForScreens();
	}
}