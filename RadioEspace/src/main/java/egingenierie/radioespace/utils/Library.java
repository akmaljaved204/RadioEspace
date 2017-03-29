package egingenierie.radioespace.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Library {

    private Context context;
    private Activity activity;
    private ProgressDialog progressDialog = null;
    public Typeface robotoBold, robotoLight;

    public Library(Context context) {
        this.context = context;
        activity = (Activity) context;
        robotoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
        robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

    }

    public void showLoading() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                progressDialog = ProgressDialog.show(context, "", "Loading...",
                        true, true);
                progressDialog.setCancelable(false);
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        });
    }

    public void showLoading(final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                progressDialog = ProgressDialog.show(context, "", message
                        + "...", true, true);
                progressDialog.setCancelable(false);
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        });
    }

    public void hideLoading() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                }

            }
        });
    }

    public void messageBox(final String massage) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
                alertbox.setTitle("ODS");
                alertbox.setMessage(massage);
                alertbox.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                alertbox.show();
            }
        });
    }

    public void messageBoxFinish(final String massage) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
                alertbox.setTitle("ODS");
                alertbox.setMessage(massage);
                alertbox.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                activity.finish();
                            }
                        });

                alertbox.show();
            }
        });
    }

    public boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    return haveConnectedWifi;
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    return haveConnectedMobile;
                }
        }
        return false;
    }


}
