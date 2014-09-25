package net.year4000.mobile.android.servers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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
    public class Group {
        private String name;
        private String display;
    }

    @Data
    public class StatusResponse {
        private String description;
        private Players players;
        private Version version;
        private String favicon;

        /** decode Base64 to bitmap */
        public Bitmap getFaviconAsBitmap() {
            String faviconSubString = favicon.substring(favicon.indexOf(",") + 1);
            Log.e("FAVICON STRING", faviconSubString);
            byte[] decodedString = Base64.decode(faviconSubString, Base64.DEFAULT);

            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
    }

    @Data
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
    public class Player {
        private String name;
        private String id;
    }

    @Data
    public class Version {
        private String name;
        private String protocol;
    }
}
