package net.year4000.android_app.servers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import net.year4000.android_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServersActivity extends Activity {

    private ExpandListAdapter expAdapter;
    private List<ExpandListGroup> expListItems;
    private ExpandableListView expandList;
    private static final String TAG = "ServersActivity";
    private SwipeRefreshLayout swipeView;
    private PostFetcher fetcher;

    /** Called when the activity is first created. */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servers_activity);
        fetcher = new PostFetcher(ServersActivity.this);
        fetcher.execute();
        swipeView = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        fetcher = new PostFetcher(ServersActivity.this);
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
    /**runs on the thread*/
    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expandList = (ExpandableListView) findViewById(R.id.serversListView);
                expAdapter = new ExpandListAdapter(ServersActivity.this, expListItems);
                expandList.setAdapter(expAdapter);
                setListScrollListener(expandList);

            }
        });
    }
    /**this only allows swipeRefresh if first child of list is visible*/
    public void setListScrollListener(final ExpandableListView list) {
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) { }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0 && visibleItemCount > 0 && list.getChildAt(0).getTop() >= 0) {
                    swipeView.setEnabled(true);
                }
                else {
                    swipeView.setEnabled(false);
                }

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
