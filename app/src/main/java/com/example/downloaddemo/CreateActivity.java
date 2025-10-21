package com.example.downloaddemo;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.downloaddemo.model.DownloadObject;
import com.example.downloaddemo.model.DownloadTemp;
import com.example.downloaddemo.utils.DownloadHelper;
import com.example.downloaddemo.utils.Response;


public class CreateActivity extends AppCompatActivity {
    private final String TAG = "CreateActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initialView();
    }

    protected void initialView() {
        ImageView back = findViewById(R.id.create_back);
        back.setOnClickListener(v -> {
            finish();
        });

        EditText editText = findViewById(R.id.create_editText);
        editText.setText(R.string.test_link);
        Button downLoadButton = findViewById(R.id.create_download_button);
        //设置变化监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString().trim();
                Log.d(TAG, temp);
                downLoadButton.setEnabled(!temp.isEmpty());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        //点击事件
        downLoadButton.setOnClickListener(v -> {
            String targetHttp = editText.getText().toString().trim();
            Log.d(TAG, targetHttp);
            //进行判断看是否有下载资源
            new Thread(() -> {
                Response<DownloadTemp> res = DownloadHelper.canConnect(targetHttp);
                int resCode = res.code;
                if (resCode == -100) {
                    Log.d(TAG, "资源不可下载");
                    runOnUiThread(() -> {
                        Toast.makeText(this, "url不可用", Toast.LENGTH_SHORT).show();
                    });
                }
                else{
                    Log.d(TAG, res.data.toString());
                    Intent intent = new Intent();
                    intent.putExtra("temp", res.data);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }).start();
        });
    }

}
