package egingenierie.radioespace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import egingenierie.radioespace.adapter.ShareIntentListAdapter;
import egingenierie.radioespace.utils.Constants;


public class LesPlusScreen extends Activity implements OnClickListener ,NavigationDrawerFragment.NavigationDrawerCallbacks{
	private RelativeLayout partagerEmailLayout,
			partagerNoterLayout, surviveLyonmagLayout, surviveFacebookLayout, surviveTwitterLayout,rlForShreInstgrm,surviveGPlusLayout;
	private Tracker tracker ;
	public GoogleAnalytics analytics;
	private  Button button1 ;
	private Button btnOpenDrawer ;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.les_plus_screen);
		Constants.ACTIVITY_CONTEXT = this;
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout),
				(RelativeLayout) findViewById(R.id.container),
				LesPlusScreen.this);
		partagerEmailLayout = (RelativeLayout) findViewById(R.id.partagerEmailLayout);
		partagerNoterLayout = (RelativeLayout) findViewById(R.id.partagerNoterLayout);
		surviveLyonmagLayout = (RelativeLayout) findViewById(R.id.surviveLyonmagLayout);
		surviveFacebookLayout = (RelativeLayout) findViewById(R.id.surviveFacebookLayout);
		surviveTwitterLayout = (RelativeLayout) findViewById(R.id.surviveTwitterLayout);
		rlForShreInstgrm = (RelativeLayout) findViewById(R.id.rlForShreInstgrm);
		surviveGPlusLayout = (RelativeLayout) findViewById(R.id.surviveGPlusLayout);
		btnOpenDrawer = (Button) findViewById(R.id.btnOpenDrawer);
		button1 = (Button) findViewById(R.id.button1);
		partagerEmailLayout.setOnClickListener(this);
		partagerNoterLayout.setOnClickListener(this);
		surviveLyonmagLayout.setOnClickListener(this);
		surviveFacebookLayout.setOnClickListener(this);
		surviveTwitterLayout.setOnClickListener(this);
		rlForShreInstgrm.setOnClickListener(this);
		surviveGPlusLayout.setOnClickListener(this);
		btnOpenDrawer.setOnClickListener(this);
		button1.setOnClickListener(this);
		analytics = GoogleAnalytics.getInstance(getApplicationContext());
		analytics.setLocalDispatchPeriod(1800);
		tracker = analytics.newTracker(Constants.GOOGLE_ANALYTICS_KEY);
		tracker.enableExceptionReporting(true);
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);
		tracker.setScreenName("Les Plus Screen");
		tracker.send(new HitBuilders.ScreenViewBuilder().build());

	}

	public void getOpenFacebookIntent() {
		try {
			int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
			if (versionCode >= 3002850) {
				Uri uri = Uri.parse("fb://facewebmodal/f?href=" + Constants.FACEBOOK_PAGE_LINK);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));;
			} else {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/104998529389")));
			}
		} catch (PackageManager.NameNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse( Constants.FACEBOOK_PAGE_LINK)));
		}

	}

	private ShareIntentListAdapter objShareIntentListAdapter ;
	public void shareonSocialMedia(){
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		final Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		sharingIntent.setType("text/plain");
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
		List<String> activityNames = new ArrayList<String>();
		for(int i= 0 ;i<activityList.size();i++){
			String name =activityList.get(i).activityInfo.targetActivity;
			if(name==null){
				name =activityList.get(i).activityInfo.parentActivityName;
			}
			if(name!=null){
			if(name.contains("com.facebook.saved.intentfilter.ExternalSaveActivity")) {
				activityNames.add("Save To Facebook");
			}else if (name.contains("com.twitter.android.RootDMActivity")){
				activityNames.add("Direct Message");
			}else{
				activityNames.add(activityList.get(i).activityInfo.applicationInfo.loadLabel(getPackageManager()).toString());
			}
			}else {
				activityNames.add(activityList.get(i).activityInfo.applicationInfo.loadLabel(getPackageManager()).toString());
			}
		}
		objShareIntentListAdapter = new ShareIntentListAdapter(this,activityList.toArray(),activityNames);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setAdapter(objShareIntentListAdapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				ResolveInfo info = (ResolveInfo) objShareIntentListAdapter.getItem(item);
				if(info.activityInfo.packageName.contains("mms")
						|| info.activityInfo.packageName.contains("conversation")) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
					intent.setType("text/plain");
					intent.putExtra("android.intent.extra.TEXT",getResources().getString(R.string.lesPlusTitle)+"\n"+getResources().getString(R.string.radioDetail) +"\n"+"\n"+  Constants.APP_LINK);
					startActivity(intent);
				}else {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_SUBJECT,getResources().getString(R.string.app_name));
					intent.putExtra("android.intent.extra.TEXT",getResources().getString(R.string.lesPlusTitle)+"\n"+getResources().getString(R.string.radioDetail) +"\n"+"\n"+ Constants.APP_LINK);
					intent.putExtra(Intent.EXTRA_STREAM,getLocalBitmapUri(largeIcon,"image"));
					startActivity(intent);
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onClick(View v) {
		if(v==partagerEmailLayout){
			shareonSocialMedia();
		}else if(v==partagerNoterLayout){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Constants.APP_LINK));
			startActivity(browserIntent);
		}else if(v==surviveLyonmagLayout){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(Constants.FACEBOOK_PAGE_LINK));
			startActivity(browserIntent);
		}else if(v==surviveFacebookLayout){
			try {
			ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo("com.facebook.katana", 0);
			if (applicationInfo.enabled) {
				getOpenFacebookIntent();
			}else {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse(Constants.FACEBOOK_PAGE_LINK));
				startActivity(browserIntent);
			}
			} catch (PackageManager.NameNotFoundException ignored) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(Constants.FACEBOOK_PAGE_LINK));
				startActivity(browserIntent);
			}
		}else if(v==surviveTwitterLayout){
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(Constants.TWITTER_LINK));
			startActivity(i);
		}else if(surviveGPlusLayout == v){
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(Constants.GPLUS_LINK));
			startActivity(i);
		}else if(rlForShreInstgrm == v){
			String url=Constants.INSTAGRAM_LINK;
			try {
				Intent intent = newInstagramProfileIntent(getPackageManager(), url);
				startActivity(intent);
			}catch (Exception ex){
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
				startActivity(browserIntent);
			}
		}else if(btnOpenDrawer == v){
			mNavigationDrawerFragment.openDrawer();
		}

	}

	public static Intent newInstagramProfileIntent(PackageManager pm, String url) {
		final Intent intent = new Intent(Intent.ACTION_VIEW);
		try {
			if (pm.getPackageInfo("com.instagram.android", 0) != null) {
				if (url.endsWith("/")) {
					url = url.substring(0, url.length() - 1);
				}
				final String username = url.substring(url.lastIndexOf("/") + 1);
				intent.setData(Uri.parse("http://instagram.com/_u/" + username));
				intent.setPackage("com.instagram.android");
				return intent;
			}
		} catch (PackageManager.NameNotFoundException ignored) {
		}
		intent.setData(Uri.parse(url));
		return intent;
	}

	public Uri getLocalBitmapUri(Bitmap bmp,String fileName) {
		Uri bmpUri = null;
		try {
			File file =  new File(Constants.ACTIVITY_CONTEXT.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName + System.currentTimeMillis() + ".png");
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 70, out);
			out.close();
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpUri;
	}


	@Override
	protected void onResume() {
		super.onResume();
		Constants.POPUPCONTEXT = this ;
		RadioHomePage.startTimerForScreens();
	}
	@Override
	public void onNavigationDrawerItemSelected(int position) {

	}
}