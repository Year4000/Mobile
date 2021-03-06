package net.year4000.mobile.android.servers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import net.year4000.mobile.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayServer extends Activity {
    private String chosenServer;
    private String[] nameArray;
    private Bitmap[] headsArray;
    private static final ImmutableMap<String, String> COLORS = ImmutableMap.<String, String>builder()
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

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.display_server_info);
        } else {
            setContentView(R.layout.display_server_info_land);
        }

        TextView headerText = (TextView)findViewById(R.id.servInfoHead);
        Intent intent = getIntent();
        chosenServer = intent.getStringExtra(ExpandListAdapter.EXTRA_NAME);
        headerText.setText(chosenServer);
        serverList();
    }

    private void serverList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Server server = APIManager.get().getServers().get(chosenServer);
                ImageView favicon = (ImageView) findViewById(R.id.server_banner_favicon_view);
                TextView playersCountView = (TextView) findViewById(R.id.servPlayers);
                TextView descriptionView = (TextView) findViewById(R.id.servMapDescription);
                GridView gridview = (GridView) findViewById(R.id.playersIconList);

                if (server == null || !server.isOnline()) {
                    playersCountView.setText("No Activity");
                    descriptionView.setText("Server Offline");
                    favicon.setImageResource(R.drawable.ic_launcher);
                }
                else {
                    Server.Players players = server.getStatus().getPlayers();
                    formatDescription(descriptionView, server.getStatus().getDescription());
                    playersCountView.setText(String.format("Players (%d/%d)", players.getOnline(), players.getMax()));
                    Bitmap mapImage = server.getStatus().getFaviconAsBitmap();

                    if (favicon != null && mapImage != null) {
                        favicon.setImageBitmap(mapImage);
                    }
                    if (server.isSample()) {
                        setPlayersArrays(players.getPlayerNames());
                        final HeadsGridAdapter gridadapter = new HeadsGridAdapter(DisplayServer.this, headsArray, nameArray);
                        gridview.setAdapter(gridadapter);
                    }
                    else {
                        playersCountView.append("\nNo active players");
                    }
                }
            }
        });
    }

    public void formatDescription(TextView tv, String description) {
        SpannableStringBuilder wordtoSpan = new SpannableStringBuilder(description);

        for (int i = 0; i < description.length()-2; i++) {
            String key = description.substring(i, i+2);
            int index = description.indexOf(key);
            int length = description.length();

            if (COLORS.containsKey(key)) {
                String value = COLORS.get(key);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor(value)), index, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                wordtoSpan.replace(i, i+2, "");
                description = description.substring(0,i) + description.substring(i+2);
                i = (2 > i) ? 0 : i - 2;
            }

        }

        tv.setText(wordtoSpan);
    }

    private void setPlayersArrays(List<String> playerNames) {
        List<Bitmap> playersHeadList = new ArrayList<Bitmap>();
        List<String> playersNameList = new ArrayList<String>();
        for (String name : playerNames) {
            Bitmap player = HeadsManager.get(DisplayServer.this).getImageBitmap(name);
            playersHeadList.add(player);
            playersNameList.add(name);
        }
        headsArray = new Bitmap[playersHeadList.size()];
        nameArray = new String[playersNameList.size()];
        playersHeadList.toArray(headsArray);
        playersNameList.toArray(nameArray);
    }

}
