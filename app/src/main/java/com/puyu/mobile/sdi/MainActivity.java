package com.puyu.mobile.sdi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pgyersdk.update.PgyUpdateManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateAPP();  //蒲公英更新监听

    }
    /**
     * 延时显示更新对话框
     */
    private void updateAPP() {
        new PgyUpdateManager.Builder()
                .register();
    }

}