package net.year4000.mobile.android.servers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import net.year4000.mobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServersActivity extends Activity {

    private ExpandListAdapter expandListAdapter;
    private List<ExpandListGroup> expandListItems;
    private ExpandableListView expandListView;
    private static final String TAG = "ServersActivity";
    private SwipeRefreshLayout swipeView;
    private FetcherFragment fetcherFragment;
    private Context context;

    /** Called when the activity is first created. */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.servers_activity);
        } else {
            setContentView(R.layout.servers_activity_land);
        }
        context = getApplicationContext();
        fetcherFragment = new FetcherFragment(LoadType.START);
        setFragment(fetcherFragment);
        swipeView = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeView.setColorSchemeColors(Color.rgb(0, 114, 188), Color.WHITE, Color.rgb(0, 114, 188), Color.WHITE);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void run() {
                        fetcherFragment = new FetcherFragment(LoadType.RELOAD);
                        setFragment(fetcherFragment);
                        swipeView.setRefreshing(false);
                    }
                }, 3000);
            }
        });

    }

    /** Set up and start the fetcherFragment */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setFragment(Fragment frag)
    {
        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag("FETCHER_FRAG") == null) {
            fm.beginTransaction().add(R.id.swipeContainer, frag).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    /** runs on the thread */
    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expandListView = (ExpandableListView) findViewById(R.id.serversListView);
                expandListAdapter = new ExpandListAdapter(ServersActivity.this, expandListItems);
                expandListView.setAdapter(expandListAdapter);
                setListScrollListener(expandListView);
            }
        });
    }

    /** this only allows swipeRefresh if first child of list is visible */
    public void setListScrollListener(final ExpandableListView list) {
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0 && visibleItemCount > 0 && list.getChildAt(0).getTop() >= 0) {
                    swipeView.setEnabled(true);
                } else {
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

    /** used to show progress dialog only on start up */
    public enum LoadType {
        START, RELOAD
    }

    /** fragment the async to prevent crash on app disruption */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class FetcherFragment extends Fragment  {

        public static final String FETCHER_FRAG_TAG = "FETCHER_FRAG";
        private ProgressDialog progressDialog;
        private boolean isTaskRunning = false;
        public LoadType loadType;

        public FetcherFragment(LoadType loadType) {
            this.loadType = loadType;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            new PostFetcher().execute();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.servers_activity, container, false);
        }

        /** async to communicate with API */
        @TargetApi(Build.VERSION_CODES.CUPCAKE)
        private class PostFetcher extends AsyncTask<Void, Void, String> {
            private static final String TAG = "PostFetcher";

            public PostFetcher() {}

            @Override
            protected void onPreExecute() {
                isTaskRunning = true;
                if (loadType == LoadType.START) {
                    progressDialog = ProgressDialog.show(getActivity(), "Loading Server Info...", "Please wait! This will only take a moment.");
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                if (loadType == LoadType.START) {
                    APIManager.get();
                    HeadsManager.get(context);
                }
                else {
                    APIManager.get().pullAPI();
                    HeadsManager.get(context).pullData();
                }
                expandListItems = setStandardGroups();
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                serverList();
                isTaskRunning = false;
            }

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            isTaskRunning = false;
        }

        @Override
        public void onDetach() {
            // prevent Activity has leaked window com.android.internal.policy... exception
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            super.onDetach();
        }
    }
}
