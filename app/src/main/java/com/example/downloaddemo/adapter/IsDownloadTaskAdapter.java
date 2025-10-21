package com.example.downloaddemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.downloaddemo.R;
import com.example.downloaddemo.model.IsDownloadObject;
import com.example.downloaddemo.viewholder.IsDownloadViewHolder;
import com.example.downloaddemo.viewholder.ViewHolder;

import java.util.List;

public class IsDownloadTaskAdapter extends BaseAdapter {
    private List<IsDownloadObject> isDownloadObjects;

    private LayoutInflater inflater;
    private Bitmap bitmap;
    @Override
    public int getCount() {
        return isDownloadObjects.size();
    }

    public IsDownloadTaskAdapter(List<IsDownloadObject> isDownloadObjects, Context context) {
        this.isDownloadObjects = isDownloadObjects;
        this.inflater = LayoutInflater.from(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.outline_android_24);
    }

    @Override
    public Object getItem(int position) {
        return isDownloadObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IsDownloadViewHolder viewHolder;
        IsDownloadObject isDownloadObject = isDownloadObjects.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.is_download_list_item_layout, parent, false);
            viewHolder = new IsDownloadViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.isDownload_list_item_image);
            viewHolder.textBigTitle = convertView.findViewById(R.id.isDownload_list_item_big_title);
            viewHolder.smallTextView = convertView.findViewById(R.id.isDownload_list_item_small_title);
            viewHolder.button = convertView.findViewById(R.id.isDownload_list_item_btn);
            //初始化
            convertView.setTag(viewHolder);
            viewHolder.imageView.setImageBitmap(bitmap);
            viewHolder.textBigTitle.setText(isDownloadObject.getName());
            viewHolder.smallTextView.setText(isDownloadObject.getSize());
        }
        else {
            viewHolder = (IsDownloadViewHolder) convertView.getTag();
        }
         return convertView;
    }
}
