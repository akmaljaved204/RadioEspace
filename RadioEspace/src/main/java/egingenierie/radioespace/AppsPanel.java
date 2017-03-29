package egingenierie.radioespace;

import android.util.Log;

import com.appspanel.AppsPanelApplication;
import com.appspanel.manager.conf.APLocalConfiguration;
import com.hcnx.hello.callbacks.HelloCallback;
import com.hcnx.hello.configuration.Hello;

public class AppsPanel extends AppsPanelApplication {

    public APLocalConfiguration getAppsPanelConfiguration() {
        APLocalConfiguration mAPLocalConfiguration = new APLocalConfiguration(
                this,
                "ods",
                "2nXM1AWh",
                "915506476229",
                R.drawable.ic_launcher
        );
        return mAPLocalConfiguration;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Hello hello = Hello.getInstance(getApplicationContext());
        hello.showLog(BuildConfig.DEBUG);
        hello.configure("LMG_158sge548erj3", " ", "915506476229");
        hello.register(new HelloCallback() {
            @Override
            public void onRequestFinished(boolean b, String s, Integer integer) {
                Log.d("hello", "hcnxid " + Hello.getHnId(getApplicationContext()));
            }
        });


    }
}
