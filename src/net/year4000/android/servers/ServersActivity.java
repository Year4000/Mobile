package net.year4000.android.servers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import android.widget.ExpandableListView;
import android.widget.*;
import com.google.gson.*;
import com.google.gson.GsonBuilder;
import net.year4000.android.R;
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

public class ServersActivity extends Activity {
    /** Called when the activity is first created. */
    private ExpandListAdapter ExpAdapter;
    private ArrayList<ExpandListGroup> ExpListItems;
    private ExpandableListView ExpandList;
    TextView text;
    private static final String TAG = "ServersActivity";
    private ServersList posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servers_activity);
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

                ExpandList = (ExpandableListView) findViewById(R.id.serversListView);
                ExpListItems = SetStandardGroups();
                ExpAdapter = new ExpandListAdapter(ServersActivity.this, ExpListItems);
                ExpandList.setAdapter(ExpAdapter);
                //test info pulled from API and place in textview
                /*text = (TextView)findViewById(R.id.text);
                String testList = "";
                for (Server server : posts.servers) {
                    if ("".equals(testList))
                        testList = server.name + " " + server.group.get("display");
                    else
                        testList = testList + "\n" + server.name + " " + server.group.get("display");
                }
                text.setText(testList);*/
            }
        });
    }

    private void failedLoadingServers() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServersActivity.this, "Failed to load Servers. Have a look at LogCat.", Toast.LENGTH_SHORT).show();
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

    public ArrayList<ExpandListGroup> SetStandardGroups() {
        ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
        ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();

        ExpandListGroup gru1 = new ExpandListGroup();
        gru1.setName("Survival Games");
        gru1.setItems(list2, posts, "\"us-sg\"");
        list.add(gru1);

        ExpandListGroup gru2 = new ExpandListGroup();
        gru2.setName("PVP");
        gru2.setItems(list2, posts, "\"us-pvp\"");
        list.add(gru2);

        ExpandListGroup gru3 = new ExpandListGroup();
        gru3.setName("HUB");
        gru3.setItems(list2, posts, "\"us-hubs\"");
        list.add(gru3);

        ExpandListGroup gru4 = new ExpandListGroup();
        gru4.setName("Skywars");
        gru4.setItems(list2, posts, "\"us-skywars\"");
        list.add(gru4);

        ExpandListGroup gru5 = new ExpandListGroup();
        gru5.setName("Network");
        list.add(gru5);

            return list;
    }

}
