package egingenierie.radioespace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import egingenierie.radioespace.R;
import egingenierie.radioespace.model.PodcastCategory;

public class AdapterForPodcastCategory extends ArrayAdapter<PodcastCategory>{

	private LayoutInflater inflater;
	private int textViewResourceId;
	private Context context;
	private String mediaURL="";
	private List<PodcastCategory> tempObjects;
	public AdapterForPodcastCategory(Context lContext, int textViewResourceId, List<PodcastCategory> objects,String mediaURL) {
		super(lContext, textViewResourceId, objects);
		context=lContext;
		tempObjects = objects ;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.textViewResourceId=textViewResourceId;
		this.mediaURL=mediaURL;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row;
		PodcastCategory item = getItem(position);
		row = inflater.inflate(textViewResourceId, parent, false);
		ImageView imgPodcastPicture = (ImageView)row.findViewById(R.id.imgRadioPicture);
		TextView txtPodcastName = (TextView)row.findViewById(R.id.txtRadioName);
		TextView txtNumberOfPodcast = (TextView)row.findViewById(R.id.txtNumberOfPodcast);
		final ProgressBar pgBar = (ProgressBar)row.findViewById(R.id.progressBar1);
		if((tempObjects.size()-1)==position){
			RelativeLayout rlBorder = (RelativeLayout) row.findViewById(R.id.rlBorder);
			rlBorder.setVisibility(View.VISIBLE);
		}

		txtPodcastName.setText(item.getTitle());
		if(item.getCount()<2){
			txtNumberOfPodcast.setText("("+item.getCount()+" podcast)");
		}else{
			txtNumberOfPodcast.setText("("+item.getCount()+" podcasts)");
		}

		try {
			if(!(mediaURL+item.getPicture()).equals("")) {
				Picasso.with(context)
						.load(mediaURL+item.getPicture())
						.placeholder(R.drawable.podcast_default)
						.fit()
						.centerInside()
						.into(imgPodcastPicture, new Callback() {
							@Override
							public void onSuccess() {
								pgBar.setVisibility(View.GONE);
							}

							@Override
							public void onError() {
								pgBar.setVisibility(View.GONE);
							}
						});
			}else {
				pgBar.setVisibility(View.GONE);
			}
		}catch (Exception ex){
			System.out.print("");
		}

		return row;
	}
}