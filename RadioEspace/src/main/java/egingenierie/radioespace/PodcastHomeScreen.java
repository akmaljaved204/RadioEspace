package egingenierie.radioespace;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import egingenierie.radioespace.adapter.AdapterForPodcast;
import egingenierie.radioespace.model.Podcast;
import egingenierie.radioespace.model.PodcastCategory;
import egingenierie.radioespace.network.APIDataLoaderPodcast;
import egingenierie.radioespace.radiostreeming.RadioPlayer;
import egingenierie.radioespace.utils.Constants;
import egingenierie.radioespace.utils.Library;
import egingenierie.radioespace.utils.SocialSharing;
import fr.mediametrie.mesure.library.android.Estat;
import fr.mediametrie.mesure.library.android.EstatAudienceTagger;

public class PodcastHomeScreen extends Activity implements OnClickListener {

	private ListView lvPodcast;
	private ArrayList<Podcast> listPodcast = new ArrayList<Podcast>();
	private Button btnOpenMenu;
	private Library library;
	private PodcastCategory newsItem;
	private String mediaUrlCtgry = "" ;
	private ImageView imgCtgryCntnr ;
	private ProgressBar pgBar ;
	private int pageNumber=0;
	private Handler handler = new Handler();
	private RadioHomePage radioHomePage;
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.podcast_home_screen);
		Estat.init(this);
		//Création du tagger médiamétrie
		EstatAudienceTagger audienceTagger = Estat.getAudience("202002214620");
		//envoie d'un hit signalant que la page a été ouverte
		audienceTagger.sendHit("Podcast");
		radioHomePage=RadioHomePage.getRadioHomePage();
		library = new Library(this);
		newsItem=(PodcastCategory) getIntent().getExtras().getSerializable("podcastDetail");
		mediaUrlCtgry = getIntent().getExtras().getString("mediaUrl");
		btnOpenMenu = (Button) findViewById(R.id.btnOpenDrawer);
		lvPodcast= (ListView) findViewById(R.id.lstPodcast);
		imgCtgryCntnr = (ImageView) findViewById(R.id.imgCategory);
		//pgBar = (ProgressBar) findViewById(R.id.progressBar);
		btnOpenMenu.setOnClickListener(this);
		if(library.haveNetworkConnection()){
			String apiURL="son/podcast";
			if(newsItem.getShowId()>0){
				apiURL="podcast_by_category/"+newsItem.getShowId()+"/"+pageNumber;
			}
			new APIDataLoaderPodcast(this,apiURL);
		}else {
			Toast.makeText(this, "No Internet Connection ", Toast.LENGTH_LONG).show();
		}
		RadioHomePage.getRadioHomePage().tracker.setScreenName("MFMRadio Pocast Screen ");
	    RadioHomePage.getRadioHomePage().tracker.send(new HitBuilders.ScreenViewBuilder().build());
	  try {
			if(!(mediaUrlCtgry+newsItem.getPicture()).equals("")) {
				Picasso.with(this)
						.load(mediaUrlCtgry+newsItem.getPicture())
						.placeholder(R.drawable.podcast_default)
						.fit()
						.centerInside()
						.into(imgCtgryCntnr, new Callback() {
							@Override
							public void onSuccess() {
								//pgBar.setVisibility(View.GONE);
							}

							@Override
							public void onError() {
								//pgBar.setVisibility(View.GONE);
							}
						});
			}else {
				//pgBar.setVisibility(View.GONE);
			}
		}catch (Exception ex){
			System.out.print("");
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
			finish();
		}
	}

	
	@Override
	protected void onDestroy() {
		if(playingIndex!=-1){
			listPodcast.get(playingIndex).setPlaying(false);
		}
		stopMediaPlayer();
		super.onDestroy();
	}



	public boolean isHaveMore=false;
	public void onDataReceive(ArrayList<Podcast> podcasts) {
		try {
			if(podcasts.size()>19){
				isHaveMore=true;
			}else{
				isHaveMore=false;
			}
			if(pageNumber==0){
				listPodcast.clear();
				listPodcast=podcasts;
				lvPodcast.setAdapter(new AdapterForPodcast(this,R.layout.podcast_detail_item, listPodcast));
			}else{
				for(int i=0;i<podcasts.size();i++){
					listPodcast.add(podcasts.get(i));
				}
				onDataSetChanged();
			}
		} catch (Exception e) {
		}
	}
	public void loadMoreFromApi() {
		if (library.haveNetworkConnection()) {
			pageNumber++;
			new APIDataLoaderPodcast(this,"podcast_by_category/"+newsItem.getShowId()+"/"+pageNumber);
		} else {
			Toast.makeText(this, "No Internet Available", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	protected void onResume() {
		Constants.POPUPCONTEXT = this ;
		RadioHomePage.startTimerForScreens();
		super.onResume();
	}
	public void sharePodcats(int position){
		Constants.ACTIVITY_CONTEXT = this ;
		String strToShare = getResources().getString(R.string.radioTitle)+"\n"+listPodcast.get(position).getTitle()+"\n"+getResources().getString(R.string.radioDetail)+"\nhttp://www.radioespace.com/"+listPodcast.get(position).getType()+"/view/"+listPodcast.get(position).getId()+"/"+listPodcast.get(position).getSlug();
		SocialSharing.getSocialSharing().sharingPodcastOnMedia(PodcastHomeScreen.this,strToShare);
	}

	private MediaPlayer mediaPlayer;
	private int playingIndex=-1;
	public void playPausePlayer(int position){
		if (radioHomePage.lstForRadio.get(0).isRadioPlaying()) {
			radioHomePage.lstForRadio.get(0).setRadioPlaying(false);
			RadioPlayer.getRadioPlayer().pauseRadio();
			radioHomePage.refreshAdapter();
		}
		if(!listPodcast.get(position).isLoading()){
			if(playingIndex==-1){
				listPodcast.get(position).setPlaying(true);
				createPlayer(position);
			}else if(playingIndex==position){
				if(listPodcast.get(position).isPlaying()){
					listPodcast.get(position).setPlaying(false);
					mediaPlayer.pause();
				}else{
					listPodcast.get(position).setPlaying(true);
					mediaPlayer.start();
					progressSetter();
				}
			}else{
				listPodcast.get(playingIndex).setPlaying(false);
				listPodcast.get(playingIndex).setLoading(false);
				listPodcast.get(playingIndex).setCurrentProgress(0);
				listPodcast.get(playingIndex).setCurrentTime("00:00");
				stopMediaPlayer();
				listPodcast.get(position).setPlaying(true);
				createPlayer(position);
			}
			playingIndex=position;
			onDataSetChanged();
		}
	}
	private void createPlayer(final int position){
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				handler.post(new Runnable() {
					public void run() {
						Toast.makeText(PodcastHomeScreen.this,"Some thing wrong with audio file",Toast.LENGTH_LONG).show();
					}
				});
				return false;
			}
		});
		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				try {
					mediaPlayer.start();
					progressSetter();
					listPodcast.get(position).setTotalProgress(mp.getDuration() / 1000);
					int temp = (int) (mp.getDuration() / 1000);
					DecimalFormat formatter = new DecimalFormat("00");
					listPodcast.get(position).setTotalTime(formatter.format(temp / 60) + ":" + formatter.format(temp % 60));
					listPodcast.get(position).setLoading(false);
					onDataSetChanged();
				} catch (Exception e) {
				}
			}
		});
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				listPodcast.get(position).setPlaying(false);
				onDataSetChanged();
			}
		});
		try {
			mediaPlayer.setDataSource(PodcastHomeScreen.this, Uri.parse(listPodcast.get(position).getAudioFile()));
			mediaPlayer.prepareAsync();
			listPodcast.get(position).setLoading(true);
		}catch (Exception ex){}
	}
	private void stopMediaPlayer(){
		if(mediaPlayer!=null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;
		}
	}

	public void pauseMediaPlayer(){
		if(mediaPlayer!=null){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.pause();
				listPodcast.get(playingIndex).setPlaying(false);
				onDataSetChanged();
			}
		}
	}

	private void onDataSetChanged(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((AdapterForPodcast)lvPodcast.getAdapter()).notifyDataSetChanged();
			}
		});
	}

	private void progressSetter() {
		if (mediaPlayer.isPlaying()) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					while (listPodcast.get(playingIndex).isPlaying()) {
						try {
							int currentPosition = mediaPlayer.getCurrentPosition();
							listPodcast.get(playingIndex).setCurrentProgress(currentPosition / 1000);
							int temp = (int) (mediaPlayer.getCurrentPosition() / 1000);
							DecimalFormat formatter = new DecimalFormat("00");
							listPodcast.get(playingIndex).setCurrentTime(formatter.format(temp / 60)+ ":"+ formatter.format(temp % 60));
							onDataSetChanged();
						} catch (Exception e) {
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			thread.start();
		}
	}

	
}