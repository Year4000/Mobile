package net.year4000.mobile.android;

import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Year4000 extends Application {

    // max age of head file before re-downloading (change params as see fit).
    private long maxAge = TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);

    @Override
    public void onCreate() {
        super.onCreate();
        Date currentDate = new Date();
        File headsDir = this.getDir("headsDir", Context.MODE_PRIVATE);
        File[] directoryListing = headsDir.listFiles();
        if (directoryListing != null) {
            for (File head : directoryListing) {
                Log.e("CHECKING_HEAD", head.toString());
                Date lastModifiedDate = new Date(head.lastModified());
                long headsAge = currentDate.getTime() - lastModifiedDate.getTime();
                if (headsAge >= maxAge) {
                    Log.e("DELETING_HEAD", head.toString());
                    head.delete();
                }
            }
        }
    }
}
