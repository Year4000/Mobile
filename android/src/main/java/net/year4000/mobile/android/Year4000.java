package net.year4000.mobile.android;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.Getter;

@Getter
public class Year4000 extends Application {
    public static Context appContext;
    // max age of head file before re-downloading (change params as see fit).
    private long maxAge = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
    private File headsDir;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        deleteOldHeads();
    }

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
}
