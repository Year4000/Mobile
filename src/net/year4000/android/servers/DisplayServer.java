package net.year4000.android.servers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.google.common.base.Joiner;

import com.google.common.collect.ImmutableMap;
import net.year4000.android.R;

public class DisplayServer extends Activity {
    private String chosen;
    private ImmutableMap<String, String> colors;
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
                    text.setText("Server Offline");
                }
                else {
                    Server.Players players = server.getStatus().getPlayers();
                    formatDescription(text, server.getStatus().getDescription());
                    text.append("\n" + String.format("Players (%d/%d) \n", players.getOnline(), players.getMax()));
                    text.append(server.isSample() ? Joiner.on(", ").join(players.getPlayerNames()) : "No active players");
                }
            }
        });
    }

    public void formatDescription(TextView tv, String des) {
        colors = ImmutableMap.<String, String>builder()
            .put("§a", "#55ff55")
            .put("§b", "#55ffff")
            .put("§c", "#ff5555")
            .put("§d", "#ff55ff")
            .put("§e", "#ffff55")
            .put("§f", "#ffffff")
            .put("§0", "#000000")
            .put("§1", "#0000aa")
            .put("§2", "#00aa00")
            .put("§3", "#00aaaa")
            .put("§4", "#aa0000")
            .put("§5", "#aa00aa")
            .put("§6", "#ffaa00")
            .put("§7", "#aaaaaa")
            .put("§8", "#555555")
            .put("§9", "#5555ff")
            .put("§k", "#ffffff")
            .put("§o", "#ffffff")
            .put("§l", "#ffffff")
            .put("§m", "#ffffff")
            .put("§r", "#ffffff")
            .build();

        SpannableStringBuilder wordtoSpan = new SpannableStringBuilder(des);

        for (int i = 0; i < des.length()-2; i++) {
            String key = des.substring(i, i+2);
            int index = des.indexOf(key);
            int length = des.length();

            if (colors.containsKey(key)) {
                String value = colors.get(key);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor(value)), index, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordtoSpan.replace(i, i+2, "");
                des = des.substring(0,i) + des.substring(i+2);
                i = (2 > i) ? 0 : i - 2;
            }

        }

        tv.setText(wordtoSpan);
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
            //dialog.setMessage("Updating Server Info...");
            //dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            APIManager.get().pullAPI();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //dialog.dismiss();
            serverList();
        }
    }
}
