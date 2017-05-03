package egingenierie.radioespace.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import egingenierie.radioespace.utils.Constants;


public class APIForSearch {
	private ProgressDialog progressDialog = null;
	private Context context;
	private String type;

	public APIForSearch(Context contextL, String lRrequestType, String searchedString, int pageNumber, String typeL) {
		type=typeL;
		context= contextL;
		new AsyncTaskForNews().execute(lRrequestType,searchedString,""+pageNumber);
	}

	public class AsyncTaskForNews extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			try {
				
				//String requestType = params[0]+"/"+params[1];
				String requestType = params[0];
				String searchedString = params[1];
				String pageNumber = params[2];

				String time = new Integer((int) (System.currentTimeMillis() / 1000L)).toString();
				String sig = Constants.base64Encode(Constants.calculateRFC2104HMACBinary(requestType + time, Constants.PRIVATE_KEY));
				RestClient client = new RestClient(Constants.BASE_URL + requestType);
				client.AddHeader("X-Public-Key", Constants.PUBLIC_KEY);
				client.AddHeader("X-Request-Hash", sig);
				client.AddHeader("X-Request-Timestamp", time);
				client.AddHeader("Content-Type", "application/x-www-form-urlencoded");
				client.AddParam("search", "lyon");
				client.AddParam("page", pageNumber);
				type="news";
				if(!type.equals("")){
					client.AddParam("type", type);
				}
				client.AddParam("category", "locales");
				client.Execute(RestClient.RequestMethod.POST);
				String response = client.getResponse();
				JSONObject jsonObject = new JSONObject(response);

			} catch (Exception e) {
				System.out.println("");
			}

			return null;
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

			System.out.println("");
		}
	}

	public void showLoading() {
		progressDialog = ProgressDialog.show(context, "", "Loading...", true, true);
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
