package com.ma.okhttp3project;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.internal.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by binbin.ma on 2017/3/30.
 */

public class OkHttpHlper {

    private static OkHttpHlper instance;
    private static OkHttpClient client = new OkHttpClient();

    public static OkHttpHlper getInstance() {
        if (instance == null) {
            client.newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)//10秒连接超时
                    .writeTimeout(10, TimeUnit.SECONDS)//10m秒写入超时
                    .readTimeout(10, TimeUnit.SECONDS)//10秒读取超时
                    .build();
            instance = new OkHttpHlper();
        }
        return instance;
    }



    private static final String base_url = "http://mabinbin.top/okservices.php";
    private static final String imgurl = "http://pic.58pic.com/58pic/14/32/85/08a58PICyjw_1024.jpg";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    //GET
    public String get() throws IOException {
        Request request = new Request.Builder()
                .url(base_url + "?method=GET")
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        return res;
    }

    //POST
    public String post() throws IOException {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("method", "POST");

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(base_url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();

        return res;
    }

    //异步 GET
    public void asyGet() {
        Request request = new Request.Builder().url(base_url + "?method=ASY_GET").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //   请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //  请求成功
                //  返回体 ResponseBody body = response.body();
                String res = response.body().string();
                EventBus.getDefault().post(res);
            }
        });
    }

    //异步 POST
    public void asyPost() {
        //step 2: 创建  FormBody.Builder
        FormBody formBody = new FormBody.Builder()
                .add("method", "asy_post")
                .build();

        //step 3: 创建请求
        Request request = new Request.Builder().url(base_url)
                .post(formBody)
                .build();

        //step 4： 建立联系 创建Call对象
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //   请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //  请求成功
                //  返回体 ResponseBody body = response.body();
                String res = response.body().string();
                EventBus.getDefault().post(res);
            }
        });
    }

    public void download(ProgressBar progress, ImageView imageView) {
        DownloadManager.getInstance().download(imgurl, new DownLoadObserver() {
            @Override
            public void onNext(DownloadInfo value) {
                super.onNext(value);
                progress.setMax((int) value.getTotal());
                progress.setProgress((int) value.getProgress());
            }

            @Override
            public void onComplete() {
                if (downloadInfo != null) {
                    String res = "Download success! the file path is " + MyApplication.sContext.getFilesDir() + "/" + downloadInfo.getFileName();
//                    imageView.setImageURI(Uri.fromFile(new File("/sdcard/"+MyApplication.sContext.getFilesDir()+"/"+downloadInfo.getFileName())));
                    EventBus.getDefault().post(res);

                }
            }
        });
    }

    public void cancelDownload() {
        DownloadManager.getInstance().cancel(imgurl);
    }

    public void uploadFile(ArrayList<File> list) {
        //多个文件集合
//        ArrayList<File> list
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置为表单类型
        builder.setType(MultipartBody.FORM);
        //添加表单键值
        builder.addFormDataPart("param", "value");
        for (File file : list) {
            //添加多个文件
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            builder.addFormDataPart("files", file.getName(), fileBody);
        }
        Request request = new Request.Builder()
                .url("http://192.168.1.8/upload/UploadServlet")
                .post(builder.build())
                .build();
        //发起异步请求，并加入回调
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post("upload file fail!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                EventBus.getDefault().post(response.body().string());
            }
        });
    }


}
