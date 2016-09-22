package com.qinglinyi.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.qinglinyi.view.SectionBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionBar sb = (SectionBar) findViewById(R.id.sb);
        sb.setOnActionListener(new SectionBar.OnActionListener() {
            @Override
            public void show(String item) {
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void hidden() {

            }
        });
    }
}
