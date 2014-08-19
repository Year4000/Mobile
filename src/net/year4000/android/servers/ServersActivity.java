package net.year4000.android.servers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ExpandableListView;

import net.year4000.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServersActivity extends Activity {
    /** Called when the activity is first created. */
    private ExpandListAdapter ExpAdapter;
    private List<ExpandListGroup> ExpListItems;
    private ExpandableListView ExpandList;
    private static final String TAG = "ServersActivity";

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

    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ExpandList = (ExpandableListView) findViewById(R.id.serversListView);
                ExpAdapter = new ExpandListAdapter(ServersActivity.this, ExpListItems);
                ExpandList.setAdapter(ExpAdapter);
            }
        });
    }

<<<<<<< HEAD
    public List<ExpandListGroup> SetStandardGroups() {
        List<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
        Map<String, String> servers = APIManager.get().getGroups();

        for (Map.Entry<String, String> entry : servers.entrySet()) {
            list.add(new ExpandListGroup(entry));
=======
    public void setNetworkView() {
        TextView net = (TextView)findViewById(R.id.networkView);
        Map<String, Server> servers = APIManager.get().getServers();
        int max = 0;
        int online = 0;
        for (Server server : servers.values()) {
            if (server.isOnline()) {
                max += server.getStatus().getPlayers().getMax();
                online += server.getStatus().getPlayers().getTrueOnline();
            }
>>>>>>> 8dc53fa3cbf6ee55ea7def653e1fffb0c0b79fc3
        }

        return list;
    }

    private class PostFetcher extends AsyncTask<Void, Void, String> {
        private static final String TAG = "PostFetcher";
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
            APIManager.get().pullAPI();
            ExpListItems = SetStandardGroups();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            serverList();
        }

    }
}
