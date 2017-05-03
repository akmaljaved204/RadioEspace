package egingenierie.radioespace.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import egingenierie.radioespace.R;
import egingenierie.radioespace.RadioHomePage;
import egingenierie.radioespace.model.Radio;
import egingenierie.radioespace.radiostreeming.RadioPlayer;
import egingenierie.radioespace.utils.Library;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class AdapterForRadio extends ArrayAdapter<Radio> {

    public Context context;
    private RadioHomePage radioHomePage;
    private Library library;
    private LayoutInflater inflater;

    public AdapterForRadio(Context context, int textViewResourceId,
                           List<Radio> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        library = new Library(context);
        radioHomePage = (RadioHomePage) context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        final Radio item = getItem(position);
        if (position == 0) {
            row = inflater.inflate(R.layout.radioplayingitemscreen, parent,false);
            ImageView imgStreem = (ImageView) row.findViewById(R.id.imgRadioDashboardImage);
            final ImageView btnPalyPause = (ImageView) row.findViewById(R.id.imagePayPause);
            TextView txtRadioDec = (TextView) row.findViewById(R.id.txtRadioDescription);
            TextView txtArtistName = (TextView) row.findViewById(R.id.txtArtistName);
            TextView txtSongTile = (TextView) row.findViewById(R.id.txtArtstDescrp);
            LinearLayout imgCurrentRadioFav = (LinearLayout) row.findViewById(R.id.llFavorite);
            ImageView imgBlure = (ImageView) row.findViewById(R.id.imageViewBlure);
            LinearLayout imgShareOnAll = (LinearLayout) row.findViewById(R.id.llShare);
            imgShareOnAll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    radioHomePage.shareRadioDetail();
                }
            });
            imgCurrentRadioFav.setOnClickListener(new OnFavoriteClick(position));
            /*if (item.isFavorite()) {
                imgCurrentRadioFav.setImageResource(R.drawable.star_full);
            } else {
                imgCurrentRadioFav.setImageResource(R.drawable.star_empty);
            }*/
            txtArtistName.setText(item.getArtistName());
            txtArtistName.setTypeface(library.robotoBold);
            txtSongTile.setText(item.getSongName());
            txtSongTile.setTypeface(library.robotoLight);

            //txtRadioDec.setText("Sur " + item.getTitle());
            txtRadioDec.setText(item.getTitle());
            if (item.isRadioPlaying()) {
                radioHomePage.lstForRadio.get(0).setRadioPlaying(true);
                btnPalyPause.setImageResource(R.drawable.radio_player_pause_btn);
            } else {
                radioHomePage.lstForRadio.get(0).setRadioPlaying(false);
                btnPalyPause.setImageResource(R.drawable.radio_player_play_btn);
            }

            btnPalyPause.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (radioHomePage.lstForRadio.get(0).isRadioPlaying()) {
                        radioHomePage.lstForRadio.get(0).setRadioPlaying(false);
                        RadioPlayer.getRadioPlayer().pauseRadio();
                        radioHomePage.refreshAdapter();
                        radioHomePage.mNavigationDrawerFragment.refreshMiniPlayer();
                    } else {
                        radioHomePage.lstForRadio.get(0).setRadioPlaying(true);
                        RadioPlayer.getRadioPlayer().playRadio();
                        radioHomePage.refreshAdapter();
                        radioHomePage.mNavigationDrawerFragment.refreshMiniPlayer();
                    }
                }
            });

            try {
                Picasso.with(context)
                        .load(item.getStreamImage())
                        .fit().centerInside()
                        .placeholder(getImageResource(item.getId()))
                        .into(imgStreem);

                Picasso.with(context)
                        .load(item.getStreamImage())
                        .fit().centerInside()
                        .transform(new BlurTransformation(context, 20, 1))
                        .placeholder(getImageResource(item.getId()))
                        .into(imgBlure);
            } catch (Exception ex) {
                System.out.print("");
            }

        } else {
            if (item.isRadioPlaying()) {
                row = inflater.inflate(R.layout.radio_items_playing, parent, false);
                ImageView imgRadioPicture = (ImageView) row.findViewById(R.id.imgRadioPicture);
                ImageView imgItemImage = (ImageView) row.findViewById(R.id.imgItemImage);
                Picasso.with(context)
                        .load(radioHomePage.mediaUrl + item.getFile())
                        .fit().centerInside().noFade()
                        .placeholder(getImageResource(item.getId()))
                        .error(getImageResource(item.getId()))
                        .into(imgRadioPicture);
                Picasso.with(context)
                        .load(item.getStreamImage())
                        .fit().centerInside()
                        .transform(new BlurTransformation(context, 30, 1))
                        .placeholder(getImageResource(item.getId()))
                        .into(imgItemImage);
            }else {
                row = inflater.inflate(R.layout.radioitemstoplay, parent, false);
                ImageView imgRadioPicture = (ImageView) row.findViewById(R.id.imgRadioPicture);
                TextView txtRadioName = (TextView) row.findViewById(R.id.txtRadioName);
                TextView txtRadioDescription = (TextView) row.findViewById(R.id.txtRadioDescription);
                ImageView imgItemImage = (ImageView) row.findViewById(R.id.imgItemImage);
                txtRadioName.setText(item.getArtistName());
                txtRadioName.setTypeface(library.robotoBold);
                txtRadioDescription.setText(item.getSongName());
                txtRadioDescription.setTypeface(library.robotoLight);
                if (item.getArtistName().equals("")) {
                    txtRadioName.setVisibility(View.GONE);
                }
                if (item.getSongName().equals("")) {
                    txtRadioDescription.setVisibility(View.GONE);
                }
                Picasso.with(context)
                        .load(radioHomePage.mediaUrl + item.getFile())
                        .fit().centerInside().noFade()
                        .placeholder(getImageResource(item.getId()))
                        .error(getImageResource(item.getId()))
                        .into(imgRadioPicture);
                Picasso.with(context)
                        .load(item.getStreamImage())
                        .fit().centerInside()
                        .transform(new BlurTransformation(context, 30, 1))
                        .placeholder(getImageResource(item.getId()))
                        .into(imgItemImage);
            }
        }

        return row;
    }

    private int getImageResource(int imagename) {
        try {
            String uri = "drawable/espace_" + imagename;
            int imageResource = context.getResources().getIdentifier(uri,
                    null, context.getPackageName());
            if (imageResource > 0) {
                return imageResource;
            } else {
                return R.drawable.icon_espace;
            }
        } catch (Exception e) {
            return R.drawable.icon_espace;
        }
    }

    public class OnFavoriteClick implements OnClickListener {
        private int position;

        public OnFavoriteClick(int lPosition) {
            position = lPosition;
        }

        @Override
        public void onClick(View v) {
            radioHomePage.OnFavoriteButtonClick(position);
        }
    }
}
