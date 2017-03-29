package egingenierie.radioespace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import egingenierie.radioespace.network.APiForParameters;
import egingenierie.radioespace.utils.Constants;

public class SplashScreen extends Activity {
    private long SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Constants.APP_CONTEXT = getApplicationContext();
        Constants.ACTIVITY_CONTEXT = this;
        TextView versionName = (TextView) findViewById(R.id.version);
        versionName.setText("Version : " + BuildConfig.VERSION_NAME);
        try {
            new APiForParameters(this, "params");
        } catch (Exception ex) {
            System.out.print("");
        }
    }

    public void onDataReceive() {
        Intent intent = new Intent(SplashScreen.this, RadioHomePage.class);
        intent.putExtra("From", "Radio");
        startActivity(intent);
        finish();
    }

    private void splashDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(SplashScreen.this, RadioHomePage.class);
                    intent.putExtra("From", "Radio");
                    startActivity(intent);
                   /* int hasWriteStoragePermission = ContextCompat.checkSelfPermission(SplashScreen.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SplashScreen.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }*/
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
