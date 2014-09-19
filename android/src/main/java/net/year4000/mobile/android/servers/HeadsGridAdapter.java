package net.year4000.mobile.android.servers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HeadsGridAdapter extends BaseAdapter {

    Context context;
    Bitmap[] data = null;

    public HeadsGridAdapter(Context context, Bitmap[] data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ImageView headView;
        if (view == null) {
            headView = new ImageView(context);
            headView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            headView.setPadding(5, 5, 5, 5);
            headView.setImageBitmap(data[position]);
        } else {
            headView = (ImageView) view;
        }

        return headView;
    }
}