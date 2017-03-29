package egingenierie.radioespace.network;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import egingenierie.radioespace.SplashScreen;
import egingenierie.radioespace.utils.Constants;


public class APiForParameters {
    private SplashScreen splashScreen;
    private String response;

    public APiForParameters(Context context, String lRrequestType) {
        splashScreen = (SplashScreen) context;
        new AsyncTaskForNews().execute(lRrequestType);
    }

    public class AsyncTaskForNews extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String requestType = params[0];
                String time = new Integer((int) (System.currentTimeMillis() / 1000L)).toString();
                String sig = Constants
                        .base64Encode(Constants.calculateRFC2104HMACBinary(requestType + time, Constants.PRIVATE_KEY));
                RestClient client = new RestClient(Constants.BASE_URL + requestType);
                client.AddHeader("X-Public-Key", Constants.PUBLIC_KEY);
                client.AddHeader("X-Request-Hash", sig);
                client.AddHeader("X-Request-Timestamp", time);
                client.AddHeader("Content-Type", "application/json; charset=utf-8");
                client.Execute(RestClient.RequestMethod.POST);
                response = client.getResponse();
                JSONObject jsonObject = new JSONObject(response);
                try {
                    Constants.GOOGLE_ANALYTICS_KEY = jsonObject.getJSONObject("analytics").getString("android");
                    Constants.GOOGLE_ADDMOB_INTERSTITIAL_KEY = jsonObject.getJSONObject("ads").getJSONObject("android").getString("interstitial");
                    if (Constants.GOOGLE_ADDMOB_INTERSTITIAL_KEY.equals("Odsradio_320x480_android")) {
                        Constants.GOOGLE_ADDMOB_INTERSTITIAL_KEY = "ca-app-pub-1307374885636076/2139304347";
                    }
                    /*Constants.GOOGLE_ADDMOB_BANNER_KEY = jsonObject.getJSONObject("ads").getJSONObject("android").getString("banner");
					if(Constants.GOOGLE_ADDMOB_BANNER_KEY.equals("Odsradio_320x50_android")){
						Constants.GOOGLE_ADDMOB_BANNER_KEY="ca-app-pub-1307374885636076/3975964348";
					}*/
                } catch (Exception ex) {

                }
                JSONObject linksObject = jsonObject.getJSONObject("links");
                Constants.WEB_LINK = linksObject.getString("website");
                Constants.MENTION_LEGAL_LINK = linksObject.getString("legals");
                Constants.FACEBOOK_PAGE_LINK = linksObject.getString("facebook");
                Constants.TWITTER_LINK = linksObject.getString("twitter");
                Constants.GPLUS_LINK = linksObject.getString("googleplus");
                Constants.INSTAGRAM_LINK = linksObject.getString("instagram");
                if (Constants.INSTAGRAM_LINK.equals("")) {
                    Constants.INSTAGRAM_LINK = "https://www.instagram.com";
                }
                jsonObject = jsonObject.getJSONObject("rating_url");
                //playStoreLink = jsonObject1.getString("android") ;
                Constants.APP_LINK = jsonObject.getString("android");
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("");
            }

            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            splashScreen.onDataReceive();
        }
    }

//	public void showLoading() {
//		progressDialog = ProgressDialog.show(activity, "", "Loading...", true, true);
//		progressDialog.setCancelable(false);
//		if (!progressDialog.isShowing()) {
//			progressDialog.show();
//		}
//	}
//
//	public void hideLoading() {
//		if (progressDialog != null) {
//			if (progressDialog.isShowing()) {
//				progressDialog.cancel();
//			}
//		}
//	}
}