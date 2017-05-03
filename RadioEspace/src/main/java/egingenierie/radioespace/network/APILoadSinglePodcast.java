package egingenierie.radioespace.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import egingenierie.radioespace.InfosScreen;
import egingenierie.radioespace.PodcastHomeScreen;
import egingenierie.radioespace.model.Podcast;
import egingenierie.radioespace.utils.Constants;

public class APILoadSinglePodcast {
	private InfosScreen infosScreen;
	private Podcast podcast;

	public APILoadSinglePodcast(Context contextL, String requestType) {
		infosScreen=(InfosScreen) contextL;
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
				podcast=new Podcast(jsonArray.getJSONObject(0));

			} catch (Exception e) {
			}

			return "";
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			infosScreen.onPodcastReceive(podcast);
		}
	}
}
