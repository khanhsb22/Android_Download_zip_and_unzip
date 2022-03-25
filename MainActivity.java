package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.demoapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonDownloadFile.setOnClickListener(view -> {
            DownloadZip download = new DownloadZip(binding.progressBar, binding.textView);
            download.execute("https://github.com/gabrielecirulli/2048/archive/master.zip");

        });

        binding.buttonUnzipFile.setOnClickListener(view -> {
            UnzipFile unzipFile = new UnzipFile(binding.progressBar, binding.textView);
            unzipFile.execute("");
        });
    }
}