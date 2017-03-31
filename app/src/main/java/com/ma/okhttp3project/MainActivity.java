package com.ma.okhttp3project;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvRes)
    TextView tvRes;
    @BindView(R.id.btnGET)
    Button btnGET;
    @BindView(R.id.btnPOST)
    Button btnPOST;
    @BindView(R.id.btnAsy)
    Button btnAsy;
    @BindView(R.id.btnAsyPost)
    Button btnAsyPost;
    @BindView(R.id.btnDownload)
    Button btnDownload;
    @BindView(R.id.progressDownload)
    ProgressBar progressDownload;
    @BindView(R.id.btnCancelDownload)
    Button btnCancelDownload;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.btnUpload)
    Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onEventMsg(String event) {
        tvRes.setText(event + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MainActivity.this);
    }

    @OnClick({R.id.btnGET, R.id.btnPOST, R.id.btnAsy, R.id.btnAsyPost, R.id.btnDownload, R.id.btnCancelDownload,R.id.btnUpload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGET:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String res = null;
                        try {
                            res = OkHttpHlper.getInstance().get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        EventBus.getDefault().post(res);
                    }
                }).start();

                break;
            case R.id.btnPOST:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String res = OkHttpHlper.getInstance().post();
                            EventBus.getDefault().post(res);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.btnAsy:
                OkHttpHlper.getInstance().asyGet();
                break;
            case R.id.btnAsyPost:
                OkHttpHlper.getInstance().asyPost();
                break;
            case R.id.btnDownload:
                OkHttpHlper.getInstance().download(progressDownload, iv);
                break;
            case R.id.btnCancelDownload:
                OkHttpHlper.getInstance().cancelDownload();
                break;
            case R.id.btnUpload:
                ArrayList<File> files = new ArrayList<>() ;
                File file = new File("内部存储/file.txt") ;
                files.add(file) ;
                OkHttpHlper.getInstance().uploadFile(files);
                break;


        }
    }


}
