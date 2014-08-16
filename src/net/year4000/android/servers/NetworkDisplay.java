package net.year4000.android.servers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
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
        head.setText("Network");
        Intent intent = getIntent();
        PostFetcher fetcher = new PostFetcher(NetworkDisplay.this);
        fetcher.execute();
    }

    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView text = (TextView)findViewById(R.id.netPlayers);
                Map servers = APIManager.get().getServers();

                /*
                if (server == null || !server.isOnline()) {
                    text.setText(Html.fromHtml("Server Offline"));
                }
                else {
                    Server.Players players = server.getStatus().getPlayers();
                    text.append(Html.fromHtml(formatDescription(server.getStatus().getDescription())) + "\n");
                    text.append(String.format("Players (%d/%d) \n", players.getOnline(), players.getMax()));

                    if (server.isSample()) {
                        text.append(Joiner.on(", ").join(players.getPlayerNames()));
                    }
                }*/
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

