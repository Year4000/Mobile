package net.year4000.mobile.android;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.content.SharedPreferences;
import android.util.Log;
import lombok.Getter;
import net.year4000.mobile.R;

@Getter
public class Year4000 extends Application {
    public static Context appContext;
    public static String APP_NAME = "Year4000";
    public static String AVATAR_CACHE_TIME = "Avatar Time";
    // max age of head file before re-downloading (change params as see fit).
    private long maxAge;
    private File headsDir;
    private SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        preferences = getSharedPreferences(Year4000.APP_NAME, Context.MODE_PRIVATE);
        getAvatarCacheTime();
        deleteOldHeads();
    }

    /** Assigns value to maxAge based on preferences avatar cache time. */
    private void getAvatarCacheTime() {
        switch (preferences.getString(Year4000.AVATAR_CACHE_TIME, "Daily")) {
            case "Daily":
                maxAge = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
                break;
            case "Weekly":
                maxAge = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
                break;
            case "Monthly":
                maxAge = TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
                break;
        }
        Log.e("AVATAR_CACHE_TIME", preferences.getString(Year4000.AVATAR_CACHE_TIME, "Daily"));
    }

    /** Deletes avatar files that are older than maxAge. */
    private void deleteOldHeads() {
        Date currentDate = new Date();
        headsDir = this.getDir("headsDir", Context.MODE_PRIVATE);
        File[] directoryListing = headsDir.listFiles();
        if (directoryListing != null) {
            for (File head : directoryListing) {
                Date lastModifiedDate = new Date(head.lastModified());
                long headsAge = currentDate.getTime() - lastModifiedDate.getTime();
                if (headsAge >= maxAge) {
                    head.delete();
                }
            }
        }
    }

    /** Clears all current avatar heads from device. */
    public static void clearHeadsCache() {
        File headsDir = Year4000.appContext.getDir("headsDir", Context.MODE_PRIVATE);
        File[] directoryListing = headsDir.listFiles();
        if (directoryListing != null) {
            for (File head : directoryListing) {
                head.delete();
            }
        }
    }

    /** Returns heads directory contains files. */
    public static boolean isHeadsClear() {
        File headsDir = Year4000.appContext.getDir("headsDir", Context.MODE_PRIVATE);
        File[] directoryListing = headsDir.listFiles();

        return directoryListing.length == 0;
    }
}
