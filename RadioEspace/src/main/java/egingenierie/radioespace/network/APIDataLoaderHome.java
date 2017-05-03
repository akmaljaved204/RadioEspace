package egingenierie.radioespace.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import egingenierie.radioespace.InfosScreen;
import egingenierie.radioespace.model.NewsItems;
import egingenierie.radioespace.utils.Constants;

public class APIDataLoaderHome {
	private ProgressDialog progressDialog = null;
	private Activity activity;
	private InfosScreen aLaUneHomeFragment;
	ArrayList<NewsItems> newsList;

	public APIDataLoaderHome(Activity lActivity, String lRrequestType) {
		activity = lActivity;
		aLaUneHomeFragment = (InfosScreen)lActivity;
		newsList=new ArrayList<NewsItems>();
		new AsyncTaskForNews().execute(lRrequestType);
	}

	public class AsyncTaskForNews extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
				try{
				String requestType = params[0];
				String time = new Integer((int) (System.currentTimeMillis() / 1000L)).toString();
				String sig = Constants.base64Encode(Constants.calculateRFC2104HMACBinary(requestType + time, Constants.PRIVATE_KEY));
				RestClient client = new RestClient(Constants.BASE_URL + requestType);
				client.AddHeader("X-Public-Key", Constants.PUBLIC_KEY);
				client.AddHeader("X-Request-Hash", sig);
				client.AddHeader("X-Request-Timestamp", time);
				client.AddHeader("Content-Type", "application/json; charset=utf-8");
				client.Execute(RestClient.RequestMethod.POST);
				String response = client.getResponse();
				JSONObject jsonObject = new JSONObject(response);
				if(requestType.equals("traffic")){
					jsonObject=jsonObject.getJSONObject("content");
					JSONArray jsonArray= jsonObject.getJSONArray("1");
					for (int i = 0; i < jsonArray.length(); i++) {
						NewsItems newsItems=new NewsItems();
						newsItems.setText(jsonArray.getJSONObject(i).getString("title"));
						newsItems.setStart_date(getFormattedDate(jsonArray.getJSONObject(i).getString("start_date")));
						newsList.add(newsItems);
					}
				}else if(requestType.equals("weather")){
					JSONArray jsonArray=  jsonObject.getJSONArray("content");
					String dayName="Aujourd'hui";
					for (int i = 0; i < jsonArray.length(); i++) {
						if(i==1){
							dayName="Demain";
						}else if(i==2){
							dayName="Après-demain";
						}
						NewsItems newsItems=new NewsItems();
						newsItems.setWeather(jsonArray.getJSONObject(i),dayName,jsonObject.getString("media_url"));
						newsList.add(newsItems);
					}

				}else{
					JSONArray jsonArray1 = jsonObject.getJSONArray("content");
					for (int i = 0; i < jsonArray1.length(); i++) {
						newsList.add(new NewsItems(jsonArray1.getJSONObject(i)));
					}
				}

				}catch (Exception ex){
					System.out.print("");
				}
			return null;
		}
		public String getFormattedDate(String strDate) {
			String formattedDate=strDate;
			try {

				String inputPattern = "yyyy-MM-dd HH:mm:ss";
				String outputPattern = "dd/MM/yyyy";
				SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
				SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.FRANCE);
				String outputPattern1 = "EEEE dd MMMM yyyy";
				SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputPattern1, Locale.FRANCE);

				Date date = null;
				String str = "";
				try {
					date = inputFormat.parse(strDate);
					str = outputFormat.format(date);
					String[] temp=strDate.split(" ");
					temp=temp[1].split(":");
					formattedDate=str+" à "+temp[0]+"h"+temp[1];
					str = outputFormat1.format(date);
					temp=str.split(" ");
					formattedDate=temp[0]+" "+formattedDate;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				return strDate;
			}
			return formattedDate;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				showLoading();
			}catch (Exception ex){
				System.out.print("");
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				hideLoading();
			}catch (Exception ex){
				System.out.print("");
			}
			aLaUneHomeFragment.onDataReceive(newsList);

			}
		}

	public void showLoading() {
		progressDialog = ProgressDialog.show(activity, "", "Loading...", true, true);
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
