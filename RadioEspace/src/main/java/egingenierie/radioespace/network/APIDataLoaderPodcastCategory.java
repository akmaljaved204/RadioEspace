package egingenierie.radioespace.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import egingenierie.radioespace.model.DataLoader;
import egingenierie.radioespace.model.Message;
import egingenierie.radioespace.utils.Constants;


public class APIDataLoaderPodcastCategory {

    private DataLoader dataLoader = null;
    private String requestType = "";
    private String response = "";
    private Message message;
    private ProgressDialog progressDialog = null;
    private Context context;

    public APIDataLoaderPodcastCategory(DataLoader dataLoader, String requestType) {
        this.dataLoader = dataLoader;
        context = (Context) dataLoader;
        new AsyncTaskForNews().execute(requestType);
    }

    public class AsyncTaskForNews extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String requestType = params[0];
                String time = new Integer((int) (System.currentTimeMillis() / 1000L)).toString();
                String sig = Constants.base64Encode(Constants.calculateRFC2104HMACBinary(requestType + time, Constants.PRIVATE_KEY));
                RestClient client = new RestClient(Constants.BASE_URL + requestType);
                client.AddHeader("X-Public-Key", Constants.PUBLIC_KEY);
                client.AddHeader("X-Request-Hash", sig);
                client.AddHeader("X-Request-Timestamp", time);
                client.AddHeader("Content-Type", "application/json; charset=utf-8");
                client.Execute(RestClient.RequestMethod.POST);
                response = client.getResponse();
            } catch (Exception e) {
                response = null;
            }
            return response;
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
            try {
                if (result != null) {
                    dataLoader.datOnSuccess(response);
                } else {
                    message = new Message();
                    message.setMessage("You Are Not Connected To Internet ");
                    dataLoader.datOnError(message);
                }
            } catch (Exception e) {
                message = new Message();
                message.setMessage("You Are Not Connected To Internet ");
                dataLoader.datOnError(message);
            }

        }
    }

    public void showLoading() {
        progressDialog = ProgressDialog.show(context, "", "Loading...",
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
