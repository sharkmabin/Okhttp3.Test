package com.ma.okhttp3project;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by binbin.ma on 2017/3/31.
 */

public class IOUtil {
    public static void closeAll(Closeable... closeables){
        if(closeables == null){
            return;
        }
        for (Closeable closeable : closeables) {
            if(closeable!=null){
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
