package egingenierie.radioespace.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import egingenierie.radioespace.R;

public class EmissionDayView extends LinearLayout {
	private LayoutInflater inflator;
	private TextView txtDayName;
	private ImageView rlSelector;
	private int counter;
	private String objectDate;
	private SimpleDateFormat formater;
	public EmissionDayView(Context lContext, int conter) {
		super(lContext);
		counter=conter;
		inflator = (LayoutInflater) lContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		formater = new SimpleDateFormat("yyyy-MM-dd");
		initializeView();
	}
	private void initializeView() {
		View row = inflator.inflate(R.layout.emission_days_item, null);
		txtDayName = (TextView) row.findViewById(R.id.txtjeu15);
		rlSelector = (ImageView) row.findViewById(R.id.rlSelectorForEmission1);
			if(counter==0){
				Calendar cal = Calendar.getInstance();
				objectDate=cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.FRANCE);
				//formater.format(cal.getTime());
				txtDayName.setText("Aujourd'hui");
			}else{
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, counter);
				//objectDate=formater.format(cal.getTime());
				objectDate=cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.FRANCE);
				String temp=cal.getTime().toString();
				if(objectDate.equals("lundi")){
					txtDayName.setText("Lun "+" "+temp.substring(8, 10));
				}else if(objectDate.equals("mardi")){
					txtDayName.setText("Mar "+" "+temp.substring(8, 10));
				}else if(objectDate.equals("mercredi")){
					txtDayName.setText("Mer "+" "+temp.substring(8, 10));
				}else if(objectDate.equals("jeudi")){
					txtDayName.setText("Jeu "+" "+temp.substring(8, 10));
				}else if(objectDate.equals("vendredi")){
					txtDayName.setText("Ven "+" "+temp.substring(8, 10));
				}else if(objectDate.equals("samedi")){
					txtDayName.setText("Sam "+" "+temp.substring(8, 10));
				}else if(objectDate.equals("dimanche")){
					txtDayName.setText("Dim "+" "+temp.substring(8, 10));
				}
			}
			if(counter==0){
				setSelected();
			}
		addView(row);
	}

	public void setSelected(){
		rlSelector.setBackgroundResource(R.drawable.emissions_arrow_icon);
		txtDayName.setTextColor(Color.parseColor("#FFFFFF"));
		txtDayName.setTypeface(null, Typeface.BOLD);
		invalidate();
	}	
	public void setUnSelected(){
		rlSelector.setBackgroundResource(R.drawable.emissions_arrow_clear);
		txtDayName.setTextColor(Color.parseColor("#FFFFFF"));
		txtDayName.setTypeface(null, Typeface.NORMAL);
		invalidate();
	}	
	public String setSelectedDate(){
		return objectDate;
	}
}
