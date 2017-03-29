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

    public AdapterForRadio(Context context, int textViewResourceId,
                           List<Radio> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        library = new Library(context);
        radioHomePage = (RadioHomePage) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        final Radio item = getItem(position);
        if (position == 0) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.radioplayingitemscreen, parent,
                    false);
            ImageView imgStreem = (ImageView) row
                    .findViewById(R.id.imgRadioDashboardImage);
            final ImageView btnPalyPause = (ImageView) row
                    .findViewById(R.id.imagePayPause);
            TextView txtRadioDec = (TextView) row
                    .findViewById(R.id.txtRadioDescription);
            TextView txtArtistName = (TextView) row
                    .findViewById(R.id.txtArtistName);
            TextView txtSongTile = (TextView) row
                    .findViewById(R.id.txtArtstDescrp);
            ImageView imgCurrentRadioFav = (ImageView) row.findViewById(R.id.imgCurrentRadioFav);
            ImageView imgBlure = (ImageView) row.findViewById(R.id.imageViewBlure);
            /*RelativeLayout imgShareOnAll = (RelativeLayout) row.findViewById(R.id.rlShareButton);
            imgShareOnAll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    radioHomePage.shareRadioDetail();
                }
            });*/
            imgCurrentRadioFav.setOnClickListener(new OnFavoriteClick(position));
           /* if (item.isFavorite()) {
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
                       // radioHomePage.viewMiniPlayer.refreshMiniPlayer();
                    } else {
                        radioHomePage.lstForRadio.get(0).setRadioPlaying(true);
                        RadioPlayer.getRadioPlayer().playRadio();
                        radioHomePage.refreshAdapter();
                       // radioHomePage.viewMiniPlayer.refreshMiniPlayer();
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
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.radioitemstoplay, parent, false);
            RelativeLayout lRelativeLayout = (RelativeLayout) row.findViewById(R.id.relativeMain);
            ImageView imgRadioPicture = (ImageView) row
                    .findViewById(R.id.imgRadioPicture);
            TextView txtRadioName = (TextView) row
                    .findViewById(R.id.txtRadioName);
            TextView txtRadioDescription = (TextView) row
                    .findViewById(R.id.txtRadioDescription);
            ImageView imgRadioFvrt = (ImageView) row
                    .findViewById(R.id.imgRadioFvrt);

            if (item.isFavorite()) {
                imgRadioFvrt.setBackgroundResource(R.drawable.list_fav_icon);
            } else {
                imgRadioFvrt.setBackgroundResource(R.drawable.list_fav_icon);
            }
            imgRadioFvrt.setOnClickListener(new OnFavoriteClick(position));

            txtRadioName.setText(item.getTitle());
            txtRadioName.setTypeface(library.robotoBold);
            String temp = "";
            if (!item.getArtistName().equals("")) {
                temp = item.getArtistName();
            }
            if (!item.getSongName().equals("")) {
                if (!temp.equals("")) {
                    temp = temp + " - " + item.getSongName();
                } else {
                    temp = item.getSongName();
                }
            }
            if (item.isRadioPlaying()) {
                lRelativeLayout.setBackgroundColor(Color.WHITE);
            } else {
                lRelativeLayout.setBackgroundColor(Color.parseColor("#f6f5f5"));
            }
            txtRadioDescription.setText(temp);
            txtRadioDescription.setTypeface(library.robotoLight);
            Picasso.with(context)
                    .load(radioHomePage.mediaUrl + item.getFile())
                    .fit().centerInside().noFade()
                    .placeholder(getImageResource(item.getId()))
                    .error(getImageResource(item.getId()))
                    .into(imgRadioPicture);
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
