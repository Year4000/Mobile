package net.year4000.android;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import com.google.gson.*;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import android.widget.TextView;

public class ServersActivity extends Activity {
    TextView text;
    private static final String TAG = "ServersActivity";
    private ServersList posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PostFetcher fetcher = new PostFetcher();
        fetcher.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.servers, menu);

        return true;
    }

    private void serverList(final ServersList posts) {
        this.posts = posts;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //test info pulled from API and place in textview
                text = (TextView)findViewById(R.id.text);
                String testList = "";
                for (Server server : posts.servers) {
                    if ("".equals(testList))
                        testList = server.name + " " + server.group.get("display");
                    else
                        testList = testList + "\n" + server.name + " " + server.group.get("display");
                }
                text.setText(testList);
            }
        });
    }

    private void failedLoadingServers() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServersActivity.this, "Failed to load Posts. Have a look at LogCat.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class PostFetcher extends AsyncTask<Void, Void, String> {
        private static final String TAG = "PostFetcher";
        public static final String SERVER_URL = "https://api.year4000.net/servers/";

        @Override
        protected String doInBackground(Void... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(SERVER_URL);

                //Perform the request and check the status code
                HttpResponse response = client.execute(post);
                StatusLine statusLine = response.getStatusLine();

                if(statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(ServersList.class, new ServersListDeserializer());
                        Gson gson = gsonBuilder.create();
                        posts = gson.fromJson(reader, ServersList.class);
                        content.close();

                        serverList(posts);
                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
                        failedLoadingServers();
                    }
                } else {
                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
                    failedLoadingServers();
                }
            } catch(Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
                failedLoadingServers();
            }
            return null;
        }

    }
}