package com.example.demoapp;

import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFile extends AsyncTask<String, String, String> {
    private ProgressBar progressBar;
    private TextView textView;
    private String result = "";

    public UnzipFile(ProgressBar progressBar, TextView textView) {
        this.progressBar = progressBar;
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream;
        ZipInputStream zipInputStream;
        try {
            String destinationFilePath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS + "/" + "zipFile" + "/" + "2058" + ".zip"
            ).getAbsolutePath();
            File zipFile = new File(destinationFilePath);
            String parentFolder = zipFile.getParentFile().getPath();
            String filename;

            inputStream = new FileInputStream(destinationFilePath);
            zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zipInputStream.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File(parentFolder + "/" + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(parentFolder + "/" + filename);

                while ((count = zipInputStream.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zipInputStream.closeEntry();
            }
            zipInputStream.close();
            result = "true";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = "false";
        } catch (IOException e) {
            e.printStackTrace();
            result = "false";
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
            textView.setText("Unzip file success");
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText("Unzip file fail");
        }
    }
}
