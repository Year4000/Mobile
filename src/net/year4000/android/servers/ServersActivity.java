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
                ExpListItems = SetStandardGroups();
                ExpAdapter = new ExpandListAdapter(ServersActivity.this, ExpListItems);
                ExpandList.setAdapter(ExpAdapter);
            }
        });
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
            serverList();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
        }

    }

    public List<ExpandListGroup> SetStandardGroups() {
        List<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
        //ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();

        Map<String, String> servers = APIManager.get().getGroups();

        for (Map.Entry<String, String> entry : servers.entrySet()) {
            list.add(new ExpandListGroup(entry));
        }

        /*ExpandListGroup listGroup = new ExpandListGroup();
        listGroup.setName("Network");
        list2 = new ArrayList<ExpandListChild>();
        ExpandListChild child = new ExpandListChild();
        child.setName("View Network");
        child.setTag(null);
        list2.add(child);
        listGroup.setItems(list2);
        list.add(listGroup);*/

        return list;
    }
}
