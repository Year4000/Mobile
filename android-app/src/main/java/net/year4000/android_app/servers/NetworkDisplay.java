package net.year4000.android_app.servers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import com.google.common.base.Joiner;
import net.year4000.android_app.R;

import java.util.Map;

public class NetworkDisplay extends Activity{
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_network_info);
        TextView head = (TextView)findViewById(R.id.netInfoHead);
        head.setText("Year4000 Network");
        serverList();
    }

    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView text = (TextView)findViewById(R.id.netPlayers);
                text.setText(setTextView());
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

        return 2 > samples.length() ? "No active players" : String.format("Players (%d/%d)\n%s", online, max, samples.substring(2));
    }

}

