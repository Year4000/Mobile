package net.year4000.android.servers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.year4000.android.MyActivity;
import net.year4000.android.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.swing.text.html.ImageView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class DisplayServer extends Activity{
    private ServersList posts;
    private Server selectedServer;
    private String chosen;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_server_info);
        TextView head = (TextView)findViewById(R.id.servInfoHead);
        Intent intent = getIntent();
        chosen = intent.getStringExtra(ExpandListAdapter.EXTRA_NAME);
        head.setText(chosen);
        PostFetcher fetcher = new PostFetcher(DisplayServer.this);
        fetcher.execute();
    }

    private void serverList(final ServersList posts) {
        this.posts = posts;
        getSelectedServer(posts, chosen);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView players = (TextView)findViewById(R.id.servPlayers);
                players.setText("Players: (" + selectedServer.getStatus().getPlayers().getOnline() +
                                    " / " + selectedServer.getStatus().getPlayers().getMax()  +
                                        ")");
            }
        });
    }

    public void getSelectedServer(ServersList posts, String name) {
        for(Server server : posts.servers) {
            if(server.getName().equals(name))
               selectedServer = server;
        }
    }

    private void failedLoadingServers() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DisplayServer.this, "Failed to load Servers. Have a look at LogCat.", Toast.LENGTH_SHORT).show();
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
            dialog.setMessage("Updating Server Info...");
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

                        posts = MyActivity.GSON.fromJson(reader, ServersList.class);
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
}
