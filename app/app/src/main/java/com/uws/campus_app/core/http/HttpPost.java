package com.uws.campus_app.core.http;

import android.util.Log;
import com.uws.campus_app.core.files.File;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpPost implements Http {
    private org.apache.http.client.methods.HttpPost postObject;
    private DefaultHttpClient httpClient;
    private File cacheFile;

    public HttpPost() {
        httpClient = new DefaultHttpClient();
        cacheFile = new File();
    }

    public void open(String url, String cacheFn) {
        postObject = new org.apache.http.client.methods.HttpPost(url);
        if(cacheFn != null && cacheFn.length() > 0) {
            cacheFile.open(cacheFn);
        }
    }

    public void open(String url) {
        open(url, "");
    }

    public String send() {
        String content = "";
        try {
            HttpResponse httpResponse = httpClient.execute(postObject);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream stream = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            content = builder.toString();
            stream.close();
        } catch(IOException e) {
            Log.e("HttpPost", e.getMessage());
        }

        return content;
    }

    public String get() {
        String content = "";
        try {
            InputStreamReader stream = cacheFile.getFileInputStreamReader("iso-8859-1");
            BufferedReader reader = new BufferedReader(stream, 8);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            content = sb.toString();
            stream.close();
            reader.close();
        } catch (Exception e) {
            Log.e("HttpPost", "Error getting cacheFile " + e.toString());
        }

        return content;
    }
}
