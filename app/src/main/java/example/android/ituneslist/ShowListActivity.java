package example.android.ituneslist;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowListActivity extends AppCompatActivity {

    TextView artistName1;
    TextView collectionName1;
    TextView collectionPrice1;
    TextView currency1;

    TextView artistName2;
    TextView collectionName2;
    TextView collectionPrice2;
    TextView currency2;

    String itunesInfo[][] = new String [10] [3];

    ListView list_view;

    //ArrayList<HashMap<String, String>> itunesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

         artistName1 = findViewById(R.id.artistName1);
         collectionName1 = findViewById(R.id.collectionName1);
         collectionPrice1 = findViewById(R.id.collectionPrice1);
         currency1 = findViewById(R.id.currency1);

        artistName2 = findViewById(R.id.artistName2);
        collectionName2 = findViewById(R.id.collectionName2);
        collectionPrice2 = findViewById(R.id.collectionPrice2);
        currency2 = findViewById(R.id.currency2);

         // not using this
       // itunesList = new ArrayList<>();
       // list_view = (ListView) findViewById(R.id.list);

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
                    //HashMap<String, String> itunes = new HashMap<>();

                    //itunes.put("name", name);
                    //itunes.put("album", album);
                    //itunes.put("price", price);
                    itunesInfo[i][0] = name;
                    itunesInfo[i][1] = album;
                    itunesInfo[i][2] = price;
                    itunesInfo[i][3] = currency;

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
        @Override
        protected void onPostExecute(String result) {
            artistName1.setText(itunesInfo[0][0]);
            collectionName1.setText(itunesInfo[0][1]);
            collectionPrice1.setText(itunesInfo[0][2]);
            currency1.setText("$");

            artistName2.setText(itunesInfo[1][0]);
            collectionName2.setText(itunesInfo[1][1]);
            collectionPrice2.setText(itunesInfo[1][2]);
            currency2.setText("$");


            // ListAdapter adapter = new SimpleAdapter(ShowListActivity.this, itunesList,
           //         R.layout.list_item, new String[]{"artistName", "collectionName", "collectionPrice"},
           //         new int[]{R.id.artistName, R.id.collectionName, R.id.collectionPrice});
           // list_view.setAdapter(adapter);
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
