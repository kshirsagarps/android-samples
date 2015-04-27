package android.sample.mapsearch.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Pratyush Kshirsagar on 2/21/15.
 */
public class Utility {

    private static final String TAG = Utility.class.getSimpleName();

    /**
     * Check if device has network connectivity.
     * @param context
     * @return boolean
     */
    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    /**
     * Open Http Connection.
     * @param url
     * @return HttpURLConnection
     * @throws IOException
     */
    private static HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(Constants.READ_TIMEOUT /* milliseconds */);
        conn.setConnectTimeout(Constants.CONNECTION_TIMEOUT /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setUseCaches(false);
        return conn;
    }

    /**
     * Read the JSON and put it in the lap of {@link #getJsonForUrl(String)} getJSON(String url)} to handle the exceptions.
     * @param httpUrl
     * @return JSONObject
     * @throws IOException
     * @throws JSONException
     */
    private static JSONObject fetchJSON(String httpUrl) throws IOException, JSONException {

        URL url = null;
        url = new URL(httpUrl);
        HttpURLConnection conn = openConnection(url);
        InputStream inputStream = conn.getInputStream();
        int responseCode = conn.getResponseCode();

        InputStreamReader isReader = new InputStreamReader(conn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(isReader);
        StringBuilder data = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            data.append(line + "\r\n");
        }
        if (isReader != null) {
            isReader.close();
        }

        JSONObject result = new JSONObject(data.toString());

        if (conn != null) {
            conn.disconnect();
        }

        return result;
    }

    /**
     * This method also handles the exceptions.
     * Thus we are segregating the exception handling from {@link #fetchJSON(String)} fetchJSON(String httpUrl)}
     * @param url
     * @return JSONObject
     */
    public static JSONObject getJsonForUrl(String url) {
        try {
            JSONObject obj = fetchJSON(url);
            return obj;
        } catch (IOException e) {
            Log.d(TAG,e.getMessage());
        } catch (JSONException e) {
            Log.d(TAG,e.getMessage());
        }

        return null;
    }

    public static void showToast(Context cntx, String msg) {
        Toast.makeText(cntx, msg, Toast.LENGTH_LONG).show();
    }
}