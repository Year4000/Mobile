package net.year4000.mobile.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import net.year4000.mobile.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_settings);
        } else {
            setContentView(R.layout.activity_settings_land);
        }

    }
}
