package egingenierie.radioespace.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import egingenierie.radioespace.R;

/**
 * Created by AkmalJaved on 3/25/2017.
 */
public class AppRater {

    private String APP_PACKAGE_NAME = "";
    private String APP_NAME = "";
    private int LAUNCHES_UNTIL_PROMPT = 3;
    private Context context;

    public AppRater(Context context){
        this.context=context;
        APP_PACKAGE_NAME=context.getPackageName();
        APP_NAME=context.getResources().getString(R.string.app_name);
        SharedPreferences prefs = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            showRateDialog(editor);
        }
        editor.commit();
    }

    private void showRateDialog(final SharedPreferences.Editor editor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(APP_NAME);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("Si vous aimez cette application, laissez-nous un petit commentaire et une bonne note sur le store. Merci de votre support!");
        builder.setPositiveButton("Noter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Plus tard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                editor.putLong("launch_count", 0);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Jamais", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
