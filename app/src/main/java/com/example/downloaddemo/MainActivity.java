package com.example.downloaddemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.downloaddemo.adapter.DownloadTaskAdapter;
import com.example.downloaddemo.adapter.IsDownloadTaskAdapter;
import com.example.downloaddemo.callback.DownloadCallback;
import com.example.downloaddemo.model.DownloadObject;
import com.example.downloaddemo.model.DownloadRange;
import com.example.downloaddemo.model.DownloadTask;
import com.example.downloaddemo.model.DownloadTemp;
import com.example.downloaddemo.model.IsDownloadObject;
import com.example.downloaddemo.thread.DownloadThreadPool;
import com.example.downloaddemo.utils.FileDownloader;
import com.example.downloaddemo.utils.SomeUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private Context mContext;
    private List<DownloadObject> downloadObjects = new ArrayList<>();
    private List<IsDownloadObject> isDownloadObjects = new ArrayList<>();
    private DownloadTaskAdapter downloadTaskAdapter;

    private IsDownloadTaskAdapter isDownloadTaskAdapter;

    private File downloadsDir;
    private DownloadThreadPool threadPool;
    private static final AtomicLong id = new AtomicLong(1);

    //注册回调，从其他页面返回会携带数据,定义的该回调是从CreateActivity返回数据
    private final ActivityResultLauncher<Intent> activityResultLauncherDownload = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    DownloadTemp back_data = result.getData().getParcelableExtra("temp");
                    Log.d(TAG, back_data.toString());
                    DownloadObject downloadObject = new DownloadObject(back_data.getUrl(), back_data.getTargetSize(), back_data.getName(), generate());
                    downloadObject.setThreadCount(FileDownloader.calculateOptimalThreadCount(back_data.getTargetSize()));
                    Log.d(TAG, downloadObject.getThreadCount() + "");
                    downloadObject.setTargetFile(new File(downloadsDir, back_data.getName()));
                    downloadObjects.add(downloadObject);
                    downloadTaskAdapter.notifyDataSetChanged();
                    threadPool.adjustMaxPool(downloadObject.getThreadCount());
                    List<DownloadRange> ranges = FileDownloader.calculateRanges(downloadObject);
                    DownloadCallback callback = new DownloadCallback() {
                        @Override
                        public void onProgress(DownloadObject downloadObject) {
                            downloadTaskAdapter.refreshItem(downloadObject);
                        }

                        @Override
                        public void onComplete(DownloadObject downloadObject) {
                            Log.d(TAG, "下载最终完成");
                            downloadObjects.remove(downloadObject);
                            //添加数据
                            IsDownloadObject isDownloadObject = new IsDownloadObject();
                            isDownloadObject.setName(downloadObject.getName());
                            isDownloadObject.setFile(downloadObject.getTargetFile());
                            isDownloadObject.setSize(SomeUtils.compareSize(downloadObject.getTargetSize()));
                            Date date = new Date();
                            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                            isDownloadObject.setDownloadDate(df.format(date));

                            isDownloadObjects.add(isDownloadObject);
                            runOnUiThread(() -> {
                                isDownloadTaskAdapter.notifyDataSetChanged();
                                downloadTaskAdapter.notifyDataSetChanged();
                            });
                        }

                        @Override
                        public void onError(DownloadObject downloadObject) {

                        }
                    };
                    for (DownloadRange range: ranges) {
                        threadPool.execute(new DownloadTask(range, callback));
                    }
                }
            }
    );

    //该回调是从setting返回数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //拿到线程池
        threadPool = DownloadThreadPool.getInstance();
        getFilePath();
        initialView();
    }

    protected void getFilePath() {
        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        downloadsDir = new File(downloads, "MyDownloadsDemo");
        if (!downloadsDir.exists()) downloadsDir.mkdirs();
    }
    protected void initialView() {
        ImageView ivMenu = findViewById(R.id.image_button);

        ivMenu.setOnClickListener(v -> {
            Log.d(TAG, "ivMenu被按下");
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_option, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.create_download) {
                    Log.d(TAG, "这是新建下载");
                    createOnClick();
                }
                else if (item.getItemId() == R.id.settings) {
                    Log.d(TAG, "这是设置");
                    settingOnClick();
                }
                return false;
            });
            popupMenu.show();
        });
        ListView downloading_listview = findViewById(R.id.main_downloading_listview);
        downloadTaskAdapter = new DownloadTaskAdapter(this, downloadObjects, downloading_listview);
        downloading_listview.setAdapter(downloadTaskAdapter);

        ListView isDownloaded_listview = findViewById(R.id.main_isDownloaded_listview);
        isDownloadTaskAdapter = new IsDownloadTaskAdapter(isDownloadObjects, this);
        isDownloaded_listview.setAdapter(isDownloadTaskAdapter);
    }

    protected void createOnClick() {
        Intent intent = new Intent(this, CreateActivity.class);
        activityResultLauncherDownload.launch(intent);
    }

    protected void settingOnClick() {

    }

    public long generate() {
        return id.getAndIncrement();
    }

}
