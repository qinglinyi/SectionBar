package com.qinglinyi.demo;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.qinglinyi.view.SectionBar;

public class MainActivity extends AppCompatActivity {

    private TextView selectedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionBar sb = (SectionBar) findViewById(R.id.sb);
        selectedTv = (TextView) findViewById(R.id.selectedTv);
        //        sb.setList(new String[]{"A","B","C","D","E","F","G","H","I"});
        sb.setOnActionListener(new SectionBar.OnActionListener() {
            @Override
            public void show(String item) {
                selectedTv.setText(item);
                selectedTv.setVisibility(View.VISIBLE);
                selectedTv.setAlpha(1f);
                ViewCompat.animate(selectedTv).setDuration(1000).alpha(0f).start();
            }

            @Override
            public void hidden() {

            }
        });
    }
}
