package egingenierie.radioespace.adapter;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

import egingenierie.radioespace.InfosScreen;
import egingenierie.radioespace.R;
import egingenierie.radioespace.model.NewsItems;
import egingenierie.radioespace.model.Weather;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class AdapterForInfos extends ArrayAdapter<NewsItems> {
	int textViewResourceid;
	LayoutInflater inflater;
	Context context;
	private List<NewsItems> newsItemsOjects = new ArrayList<>();
	private InfosScreen infosScreen ;
	public AdapterForInfos(Context context, int textViewResourceId, List<NewsItems> objects) {

		super(context, textViewResourceId, objects);
		this.context = context;
		newsItemsOjects=objects;
		textViewResourceid = textViewResourceId;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		newsItemsOjects = objects ;
		infosScreen = (InfosScreen)context;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = null;
		final NewsItems item = getItem(position);
		if(infosScreen.selectedCtgry==2){
			row = inflater.inflate(R.layout.trafic_items, parent, false);
			TextView txtHeader = (TextView) row.findViewById(R.id.txtHeader);
			TextView txtDetail = (TextView) row.findViewById(R.id.txtDetail);
			txtHeader.setText(item.getStart_date());
			txtDetail.setText(Html.fromHtml(item.getText()));
		}else if(infosScreen.selectedCtgry==4){
			Weather weather=item.getWeather();
			row = inflater.inflate(R.layout.weather_item, parent, false);
			TextView txtWeatherDate= (TextView) row.findViewById(R.id.txtDayName);
			TextView txtDayName= (TextView) row.findViewById(R.id.txtDayDate);
			TextView txtMorningWeather= (TextView) row.findViewById(R.id.txtDegreeValue1);
			TextView txtNoonWeather= (TextView) row.findViewById(R.id.txtDegreeValue2);
			TextView txtAfterNoonWeather= (TextView) row.findViewById(R.id.txtDegreeValue3);
			TextView txtEveningWeather= (TextView) row.findViewById(R.id.txtDegreeValue4);
			ImageView imgMorning= (ImageView) row.findViewById(R.id.imgWeatherImage1);
			ImageView imgNoon= (ImageView) row.findViewById(R.id.imgWeatherImage2);
			ImageView imgAfterNoon= (ImageView) row.findViewById(R.id.imgWeatherImage3);
			ImageView imgEvenin= (ImageView) row.findViewById(R.id.imgWeatherImage4);
			final ProgressBar pbMorning = (ProgressBar) row.findViewById(R.id.progressLoader1);
			final ProgressBar pbNoon = (ProgressBar) row.findViewById(R.id.progressLoader2);
			final ProgressBar pbAfterNoon = (ProgressBar) row.findViewById(R.id.progressLoader3);
			final ProgressBar pbEvening = (ProgressBar) row.findViewById(R.id.progressLoader4);
			txtDayName.setText(weather.getTemperatureDate());
			txtWeatherDate.setText("("+weather.getDayName()+")");
			txtMorningWeather.setText(weather.getMorningWeather()+"\n"+weather.getMorningTemperature()+"\u2103");
			txtNoonWeather.setText(weather.getNoonWeather()+"\n"+weather.getNoonTemperature()+"\u2103");
			txtAfterNoonWeather.setText(weather.getAfterNoonWeather()+"\n"+weather.getAfterNoonTemperature()+"\u2103");
			txtEveningWeather.setText(weather.getEveningWeather()+"\n"+weather.getEveningTemperature()+"\u2103");
			Picasso.with(context).load(weather.getMediaUrl()+weather.getMorningWeatherImage())
					.placeholder(R.drawable.weather_default)
					.into(imgMorning, new Callback() {

						@Override
						public void onSuccess() {
							pbMorning.setVisibility(View.GONE);
						}
						@Override
						public void onError() {
							pbMorning.setVisibility(View.GONE);
						}
					});
			Picasso.with(context).load(weather.getMediaUrl()+weather.getNoonWeatherImage())
					.placeholder(R.drawable.weather_default)
					.into(imgNoon, new Callback() {

						@Override
						public void onSuccess() {
							pbNoon.setVisibility(View.GONE);
						}
						@Override
						public void onError() {
							pbNoon.setVisibility(View.GONE);
						}
					});
			Picasso.with(context).load(weather.getMediaUrl()+weather.getAfterNoonWeatherImage())
					.placeholder(R.drawable.weather_default)
					.into(imgAfterNoon, new Callback() {

						@Override
						public void onSuccess() {
							pbAfterNoon.setVisibility(View.GONE);
						}
						@Override
						public void onError() {
							pbAfterNoon.setVisibility(View.GONE);
						}
					});
			Picasso.with(context).load(weather.getMediaUrl()+weather.getEveningWeatherImage())
					.placeholder(R.drawable.weather_default)
					.into(imgEvenin, new Callback() {

						@Override
						public void onSuccess() {
							pbEvening.setVisibility(View.GONE);
						}
						@Override
						public void onError() {
							pbEvening.setVisibility(View.GONE);
						}
					});

		}else{
			row = inflater.inflate(textViewResourceid, parent, false);
			TextView txtheader = (TextView) row.findViewById(R.id.a_la_itemheader);
			TextView txtdetail = (TextView) row.findViewById(R.id.txtItemDetail);
			ImageView imgArticle = (ImageView) row.findViewById(R.id.a_la_itemimage);
			ImageView imgShare = (ImageView) row.findViewById(R.id.imgShareIcon);
			ImageView imgBackground = (ImageView) row.findViewById(R.id.imgBackgroundImage);
			TextView txtLoadMore = (TextView) row.findViewById(R.id.txtLoadMore);
			RelativeLayout rlMoreDetail= (RelativeLayout) row.findViewById(R.id.rlMoreDetail);
			final ProgressBar pgBar = (ProgressBar) row.findViewById(R.id.progressBar1);

			if(newsItemsOjects.size() - 1  == position){
				if(infosScreen.isHaveMore) {
					txtLoadMore.setVisibility(View.VISIBLE);
				}else{
					txtLoadMore.setVisibility(View.GONE);
				}
			}
			txtheader.setText(item.getTitle());
			String text=item.getText().replaceAll("color:black","color:white");
			text=text.replaceAll("color=\"#000000\"","color=\"#FFFFFF\"");
			txtdetail.setText(Html.fromHtml(text));
			if(item.isExpanded()){
				txtdetail.setVisibility(View.VISIBLE);
				imgShare.setVisibility(View.VISIBLE);
				rlMoreDetail.setVisibility(View.GONE);
				imgBackground.setImageResource(R.drawable.background_article);
			}else{
				txtdetail.setVisibility(View.GONE);
				imgShare.setVisibility(View.GONE);
				rlMoreDetail.setVisibility(View.VISIBLE);
				Picasso.with(context)
						.load(item.getMediaUrl()+item.getPicture())
						.fit().centerInside()
						.transform(new BlurTransformation(context, 20, 1))
						.placeholder(R.drawable.infos_background_default)
						.into(imgBackground);
			}
			imgShare.setOnClickListener(new OnShareClick(position));
			txtLoadMore.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					infosScreen.loadMoreFApi();
				}
			});
			try {
				Picasso.with(context).load(item.getMediaUrl()+item.getPicture())
						.placeholder(R.drawable.transparent_image)
						.into(imgArticle, new Callback() {

							@Override
							public void onSuccess() {
								pgBar.setVisibility(View.GONE);
							}
							@Override
							public void onError() {
								pgBar.setVisibility(View.GONE);
							}
						});
			} catch (Exception e) {
				System.out.print("");
				pgBar.setVisibility(View.GONE);
			}
		}

		return row;
	}

	private  class OnShareClick implements OnClickListener{
		private int position=0;
		public OnShareClick(int positionL){
			position=positionL;
		}

		@Override
		public void onClick(View v) {
			infosScreen.shareNews(position);
		}
	}

}