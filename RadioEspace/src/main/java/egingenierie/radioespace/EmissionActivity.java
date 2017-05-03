package egingenierie.radioespace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import egingenierie.radioespace.adapter.AdapterForEmissionList;
import egingenierie.radioespace.model.DataLoader;
import egingenierie.radioespace.model.EmissionModel;
import egingenierie.radioespace.model.Message;
import egingenierie.radioespace.network.APIDataLoader;
import egingenierie.radioespace.utils.Constants;
import egingenierie.radioespace.view.EmissionDayView;
import fr.mediametrie.mesure.library.android.Estat;
import fr.mediametrie.mesure.library.android.EstatAudienceTagger;

public class EmissionActivity extends Activity implements
NavigationDrawerFragment.NavigationDrawerCallbacks , OnClickListener,DataLoader {
	
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private List<EmissionModel> lstForEmission = new ArrayList<EmissionModel>();
	private List<EmissionModel> lstEmissionForAll = new ArrayList<EmissionModel>();
	private ListView lstViewForEmission ;
	private Button btnOpenDrawer;
	private LinearLayout lLDaysContainer;
	public String media_url;
	private String selectedDay ;
	private AdView mAdView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Estat.init(this);
		//Creation du tagger mediametrie
		EstatAudienceTagger audienceTagger = Estat.getAudience("298098214380");
		//envoie d'un hit signalant que la page a ete ouverte
		audienceTagger.sendHit("programmes");

		setContentView(R.layout.emisssionscreen);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout),(RelativeLayout) findViewById(R.id.container),EmissionActivity.this);
		Calendar calendar = Calendar.getInstance();
		selectedDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.FRANCE);
		
		lstViewForEmission =(ListView) findViewById(R.id.lstEmission);
		btnOpenDrawer = (Button) findViewById(R.id.btnOpenDrawer);
		lLDaysContainer = (LinearLayout) findViewById(R.id.lLDaysContainer);
		new APIDataLoader(this, "show");
		for(int i=0;i<=30;i++){
			EmissionDayView emissionDayView=new EmissionDayView(this, i);
			emissionDayView.setOnClickListener(this);
			lLDaysContainer.addView(emissionDayView);
		}
		btnOpenDrawer.setOnClickListener(this);
		lstViewForEmission.setAdapter(new AdapterForEmissionList(EmissionActivity.this, R.layout.emissionitems,lstForEmission));
		
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
        RadioHomePage.getRadioHomePage().tracker.setScreenName("Emission Screen ");
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
	public void onNavigationDrawerItemSelected(int position) {
		
	}
	@Override
	public void onClick(View v) {
		if(btnOpenDrawer == v){
			mNavigationDrawerFragment.openDrawer();
		}else if(v instanceof EmissionDayView){
			for(int i=0;i<lLDaysContainer.getChildCount();i++){
				EmissionDayView emissionDayView=(EmissionDayView)lLDaysContainer.getChildAt(i);
				emissionDayView.setUnSelected();
			}
			EmissionDayView	emissionDayView=(EmissionDayView)v;
			emissionDayView.setSelected();
			selectedDay=emissionDayView.setSelectedDate();
			getSelectedData();
		}
	}

	@Override
	public void datOnSuccess(Object object) {
		try {
			JSONObject jsonObject=new JSONObject((String)object);
			media_url=jsonObject.getString("media_url");
			JSONArray jsonArray= jsonObject.getJSONArray("content");
			for(int i=0;i<jsonArray.length();i++){
				lstEmissionForAll.add(new EmissionModel(jsonArray.getJSONObject(i)));
				getSelectedData();
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	private void getSelectedData(){
		lstForEmission.clear();
		for(int i=0;i<lstEmissionForAll.size();i++){
			EmissionModel emissionModel=lstEmissionForAll.get(i);
			emissionModel.setSchedual(getProgramsScheduled(emissionModel));
			if(emissionModel.getSchedual().contains(selectedDay.toLowerCase())){
				lstForEmission.add(emissionModel);
			}else if(emissionModel.isToday()){
				lstForEmission.add(emissionModel);
			}else if(emissionModel.isWeekend()){
				lstForEmission.add(emissionModel);
			}
		}
		((AdapterForEmissionList)lstViewForEmission.getAdapter()).notifyDataSetChanged();
	}
	
	
	
	private String getProgramsScheduled(EmissionModel emissionModel){
		emissionModel.setWeekend(false);
		emissionModel.setToday(false);
		emissionModel.setOpenOrNot(false);
		String startTime ="" , endTime= "";
		String scheduleDays = "" ;
		if(emissionModel.getLundi()==1){
			scheduleDays = scheduleDays+"lundi";
		}
		if(emissionModel.getMardi()==1){
			scheduleDays = scheduleDays+" mardi";
		}
		if(emissionModel.getMercredi()==1){
			scheduleDays = scheduleDays+" mercredi";
		}
		if(emissionModel.getJeudi()==1){
			scheduleDays = scheduleDays+" jeudi";
		}
		if(emissionModel.getVendredi()==1){
			scheduleDays = scheduleDays+" vendredi";
		}
		if(emissionModel.getSamedi()==1){
			scheduleDays = scheduleDays+" samedi";
		}
		if(emissionModel.getDimanche()==1){
			scheduleDays = scheduleDays+" dimanche";
		}
		if(scheduleDays.trim().equals("samedi dimanche")){
			if(scheduleDays.contains(selectedDay.toLowerCase())){
				emissionModel.setWeekend(true);
			}
			scheduleDays = "Le week-end";
		}
		if(scheduleDays.trim().equals("lundi mardi mercredi jeudi vendredi")){
			if(scheduleDays.contains(selectedDay.toLowerCase())){
				emissionModel.setToday(true);
			}
			scheduleDays = "du lundi au vendredi";
		}
		
		startTime = emissionModel.getStart_hour();
		startTime=startTime.substring(0, startTime.length()-3);
		startTime= startTime.replace(":","h");
		
		endTime = emissionModel.getEnd_hour();
		endTime=endTime.substring(0, endTime.length()-3);
		endTime= endTime.replace(":","h");
		
		scheduleDays = scheduleDays+" "+startTime+"-"+endTime;
		
		return scheduleDays.trim();
		//return startTime+"-"+endTime;
	}
	//Sunday (dimanche), Monday (lundi), Tuesday (mardi) , Wednesday (mercredi) , Thursday (jeudi) , Friday (vendredi), Saturday (samedi) ...	
	//"lundi":"1","mardi":"1","mercredi":"1","jeudi":"1","vendredi":"1","samedi":"0","dimanche":"0",
	@Override
	public void datOnError(Message message) {
	}
	
	public void notifyAdapter(int position){
		
		if(lstForEmission.get(position).isOpenOrNot()){
			lstForEmission.get(position).setOpenOrNot(false);
		}else {
			lstForEmission.get(position).setOpenOrNot(true);
		}
		for(int i=0;i<lstForEmission.size();i++){
			if(position!=i){
			lstForEmission.get(i).setOpenOrNot(false);
			}
		}
		((AdapterForEmissionList) lstViewForEmission.getAdapter())
		.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Constants.POPUPCONTEXT = this ;
		RadioHomePage.startTimerForScreens();
	}
}