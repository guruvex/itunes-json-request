package example.android.ituneslist;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputText = findViewById(R.id.myText);

        // make the  AsyncTask to run in the background
        GetUrlContentTask task = new GetUrlContentTask();
        task.execute();
    }

    /**
     * make a background thread to handle the http connection
     */
    private class GetUrlContentTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... url1) {
            HttpHandler sh = new HttpHandler();

            String url = "https://itunes.apple.com/search?term=john&limit=10";

            String jsonString = "";
            try {
                jsonString = sh.makeHttpRequest(createUrl(url));

            } catch (IOException e) {
                return null;
            }

            /**
             * if it worked to this point process the
             * raw jason data is stored in jsonString
             */
            StringBuilder data = new StringBuilder(); // string to hold the processed json data

            try {
                JSONObject rootJsonObj = new JSONObject(jsonString); // build root jason obj
                JSONArray myJsonArray = rootJsonObj.optJSONArray("results"); // build array from the root object

                for (int i = 0; i < myJsonArray.length(); i++) {
                    JSONObject jsonItems = myJsonArray.getJSONObject(i); // pull out each item in the jason array one at a time.

                    String name = jsonItems.optString("artistName").toString();
                    String album = jsonItems.optString("collectionName").toString();
                    String price = jsonItems.optString("collectionPrice").toString();
                    String currency = jsonItems.optString("currency").toString();

                    if (price == "") {
                        price = "n/a";  // catch if the price string is empty and replace
                    }
                    // append new data to string
                    data.append(name).append(" ");
                    data.append(album).append("\n");
                    data.append(price).append(" ");
                    data.append(currency).append("\n");
                }
            } catch (Exception e) {
                System.out.println("error " + e);
            }
            return data.toString();
        }

        /**
         * when the background thread ends this method runs
         */
        protected void onPostExecute(String result) {
            outputText.setText(result);
        }
    }

    /**
     * this takes the url string and catchs if its a invalid url
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }
}
