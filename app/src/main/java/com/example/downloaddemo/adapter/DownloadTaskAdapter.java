package com.example.downloaddemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;


import com.example.downloaddemo.R;
import com.example.downloaddemo.model.DownloadObject;
import com.example.downloaddemo.viewholder.ViewHolder;

import java.util.List;

public class DownloadTaskAdapter extends BaseAdapter {
    private static final String TAG = "DownloadTaskAdapter";
    private LayoutInflater mInflater;
    private Context context;
    private List<DownloadObject> mData;
    private ListView listView;
    private Bitmap bitmap;

    public DownloadTaskAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.outline_android_24);
    }

    public DownloadTaskAdapter(Context context, List<DownloadObject> list, ListView listView) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.outline_android_24);
        mData = list;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DownloadObject object = mData.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.download_list_item_layout, parent, false);
            holder = new ViewHolder();
            holder.textView_bigTitle = convertView.findViewById(R.id.list_item_big_title);
            holder.textView_smallTitle = convertView.findViewById(R.id.list_item_small_title);
            holder.textView_state = convertView.findViewById(R.id.list_item_state);
            holder.progressIndicator = convertView.findViewById(R.id.list_item_progress);
            holder.btn = convertView.findViewById(R.id.list_item_btn);
            holder.icon = convertView.findViewById(R.id.list_item_image);
            //只new一次并且只在开始初始化后面单个进行刷新
            holder.textView_smallTitle.setText(object.showSize());
            holder.textView_bigTitle.setText(object.getName());
            holder.icon.setImageBitmap(bitmap);
            holder.textView_state.setText(object.getSpeed());
            holder.btn.setText("暂停");
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag(); // 一直复用
        }
        return convertView;
    }

    public void refreshItem(DownloadObject downloadObject) {
        int position = mData.indexOf(downloadObject);
        if (position == -1) return ;//已删除
        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        if (position >= first && position <= last) {
            View view = listView.getChildAt(position - first);
            if (view != null) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.textView_smallTitle.setText(downloadObject.showSize());
                Log.d(TAG, downloadObject.getSpeed());
                viewHolder.textView_state.setText(downloadObject.getSpeed());
                viewHolder.progressIndicator.setProgress((int) (downloadObject.getNowSize() * 100 / downloadObject.getTargetSize()));
            }
        }
    }
}
