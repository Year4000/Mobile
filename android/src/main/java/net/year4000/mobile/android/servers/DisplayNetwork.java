package net.year4000.mobile.android.servers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import net.year4000.mobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayNetwork extends Activity{
    private Map<String, Server> servers = APIManager.get().getServers();
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
                getGridView();
            }
        });
    }

    public void getGridView(){
        GridView gridView = (GridView) findViewById(R.id.playersIconList);
        List<String> players = new ArrayList<String>();
        for (Server server : servers.values()) {
            if (server.isSample()) {
                players.addAll(server.getStatus().getPlayers().getPlayerNames());
            }
        }
        Bitmap[] headsArray = getHeadsArray(players);
        final HeadsGridAdapter gridAdapter = new HeadsGridAdapter(DisplayNetwork.this, headsArray);
        gridView.setAdapter(gridAdapter);
    }

    private String setTextView() {
        int max = 0;
        int online = 0;

        for (Server server : servers.values()) {
            if (server.isOnline()) {
                max += server.getStatus().getPlayers().getMax();
                online += server.getStatus().getPlayers().getTrueOnline();
            }
        }

        return 0 >= online ? "No active players" : String.format("Players (%d/%d)", online, max);
    }

    private Bitmap[] getHeadsArray(List<String> playerNames) {
        List<Bitmap> playersList= new ArrayList<Bitmap>();
        for (String name : playerNames) {
            Bitmap player = HeadsManager.get(DisplayNetwork.this).getImageBitmap(name);
            playersList.add(player);
        }
        Bitmap[] playersArray = new Bitmap[playersList.size()];
        playersList.toArray(playersArray);

        return playersArray;
    }


}

