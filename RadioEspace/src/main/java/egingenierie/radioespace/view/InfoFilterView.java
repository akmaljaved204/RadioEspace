package egingenierie.radioespace.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import egingenierie.radioespace.R;

public class InfoFilterView extends LinearLayout {
	private LayoutInflater inflater;
	private TextView txtDayName;
	private ImageView rlSelector;
	private int counter;
	private String strText;
	public InfoFilterView(Context lContext, int conter, String strText) {
		super(lContext);
		counter=conter;
		this.strText=strText;
		inflater = (LayoutInflater) lContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initializeView();
	}
	private void initializeView() {
		View row = inflater.inflate(R.layout.info_filter_item, null);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
		p.weight = 1;
		setLayoutParams(p);
		txtDayName = (TextView) row.findViewById(R.id.txtjeu15);
		txtDayName.setTextColor(Color.parseColor("#99FFFFFF"));
		rlSelector = (ImageView) row.findViewById(R.id.rlSelectorForEmission1);
		txtDayName.setText(strText);
		if(counter==1){
			setSelected();
		}
		addView(row);
	}

	public void setSelected(){
		rlSelector.setBackgroundResource(R.drawable.info_filter_sel);
		txtDayName.setTypeface(null, Typeface.BOLD);
		invalidate();
	}	
	public void setUnSelected(){
		rlSelector.setBackgroundResource(R.drawable.emissions_arrow_clear);
		txtDayName.setTypeface(null, Typeface.NORMAL);
		invalidate();
	}	
	public int getSelectedFilter(){
		return counter;
	}
}
