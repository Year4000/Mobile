package net.year4000.mobile.servers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import com.google.common.base.Joiner;
import net.year4000.mobile.R;

import java.util.Map;

public class NetworkDisplay extends Activity{
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_network_info);
        TextView headerText = (TextView)findViewById(R.id.netInfoHead);
        headerText.setText("Year4000 Network");
        serverList();
    }

    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView)findViewById(R.id.netPlayers);
                textView.setText(setTextView());
            }
        });
    }

    public String setTextView() {
        Map<String, Server> servers = APIManager.get().getServers();
        StringBuilder samplesBuilder = new StringBuilder();
        String samples;
        int max = 0;
        int online = 0;

        for (Server server : servers.values()) {
            if (server.isOnline()) {
                max += server.getStatus().getPlayers().getMax();
                online += server.getStatus().getPlayers().getTrueOnline();
            }

            if (server.isSample()) {
                samplesBuilder.append(", ").append(Joiner.on(", ").join(server.getStatus().getPlayers().getPlayerNames()));
            }
        }

        samples = samplesBuilder.toString();

        return 2 > samples.length() ? "No active players" : String.format("Players (%d/%d)\n%s", online, max, samples.substring(2));
    }

}

