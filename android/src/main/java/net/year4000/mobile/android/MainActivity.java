package net.year4000.mobile.android;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;

import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import lombok.Getter;
import lombok.Setter;
import net.year4000.mobile.R;
import net.year4000.mobile.android.forums.ForumsFragment;
import net.year4000.mobile.android.news.NewsFragment;
import net.year4000.mobile.android.profiles.ProfileFragment;
import net.year4000.mobile.android.servers.*;
import net.year4000.mobile.android.shop.ShopFragment;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MainActivity extends FragmentActivity {
    private TextView servers;
    private TextView settings;
    private TextView news;
    private TextView shop;
    private TextView forums;
    private TextView profile;
    private List<TextView> navigationButtons;
    private NewsFragment newsFragment;
    private ServersFragment serversFragment;
    private SettingsFragment settingsFragment;
    private ShopFragment shopFragment;
    private ForumsFragment forumsFragment;
    private ProfileFragment profileFragment;
    private Fragment currentFragment;

    private ExpandListAdapter expandListAdapter;
    private List<ExpandListGroup> expandListItems;
    private ExpandableListView expandListView;
    private SwipeRefreshLayout swipeView;
    private FetcherFragment fetcherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main_land);
        }

        newsFragment = new NewsFragment();
        settingsFragment = new SettingsFragment();
        shopFragment = new ShopFragment();
        serversFragment = new ServersFragment();
        forumsFragment = new ForumsFragment();
        profileFragment = new ProfileFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, newsFragment)
                .commitAllowingStateLoss();
        currentFragment = newsFragment;

        initializeButtons();

    }

    /** Initializes all main activity buttons. */
    private void initializeButtons() {
        navigationButtons = new ArrayList<>();

        news = (TextView) findViewById(R.id.news_button);
        setStandardOnClickListener(news, newsFragment);
        navigationButtons.add(news);

        servers = (TextView) findViewById(R.id.servers_button);
        setStandardOnClickListener(servers, serversFragment);
        navigationButtons.add(servers);

        settings = (TextView) findViewById(R.id.settings_button);
        setStandardOnClickListener(settings, settingsFragment);
        navigationButtons.add(settings);

        shop = (TextView) findViewById(R.id.shop_button);
        setStandardOnClickListener(shop, shopFragment);
        navigationButtons.add(shop);

        forums = (TextView) findViewById(R.id.forums_button);
        setStandardOnClickListener(forums, forumsFragment);
        navigationButtons.add(forums);

        profile = (TextView) findViewById(R.id.profile_button);
        setStandardOnClickListener(profile, profileFragment);
        navigationButtons.add(profile);

    }

    /** Sets navigation buttons standardized OnClickListener */
    private void setStandardOnClickListener(final TextView textView, final Fragment fragment) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColors();
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.parseColor("#1e6dc8"));
                switchToFragment(fragment);
            }
        });
    }

    /** Initializes the swipe view */
    public void initializeSwipeView(SwipeRefreshLayout swipe) {
        fetcherFragment = new FetcherFragment(LoadType.START);
        setFragment(fetcherFragment);

        swipeView = swipe;
        swipeView.setColorSchemeColors(Color.rgb(0, 114, 188), Color.WHITE, Color.rgb(0, 114, 188), Color.WHITE);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetcherFragment = new FetcherFragment(LoadType.RELOAD);
                setFragment(fetcherFragment);
            }
        });

    }

    /** Resets all navigation buttons to inactive colors. */
    private void resetButtonColors() {
        for (TextView textView : navigationButtons) {
            textView.setBackgroundColor(Color.parseColor("#1e6dc8"));
            textView.setTextColor(Color.WHITE);
        }
    }

    /** Switch UI to the given fragment. */
    void switchToFragment(Fragment newFrag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, newFrag)
                .commitAllowingStateLoss();
        currentFragment = newFrag;
    }

    /** Set up and start the fetcherFragment */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setFragment(android.app.Fragment frag)
    {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.findFragmentByTag("FETCHER_FRAG") == null) {
            fragmentManager.beginTransaction().add(R.id.swipeContainer, frag).commit();
        }

    }

    /** runs on the thread */
    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                expandListView = (ExpandableListView) findViewById(R.id.serversListView);
                expandListAdapter = new ExpandListAdapter(MainActivity.this, expandListItems);
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

    /** used to show progress dialog only on start up */
    public enum LoadType {
        START, RELOAD
    }

    /** fragment the async to prevent crash on app disruption */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class FetcherFragment extends android.app.Fragment {

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

        /** async to communicate with API and download avatars */
        @TargetApi(Build.VERSION_CODES.CUPCAKE)
        private class PostFetcher extends AsyncTask<Void, Void, String> {
            private static final String TAG = "PostFetcher";

            public PostFetcher() {}

            @Override
            protected void onPreExecute() {
                isTaskRunning = true;
                if (loadType == LoadType.START) {
                    progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMessage("Loading Server Info...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgress(0);
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                }
                else {
                    swipeView.setRefreshing(true);
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                if (loadType == LoadType.START) {
                    APIManager.get();
                    publishProgress();
                    HeadsManager.get(MainActivity.this);
                }
                else {
                    APIManager.get().pullAPI();
                    HeadsManager.get(MainActivity.this).pullData();
                }
                expandListItems = setStandardGroups();
                return null;
            }

            @Override
            public void onProgressUpdate(Void... params){
                progressDialog.setProgress(1);
                progressDialog.setMessage("Downloading Avatar Heads...");
            }

            @Override
            protected void onPostExecute(String result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (swipeView.isRefreshing()) {
                    swipeView.setRefreshing(false);
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

            if (swipeView.isRefreshing()) {
                swipeView.setRefreshing(false);
            }

            super.onDetach();
        }
    }
}

