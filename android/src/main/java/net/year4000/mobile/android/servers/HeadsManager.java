package net.year4000.mobile.android.servers;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class HeadsManager {
    private static HeadsManager inst;
    private Context context;
    private String url;
    private Bitmap playersHead = null;
    private List<Bitmap> playersHeadList = new ArrayList<Bitmap>();
    private List<String> playersNames = new ArrayList<String>();
    private ContextWrapper cw;
    private File headsDir;

    public HeadsManager(Context context) {
        this.context = context;
        cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        headsDir = cw.getDir("headsDir", Context.MODE_PRIVATE);
        if(!headsDir.exists()) {
            try {
                headsDir.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getPlayerNames();
        pullHeads();
    }

    /** Get Instance */
    public static HeadsManager get(Context context) {
        if (inst == null) {
            inst = new HeadsManager(context);
        }

        return inst;
    }

    /** Get the names of all players active */
    private void getPlayerNames() {
        Map<String, Server> servers = APIManager.get().getServers();
        for (Server server : servers.values()) {
            List<String> names = null;
            if (server.isSample()) {
                names = server.getStatus().getPlayers().getPlayerNames();
                playersNames.addAll(names);
            }
        }
    }

    /** Download all players heads that are not already saved to internal storage */
    private void pullHeads() {
        for (String player : playersNames) {
            url = "https://www.year4000.net/avatar/" + player + "/40/";
            URL urlValue = null;

            try {
                urlValue = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (urlValue != null) {
                try {
                    //check to see if user head exists
                    File head = new File(headsDir, player);
                    if (!head.exists()) {
                        playersHead = BitmapFactory.decodeStream(urlValue.openConnection().getInputStream());
                        saveToInternalStorage(playersHead, head);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** Save image file to internal storage */
    private void saveToInternalStorage(Bitmap bitmapImage, File head){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(head);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Allow for image to be uploaded to image views */
    public Bitmap getImageBitmap(Context context, String name){
        try{
            File file = new File(headsDir, name);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
