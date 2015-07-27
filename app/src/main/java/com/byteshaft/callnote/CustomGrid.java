package com.byteshaft.callnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class CustomGrid  extends BaseAdapter {
    private Context mContext;
    private final int[] imagesId;

    public CustomGrid(Context c, int[] Imageid) {
        mContext = c;
        this.imagesId = Imageid;
    }

    @Override
    public int getCount() {
        return imagesId.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_single, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_image);
            imageView.setImageResource(imagesId[position]);
        }
        return convertView;
    }
}
