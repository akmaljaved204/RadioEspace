package egingenierie.radioespace.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import egingenierie.radioespace.PodcastHomeScreen;
import egingenierie.radioespace.model.Podcast;
import egingenierie.radioespace.utils.Constants;

public class APIDataLoaderPodcast {
	private ProgressDialog progressDialog = null;
	private Context context;
	private PodcastHomeScreen podcastHomeScreen;
	private ArrayList<Podcast> podcasts=new ArrayList<Podcast>();

	public APIDataLoaderPodcast(Context contextL, String requestType) {
		context=contextL;
		podcastHomeScreen=(PodcastHomeScreen) contextL;
		new AsyncTaskForNews().execute(requestType);
	}

	public class AsyncTaskForNews extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			try {
				String requestType = params[0];
				String time = new Integer(
						(int) (System.currentTimeMillis() / 1000L)).toString();
				String sig = Constants.base64Encode(Constants.calculateRFC2104HMACBinary(requestType + time,Constants.PRIVATE_KEY));
				RestClient client = new RestClient(Constants.BASE_URL + requestType);
				client.AddHeader("X-Public-Key", Constants.PUBLIC_KEY);
				client.AddHeader("X-Request-Hash", sig);
				client.AddHeader("X-Request-Timestamp", time);
				client.AddHeader("Content-Type","application/json; charset=utf-8");
				client.Execute(RestClient.RequestMethod.POST);
				String response = client.getResponse();
				JSONObject jsonObject=new JSONObject(response);
				JSONArray jsonArray=jsonObject.getJSONArray("content");
				for(int i=0;i<jsonArray.length();i++){
					podcasts.add(new Podcast(jsonArray.getJSONObject(i)));
				}

			} catch (Exception e) {
			}

			return "";
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoading();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			hideLoading();
			podcastHomeScreen.onDataReceive(podcasts);
		}
	}

	public void showLoading() {
		progressDialog = ProgressDialog.show(context,"","Loading...",
				true, true);
		progressDialog.setCancelable(false);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}

	}

	public void hideLoading() {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.cancel();
			}
		}
	}
}
