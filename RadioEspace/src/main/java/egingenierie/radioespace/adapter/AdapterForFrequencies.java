package egingenierie.radioespace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import egingenierie.radioespace.R;
import egingenierie.radioespace.model.Frequences;

public class AdapterForFrequencies extends ArrayAdapter<Frequences>{
	private LayoutInflater inflater;
	private int textViewResourceId;

	public AdapterForFrequencies(Context context, int textViewResourceId, List<Frequences> objects) {
		super(context, textViewResourceId, objects);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.textViewResourceId=textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row;
		Frequences item = getItem(position);
		row = inflater.inflate(textViewResourceId, parent, false);
		TextView txtRadioName = (TextView)row.findViewById(R.id.txtCityName);
		TextView txtDepartment = (TextView)row.findViewById(R.id.txtDepartment);
		TextView txtFrequence = (TextView)row.findViewById(R.id.txtFrequence);
		txtFrequence.setText(""+item.getFrequence());
		txtRadioName.setText(item.getVille());
		txtDepartment.setText(""+String.format("%02d", item.getDepartement_id()));
		return row;
	}
}