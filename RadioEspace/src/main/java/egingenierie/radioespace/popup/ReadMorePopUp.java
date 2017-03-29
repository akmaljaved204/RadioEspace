package egingenierie.radioespace.popup;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import egingenierie.radioespace.R;
import egingenierie.radioespace.RadioHomePage;
import egingenierie.radioespace.utils.Constants;


public class ReadMorePopUp {

    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private Dialog dialog = null;
    String alertTitle, alertLink;

    public ReadMorePopUp(Context context) {
        this.context = context;
        activity = (Activity) context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initializeDialoge();
    }

    public void showPopUp(String alertTitle, String alertLink) {
        this.alertTitle = alertTitle;
        this.alertLink = alertLink;
        if (dialog != null) {
            loadPopup();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            } else {
                dialog.show();
            }
        }
    }

    public void disMissPopup() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void loadPopup() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view = inflater.inflate(R.layout.read_more_popup, null,
                        false);
                Button btnClosePopUp = (Button) view.findViewById(R.id.btClosepopup);
                Button btnReadMore = (Button) view.findViewById(R.id.btnReadMore);
                TextView txtDetail = (TextView) view.findViewById(R.id.txtDetail);
                String title = alertTitle;
                txtDetail.setText(alertTitle);
                btnClosePopUp.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Constants.DONTTRPEATPOPUP = true;
                        dialog.dismiss();
                        RadioHomePage.neverShowPopUp();

                    }
                });
                btnReadMore.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (alertLink.startsWith("http")) {
                            String temp = alertLink;
                            String[] temp1 = temp.split("/");
                            int index = temp1.length - 2;
                            int id = 0;
                            try {
                                id = Integer.parseInt(temp1[index]);
                            } catch (NumberFormatException nfe) {
                                System.out.println("Could not parse " + nfe);
                            }
                            /*if (activity instanceof ArticleDetailForSpecificArticle) {
								((ArticleDetailForSpecificArticle)activity).getDetailForArticle(id);
							} else {*/
                            /*Intent intrent = new Intent(context, ArticleDetailForSpecificArticle.class);
                            intrent.putExtra("id", id);
                            intrent.putExtra("title", "Alerte");
                            context.startActivity(intrent);*/
                            //}
                        } else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(alertLink));
                            context.startActivity(browserIntent);
                        }
                        RadioHomePage.startTimerMinute();
                        Constants.DONTTRPEATPOPUP = true;
                        dialog.dismiss();

                    }
                });

                dialog.setContentView(view);
                dialog.show();
            }
        });
    }

    private void initializeDialoge() {
        dialog = new Dialog(context,
                android.R.style.Theme_Translucent_NoTitleBar);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationBottomToUp;
        dialog.setCancelable(true);
    }
}