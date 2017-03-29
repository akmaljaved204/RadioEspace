package egingenierie.radioespace.adapter;

import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import egingenierie.radioespace.R;


/**
 * Created by Ali Abbas on 6/28/2016.
 */
public class ShareIntentListAdapter extends ArrayAdapter {

    private final Activity context;
    Object[] items;
    List<String> activityNames;

    public ShareIntentListAdapter(Activity context, Object[] items, List<String> actvNames) {

        super(context, R.layout.social_share, items);
        this.context = context;
        this.items = items;
        this.activityNames = actvNames;
    }// end HomeListViewPrototype

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.social_share, null, true);
        String name = (String) activityNames.get(position);
        // set share name
        TextView shareName = (TextView) rowView.findViewById(R.id.shareName);

        // Set share image
        ImageView imageShare = (ImageView) rowView.findViewById(R.id.shareImage);
        ResolveInfo info = (ResolveInfo) items[position];
        // set native name of App to share
        String nasame = ((ResolveInfo) items[position]).activityInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
        shareName.setText("" + name);
        // share native image of the App to share
        imageShare.setImageDrawable(((ResolveInfo) items[position]).activityInfo.applicationInfo.loadIcon(context.getPackageManager()));
        return rowView;
    }// end getView
}