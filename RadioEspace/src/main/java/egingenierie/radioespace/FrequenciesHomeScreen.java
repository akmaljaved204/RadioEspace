package egingenierie.radioespace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
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

import egingenierie.radioespace.adapter.AdapterForFrequencies;
import egingenierie.radioespace.model.DataLoader;
import egingenierie.radioespace.model.Frequences;
import egingenierie.radioespace.model.Message;
import egingenierie.radioespace.network.APIDataLoader;
import egingenierie.radioespace.utils.Constants;
import egingenierie.radioespace.utils.Library;
import fr.mediametrie.mesure.library.android.Estat;
import fr.mediametrie.mesure.library.android.EstatAudienceTagger;

public class FrequenciesHomeScreen extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, OnClickListener,
		DataLoader {
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private List<Frequences> lstForFrequncy = new ArrayList<Frequences>();
	private ListView lstViewFrequency;
	private Library library;
	private Button btnOpenDrawer ;
	private AdView mAdView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Estat.init(this);
		//Creation du tagger mediametrie
		EstatAudienceTagger audienceTagger = Estat.getAudience("298098214380");
		//envoie d'un hit signalant que la page a ete ouverte
		audienceTagger.sendHit("frequences");

		setContentView(R.layout.frequencieshomescreen);
		overridePendingTransition(0, 0);
		library=new Library(this);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		btnOpenDrawer=(Button) findViewById(R.id.btnOpenDrawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout),
				(RelativeLayout) findViewById(R.id.container),
				FrequenciesHomeScreen.this);
		lstViewFrequency = (ListView) findViewById(R.id.lstRadio);
		if (library.haveNetworkConnection()) {
			new APIDataLoader(this, "frequences");
		} else {
			Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG)
					.show();
		}
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
        RadioHomePage.getRadioHomePage().tracker.setScreenName("Frequencies Screen ");
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
			if(btnOpenDrawer == v){
				mNavigationDrawerFragment.openDrawer();
			}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(0, 0);
	}

	@Override
	public void datOnSuccess(Object object) {
		try {
			JSONObject jsonObject = new JSONObject((String) object);
			JSONArray jsonArray=jsonObject.getJSONArray("content");
			lstForFrequncy.clear();
			for(int i=0;i<jsonArray.length();i++){
				lstForFrequncy.add(new Frequences(jsonArray.getJSONObject(i)));
			}
			lstViewFrequency.setAdapter(new AdapterForFrequencies(this,	R.layout.frequenciesitem, lstForFrequncy));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}



	@Override
	public void datOnError(Message message) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		Constants.POPUPCONTEXT = this ;
		RadioHomePage.startTimerForScreens();
	}
}
