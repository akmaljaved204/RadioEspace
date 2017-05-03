package egingenierie.radioespace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import egingenierie.radioespace.PodcastHomeScreen;
import egingenierie.radioespace.R;
import egingenierie.radioespace.model.Podcast;

public class AdapterForPodcast extends ArrayAdapter<Podcast>{

	private LayoutInflater inflater;
	private int textViewResourceId;
	private Context context;
	private List<Podcast> tempObjects;
	private PodcastHomeScreen podcastHomeScreen;
	public AdapterForPodcast(Context lContext, int textViewResourceId, List<Podcast> objects) {
		super(lContext, textViewResourceId, objects);
		context=lContext;
		podcastHomeScreen=(PodcastHomeScreen)lContext;
		tempObjects = objects ;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.textViewResourceId=textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row;
		Podcast item = getItem(position);
		row = inflater.inflate(textViewResourceId, parent, false);
		TextView txtTitle=(TextView)row.findViewById(R.id.txtTitle);
		TextView txtSubTitle=(TextView)row.findViewById(R.id.txtName);
		TextView txtCurrentTime=(TextView)row.findViewById(R.id.textView1);
		TextView txtTotalTime=(TextView)row.findViewById(R.id.textView2);
		TextView txtLoadMore= (TextView) row.findViewById(R.id.txtLoadMore);
		ImageView imageViewShareIcon=(ImageView) row.findViewById(R.id.imageViewShareIcon);
		ImageView imgPlayPause=(ImageView) row.findViewById(R.id.btnPlaypauseOnMediaPlayer);
		ProgressBar prgLoader=(ProgressBar)row.findViewById(R.id.pgrsForPlayerStart);
		ProgressBar prgBar=(ProgressBar)row.findViewById(R.id.progressBar1);
		imgPlayPause.setOnClickListener(new OnClickPlayPause(position));
		imageViewShareIcon.setOnClickListener(new OnClickShare(position));
		prgBar.setMax(item.getTotalProgress());
		prgBar.setProgress(item.getCurrentProgress());
		txtTitle.setText(item.getTitle());
		txtSubTitle.setText(item.getSubtitle());
		txtCurrentTime.setText(item.getCurrentTime());
		txtTotalTime.setText(item.getTotalTime());
		if(tempObjects.size() - 1  == position){
			if(podcastHomeScreen.isHaveMore) {
				txtLoadMore.setVisibility(View.VISIBLE);
			}else{
				txtLoadMore.setVisibility(View.GONE);
			}
		}
		if(item.isLoading()){
			prgLoader.setVisibility(View.VISIBLE);
		}else{
			prgLoader.setVisibility(View.GONE);
		}
		if(item.isPlaying()){
			imgPlayPause.setBackgroundResource(R.drawable.streaming_pause);
		}else{
			imgPlayPause.setBackgroundResource(R.drawable.streaming_play);
		}
		txtLoadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				podcastHomeScreen.loadMoreFromApi();
			}
		});
		return row;
	}

	private class OnClickShare implements View.OnClickListener{
		private int position;
		public OnClickShare(int positionL){
			position=positionL;
		}

		@Override
		public void onClick(View v) {
			podcastHomeScreen.sharePodcats(position);
		}
	}
	private class OnClickPlayPause implements View.OnClickListener{
		private int position;
		public OnClickPlayPause(int positionL){
			position=positionL;
		}

		@Override
		public void onClick(View v) {
			podcastHomeScreen.playPausePlayer(position);
		}
	}
}