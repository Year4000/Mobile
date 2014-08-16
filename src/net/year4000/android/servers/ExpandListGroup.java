package net.year4000.android.servers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
public class ExpandListGroup {
    private String name;
    private String playerCount;
    private List<ExpandListChild> items = new ArrayList<ExpandListChild>();

    public ExpandListGroup(Map.Entry<String, String> entry) {
        name = entry.getValue();

        for (Server server : APIManager.get().getServersByGroupName(entry.getKey())) {
            items.add(new ExpandListChild(server));
        }

        updatePlayerCount();
    }

    public String updatePlayerCount() {
        return playerCount = String.format(" (%d/%d)", getTotalOnline(), getTotalMax());
    }

    /** The max total of player capacity in this group */
    public int getTotalMax() {
        int max = 0;

        for (ExpandListChild server : items) {
            if (server.getServer().isOnline()) {
                max += server.getServer().getStatus().getPlayers().getMax();
            }
        }

        return max;
    }

    /** The total of online players in this group */
    public int getTotalOnline() {
        int totalOnline = 0;

        for (ExpandListChild item : items) {
            Server server = item.getServer();
            if (server.isOnline()) {
                int sample = server.isSample() ? server.getStatus().getPlayers().getSample().size() : 0;
                int online = server.getStatus().getPlayers().getOnline();

                totalOnline += sample > online ? sample : online;
            }
        }

        return totalOnline;
    }
}
