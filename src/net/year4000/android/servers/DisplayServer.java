package net.year4000.android.servers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import net.year4000.android.R;

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

                TextView players = (TextView)findViewById(R.id.servPlayers);
                if (server.getStatus() == null) {
                    players.setText("Server Offline");
                }
                else {
                    players.setText("Players: (" + server.getStatus().getPlayers().getOnline() +
                            " / " + server.getStatus().getPlayers().getMax()  +
                            ") \n " + server.getStatus().getDescription());
                }
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
            serverList();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
        }
    }
}
