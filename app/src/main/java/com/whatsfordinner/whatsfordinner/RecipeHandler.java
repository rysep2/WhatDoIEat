package com.whatsfordinner.whatsfordinner;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by marck on 17.03.15.
 */
public class RecipeHandler {
    private Activity mHostActivity;
    private DisplayImageOptions options;

    public RecipeHandler(Activity mHostActivity) {
        this.mHostActivity = mHostActivity;

        this.options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

    }

    public void loadRecipes(String searchString) {
        new DownloadRecipe().execute(searchString);
    }

    private class DownloadRecipe extends AsyncTask<String, Void, String> {
        String TAG = "RecipeHandler";
        boolean querySuccessful = false;
        private ArrayList<Recipe> recipeList = new ArrayList<>();


        @Override
        protected String doInBackground(String... params) {
            String getURL = "http://mobile.chefkoch.de/ms/s0/"+ params[0] +"/Rezepte.html";

            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getURL);
            String content = null;

            try {
                HttpResponse response = client.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    InputStream resp = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(resp));
                    StringBuilder out = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                    }
                    content = out.toString();   //Prints the string content read from input stream
                    reader.close();

                    extractRecipes(content);

                    querySuccessful = true;
                } else {
                    Log.d(TAG, "HTTP Status Code was not 200 / OK");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading " + getURL, e);
            }

            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("asyncTask" , result);

            if (querySuccessful) {
                // display the tweets in a listView
                ListView listView = (ListView) mHostActivity
                        .findViewById(R.id.listRecipeViewId);
                listView.setAdapter(new RecipeItemAdapter(mHostActivity,
                        R.layout.listitem,
                        recipeList,
                        options
                ));
            } else {
                // optionally handle the unsuccessful query
            }
            //TextView txt = (TextView) findViewById(R.id.output);
            //txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

        private void extractRecipes(String html) {
            Document doc = Jsoup.parse(html);
            Elements ulList = doc.select("li.recipelist-item");
            String content = ulList.text();

            Iterator<Element> elementIterator = ulList.iterator();
            Element currentElement;
            Recipe recipe;

            while(elementIterator.hasNext()) {
                currentElement = elementIterator.next();
                String text = currentElement.text();
                String title = currentElement.getElementsByTag("h2").text();
                String imgSrc = currentElement.getElementsByTag("img").attr("src");
                imgSrc = imgSrc.replace("tiniefix", "bigfix");
                String recipeLink = currentElement.getElementsByTag("a").attr("href");
                recipe = new Recipe(title, imgSrc, recipeLink);
                recipeList.add(recipe);
                Log.d(TAG, "title: " + title);
                Log.d(TAG, "image: " + imgSrc);
                Log.d(TAG, "Link: " + recipeLink);
                Log.d(TAG, "Element content: " + text);
            }
        }
    }
}
