package net.year4000.android_app.servers;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class ExpandListChild {
    private String name;
    private String tag;
    private String playerStats;
    private Server server;

    public ExpandListChild(Server server) {
        this.server = server;
        name = server.getName();
        tag = server.getGroup().getDisplay();

        updatePlayerCount();
    }

    public String updatePlayerCount() {
        if (server.isOnline()) {
            Server.Players players = server.getStatus().getPlayers();
            return playerStats = String.format("(%d/%d)", players.getOnline(), players.getMax());
        }
        else {
            return playerStats = "Offline";
        }
    }
}