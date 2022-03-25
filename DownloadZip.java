package com.example.demoapp;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadZip extends AsyncTask<String, String, String> {
    private ProgressBar progressBar;
    private TextView textView;
    private String result = "";
    private static final String TAG = "DownloadZip";

    public DownloadZip(ProgressBar progressBar, TextView textView) {
        this.progressBar = progressBar;
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(
                        TAG, "Server ResponseCode=" + connection.getResponseCode() +
                                " ResponseMessage=" + connection.getResponseMessage()
                );
            }

            inputStream = connection.getInputStream();

            File directory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS + "/" + "zipFile"
            );
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Từ Android 11, google ko cho phép tải xuống các file có cùng tên
            String destinationFilePath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS + "/" + "zipFile" + "/" + "2058" + ".zip"
            ).getAbsolutePath(); // Thêm generate ký tự ngẫu nhiên vào tên file zip

            File file = new File(destinationFilePath);
            file.createNewFile();

            outputStream = new FileOutputStream(destinationFilePath);

            byte[] data = new byte[4096];
            int count;
            while ((count = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                int count = 0;
                if (outputStream != null) {
                    outputStream.close();
                    count++;
                }
                if (inputStream != null) {
                    inputStream.close();
                    count++;
                }
                if (count == 2) {
                    result = "true";
                }

            } catch (IOException e) {
                e.printStackTrace();
                result = "false";
            }
            if (connection != null)
                connection.disconnect();
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (result.equals("true")) {
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText("Download zip success");
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText("Download zip fail");
        }
    }
}
