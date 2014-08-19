package net.year4000.android.servers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.google.common.base.Joiner;

import net.year4000.android.R;

import java.util.Map;

public class NetworkDisplay extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_network_info);
        TextView head = (TextView)findViewById(R.id.netInfoHead);
        head.setText("Year4000 Network");
        PostFetcher fetcher = new PostFetcher(NetworkDisplay.this);
        fetcher.execute();
    }

    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView text = (TextView)findViewById(R.id.netPlayers);
                text.append(setTextView());
            }
        });
    }

    public String setTextView() {
        Map<String, Server> servers = APIManager.get().getServers();
        StringBuilder samplesBuild = new StringBuilder();
        String samples;
        int max = 0;
        int online = 0;

        for (Server server : servers.values()) {
            if (server.isOnline()) {
                max += server.getStatus().getPlayers().getMax();
                online += server.getStatus().getPlayers().getTrueOnline();
            }

            if (server.isSample()) {
                samplesBuild.append(", ").append(Joiner.on(", ").join(server.getStatus().getPlayers().getPlayerNames()));
            }
        }

        samples = samplesBuild.toString();

        return 2 > samples.length() ? "No active players" : String.format("Players (%d/%d) \n %s", online, max, samples.substring(2));
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
        dialog.setMessage("Updating Server Info...");
        dialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        APIManager.get().pullAPI();
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        serverList();
    }
}
}

