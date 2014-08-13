package net.year4000.android.servers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import net.year4000.android.R;

public class DisplayServer extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_server_info);
        TextView head = (TextView)findViewById(R.id.servInfoHead);
        Intent intent = getIntent();
        head.setText(intent.getStringExtra(ExpandListAdapter.EXTRA_NAME));

    }
}
