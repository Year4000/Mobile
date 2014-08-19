package net.year4000.android.servers;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Server {
    /** The name of the server */
    private String name;

    /** The group that this server is with */
    private Group group;

    /** The ping status */
    private StatusResponse status;

    /** Is the server online */
    public boolean isOnline() {
        return status != null;
    }

    /** Is their a sample of players */
    public boolean isSample() {
        return isOnline() && status.getPlayers().isSample();
    }

    @Data
    @AllArgsConstructor
    public class Group {
        private String name;
        private String display;
    }

    @Data
    @AllArgsConstructor
    public class StatusResponse {
        private String description;
        private Players players;
        private Version version;
        private String favicon;
    }

    @Data
    @AllArgsConstructor
    public class Players {
        private int max;
        private int online;
        private List<Player> sample;

        /** Is their a sample of players */
        public boolean isSample() {
            return sample != null;
        }

        /** Get the player names */
        public List<String> getPlayerNames() {
            List<String> names = new ArrayList<String>();

            if (!isSample()) return names;

            for (Player player : sample) {
                names.add(player.getName());
            }

            return names;
        }

        /** Get the true size to the player count */
        public int getTrueOnline() {
            // we need to check both as our plugins changes
            // the online count and sample to do cool stuff
            return isSample() && sample.size() > online ? sample.size() : online;
        }
    }

    @Data
    @AllArgsConstructor
    public class Player {
        private String name;
        private String id;
    }

    @Data
    @AllArgsConstructor
    public class Version {
        private String name;
        private String protocol;
    }
}
