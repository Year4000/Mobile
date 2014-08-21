package net.year4000.android_app.servers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.widget.ExpandableListView;
import net.year4000.android_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServersActivity extends Activity {
    /** Called when the activity is first created. */
    private ExpandListAdapter expAdapter;
    private List<ExpandListGroup> expListItems;
    private ExpandableListView expandList;
    private static final String TAG = "ServersActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servers_activity);
        PostFetcher fetcher = new PostFetcher(ServersActivity.this);
        fetcher.execute();
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        PostFetcher fetcher = new PostFetcher(ServersActivity.this);
                        fetcher.execute();

                    }
                }, 3000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expandList = (ExpandableListView) findViewById(R.id.serversListView);
                expAdapter = new ExpandListAdapter(ServersActivity.this, expListItems);
                expandList.setAdapter(expAdapter);
            }
        });
    }

    public List<ExpandListGroup> setStandardGroups() {
        List<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
        Map<String, String> servers = APIManager.get().getGroups();

        for (Map.Entry<String, String> entry : servers.entrySet()) {
            list.add(new ExpandListGroup(entry));
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
            expListItems = setStandardGroups();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            serverList();
        }

    }
}
