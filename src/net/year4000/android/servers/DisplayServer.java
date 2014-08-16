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

import java.util.HashMap;
import java.util.Map;

public class DisplayServer extends Activity {
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

    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Server server = APIManager.get().getServers().get(chosen);

                TextView text = (TextView)findViewById(R.id.servPlayers);
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
                }
            }
        });
    }

    public String formatDescription(String des) {
        HashMap<String, String> colors = new HashMap<String, String>();
        colors.put("§a", "</span><span style='color:#5f5;'>");
        colors.put("§b", "</span><span style='color:#5ff;'>");
        colors.put("§c", "</span><span style='color:#f55;'>");
        colors.put("§d", "</span><span style='color:#f5f;'>");
        colors.put("§e", "</span><span style='color:#ff5;'>");
        colors.put("§f", "</span><span style='color:#fff;'>");
        colors.put("§0", "</span><span style='color:#000;'>");
        colors.put("§1", "</span><span style='color:#00a;'>");
        colors.put("§2", "</span><span style='color:#0a0;'>");
        colors.put("§3", "</span><span style='color:#0aa;'>");
        colors.put("§4", "</span><span style='color:#a00;'>");
        colors.put("§5", "</span><span style='color:#a0a;'>");
        colors.put("§6", "</span><span style='color:#fa0;'>");
        colors.put("§7", "</span><span style='color:#aaa;'>");
        colors.put("§8", "</span><span style='color:#555;'>");
        colors.put("§9", "</span><span style='color:#55f;'>");
        colors.put("§k", "</span><span style='color:#fff;'>");
        colors.put("§o", "</span><span style='color:#fff;'>");
        colors.put("§l", "</span><span style='color:#fff;'>");
        colors.put("§m", "</span><span style='color:#fff;'>");
        colors.put("§r", "</span><span style='color:#fff;'>");
        for(Map.Entry<String, String> entry : colors.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            des = des.replace(key, value);
        }
        return ("<span>" + des + "</span>");
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
