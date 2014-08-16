package net.year4000.android;

import android.app.Activity;
import android.os.Bundle;

import com.google.gson.Gson;

public class MyActivity extends Activity {
    public static final Gson GSON = new Gson();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }
}
