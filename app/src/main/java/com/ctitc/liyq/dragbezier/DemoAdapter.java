package com.ctitc.liyq.dragbezier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DemoAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    public DemoAdapter(Context ctx) {
        this.mContext = ctx;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{
        ImageView imageView;
        TextView tvName;
        TextView tvDate;
        TextView tvContent;
        DragTextView tvCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.demo_item, parent, false);
            viewHolder.tvCount = (DragTextView) convertView.findViewById(R.id.tv_count);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
