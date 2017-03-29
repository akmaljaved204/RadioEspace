package egingenierie.radioespace.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

@SuppressWarnings("deprecation")
public class RestClient {
    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;

    public enum RequestMethod {
        GET, POST
    }

    private String url;
    private int responseCode;
    private String message;
    private String response;

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }


    public RestClient() {

    }

    public RestClient(String url) {
        this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }

    public void AddParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }

    public void Execute(RequestMethod method) throws Exception {

        switch (method) {
            case GET: {
                String combinedParams = "";
                if (!params.isEmpty()) {
                    combinedParams += "/";
                    for (NameValuePair p : params) {
                        String paramString = p.getName() + "/"
                                + URLEncoder.encode(p.getValue(), "UTF-8");

                        if (combinedParams.length() > 1) {
                            combinedParams += "/" + paramString;
                        } else {
                            combinedParams += paramString;
                        }
                    }
                }
                HttpGet request = new HttpGet(url + combinedParams);
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }
                executeRequest(request);
                break;
            }
            case POST: {
                HttpPost request = new HttpPost(url);
                for (NameValuePair h : headers) {
                    request.addHeader(h.getName(), h.getValue());
                }
                if (!params.isEmpty()) {
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }
                executeRequest(request);
            }

        }
    }

    private void executeRequest(HttpUriRequest request)
            throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        httpResponse = client.execute(request);
        responseCode = httpResponse.getStatusLine().getStatusCode();
        message = httpResponse.getStatusLine().getReasonPhrase();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            response = convertStreamToString(instream);
            instream.close();
        }
    }

    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    public String httpGetRequest(String url) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return convertStreamToString(entity.getContent());
        }
        return null;
    }

    public String doHttpGet(String url) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
