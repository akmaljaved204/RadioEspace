package egingenierie.radioespace.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import egingenierie.radioespace.EmissionActivity;
import egingenierie.radioespace.R;
import egingenierie.radioespace.model.EmissionModel;

public class AdapterForEmissionList extends ArrayAdapter<EmissionModel> {
	private LayoutInflater inflater;
	private int textViewResourceId;
	private EmissionActivity emissionActivity;
	private Context context ;
	public AdapterForEmissionList(Context context, int textViewResourceId,
								  List<EmissionModel> objects) {
		super(context, textViewResourceId, objects);
		this.context = context ;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.textViewResourceId = textViewResourceId;
		emissionActivity=(EmissionActivity)context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row;
		final EmissionModel item = getItem(position);
		row = inflater.inflate(textViewResourceId, parent, false);
		RelativeLayout selectorForDetail = (RelativeLayout) row
				.findViewById(R.id.center);
		TextView txtNameOfEmission = (TextView) row
				.findViewById(R.id.txtNameOfEmission);
		TextView txtSubtitle = (TextView) row.findViewById(R.id.txtSubtitle);
		TextView txtDateTime = (TextView) row.findViewById(R.id.txtDateTime);
		final TextView txtDetail = (TextView) row.findViewById(R.id.txtDetail);
		ImageView imgForEmission = (ImageView) row
				.findViewById(R.id.imgForEmissionRadio);
		final ImageView imgSwitcher = (ImageView) row
				.findViewById(R.id.imageView1);
		if(item.isOpenOrNot()){
			imgSwitcher.setImageResource(R.drawable.emissions_dropup);
			txtDetail.setVisibility(View.VISIBLE);
			/*txtNameOfEmission.setVisibility(View.VISIBLE);
			txtDateTime.setVisibility(View.VISIBLE);*/
		}else {
			imgSwitcher.setImageResource(R.drawable.emissions_dropdown);
			txtDetail.setVisibility(View.GONE);
			/*txtNameOfEmission.setVisibility(View.GONE);
			txtDateTime.setVisibility(View.GONE);*/
		}
		selectorForDetail.setOnClickListener(new CheckListener(position));
		txtNameOfEmission.setText(item.getTitle());
		txtSubtitle.setText(item.getSchedual());
		txtDateTime.setText(item.getSubtitle());
		txtDetail.setText(Html.fromHtml(item.getSubtitle()));
		final ProgressBar pgBar = (ProgressBar) row.findViewById(R.id.progressBar1);
		try {
			if(!(emissionActivity.media_url + item.getPicture()).equals("")){
				Picasso.with(context)
						.load(emissionActivity.media_url + item.getPicture())
						.placeholder(R.drawable.transparent_image)
						.into(imgForEmission, new Callback() {
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
	
	private class CheckListener implements OnClickListener{
		private int position=0;
		public CheckListener(int lPosition){
			position=lPosition;
		}
		@Override
		public void onClick(View v) {
			emissionActivity.notifyAdapter(position);
		}
	}	
}
