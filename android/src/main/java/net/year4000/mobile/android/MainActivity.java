package net.year4000.mobile.android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import lombok.Getter;
import net.year4000.mobile.R;
import net.year4000.mobile.android.news.NewsFragment;
import net.year4000.mobile.android.servers.ServersActivity;

@Getter
public class MainActivity extends FragmentActivity {
    private TextView servers;
    private TextView settings;
    private Fragment newsFragment;
    private Fragment serversFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main_land);
        }

        newsFragment = new NewsFragment();
        //serversFragment = new ServersFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, newsFragment)
                .commitAllowingStateLoss();
        currentFragment = newsFragment;

        initializeButtons();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return false;
    }

    private void initializeButtons() {
        servers = (TextView) findViewById(R.id.servers_button);
        servers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServersActivity.class);
                startActivity(intent);
            }
        });

        settings = (TextView) findViewById(R.id.settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    /** Switch UI to the given fragment */
    void switchToFragment(Fragment newFrag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, newFrag)
                .commitAllowingStateLoss();
        currentFragment = newFrag;
    }
}

