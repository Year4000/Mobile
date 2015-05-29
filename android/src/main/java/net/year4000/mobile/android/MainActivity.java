package net.year4000.mobile.android;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
    private TextView news;
    private TextView shop;
    private Fragment newsFragment;
    private Fragment serversFragment;
    private Fragment settingsFragment;
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
        settingsFragment = new SettingsFragment();

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
        news = (TextView) findViewById(R.id.news_button);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColors();
                news.setBackgroundColor(Color.WHITE);
                news.setTextColor(Color.parseColor("#1e6dc8"));
                switchToFragment(newsFragment);
            }
        });

        servers = (TextView) findViewById(R.id.servers_button);
        servers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColors();
                servers.setBackgroundColor(Color.WHITE);
                servers.setTextColor(Color.parseColor("#1e6dc8"));
                Intent intent = new Intent(MainActivity.this, ServersActivity.class);
                startActivity(intent);
            }
        });

        settings = (TextView) findViewById(R.id.settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColors();
                settings.setBackgroundColor(Color.WHITE);
                settings.setTextColor(Color.parseColor("#1e6dc8"));
                switchToFragment(settingsFragment);
            }
        });

        shop = (TextView) findViewById(R.id.shop_button);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColors();
                shop.setBackgroundColor(Color.WHITE);
                shop.setTextColor(Color.parseColor("#1e6dc8"));
                //switchToFragment(shopFragment);
            }
        });

    }

    private void resetButtonColors() {
        news.setBackgroundColor(Color.parseColor("#1e6dc8"));
        news.setTextColor(Color.WHITE);
        servers.setBackgroundColor(Color.parseColor("#1e6dc8"));
        servers.setTextColor(Color.WHITE);
        shop.setBackgroundColor(Color.parseColor("#1e6dc8"));
        shop.setTextColor(Color.WHITE);
        settings.setBackgroundColor(Color.parseColor("#1e6dc8"));
        settings.setTextColor(Color.WHITE);
    }

    /** Switch UI to the given fragment */
    void switchToFragment(Fragment newFrag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, newFrag)
                .commitAllowingStateLoss();
        currentFragment = newFrag;
    }
}

