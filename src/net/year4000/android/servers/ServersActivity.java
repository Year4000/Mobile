package net.year4000.android.servers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
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
    private static final String TAG = "ServersActivity";
    private ServersList posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servers_activity);
        PostFetcher fetcher = new PostFetcher(ServersActivity.this);
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
        private ProgressDialog dialog;
        private Activity context;

        public PostFetcher(Activity mainActivity) {
            this.context = mainActivity;
            dialog = new ProgressDialog(context);
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Server Info...");
            dialog.show();
        }

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

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
        }

    }

    public ArrayList<ExpandListGroup> SetStandardGroups() {
        ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
        ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();

        Map<String, String> servers = getGroups();// pull a hashmap from api.year4000.net;

        for(Map.Entry<String, String> entry : servers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            ExpandListGroup listGroup = new ExpandListGroup();
            listGroup.setName(value);
            listGroup.setItems(list2, posts, key);
            list.add(listGroup);
        }

        ExpandListGroup listGroup = new ExpandListGroup();
        listGroup.setName("Network");
        list2 = new ArrayList<ExpandListChild>();
        ExpandListChild child = new ExpandListChild();
        child.setName("View Network");
        child.setTag(null);
        list2.add(child);
        listGroup.setItems(list2);
        list.add(listGroup);

        return list;
    }

    public Map<String, String> getGroups() {
        Map<String, String> newList = new HashMap<String, String>();

        for (Server server : posts.servers) {
            if (!newList.containsKey(server.group.get("name").toString()))
                newList.put(server.group.get("name").toString(),
                        server.group.get("display").toString());
        }

        return newList;
    }

}
