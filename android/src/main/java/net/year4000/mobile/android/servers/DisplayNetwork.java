package net.year4000.mobile.android.servers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
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
    private String[] namesArray;
    private Bitmap[] headsArray;
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.display_network_info);
        } else {
            setContentView(R.layout.display_network_info_land);
        }
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
        setPlayersArrays(players);
        final HeadsGridAdapter gridAdapter = new HeadsGridAdapter(DisplayNetwork.this, headsArray, namesArray);
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

    private void setPlayersArrays(List<String> playerNames) {
        List<Bitmap> playersHeadList = new ArrayList<Bitmap>();
        List<String> playerNameList = new ArrayList<String>();
        for (String name : playerNames) {
            Bitmap player = HeadsManager.get(DisplayNetwork.this).getImageBitmap(name);
            playersHeadList.add(player);
            playerNameList.add(name);
        }
        headsArray = new Bitmap[playersHeadList.size()];
        namesArray = new String[playerNameList.size()];
        playersHeadList.toArray(headsArray);
        playerNameList.toArray(namesArray);
    }


}

