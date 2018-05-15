package com.dc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    LuckyPan luckyPan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        luckyPan = (LuckyPan) findViewById(R.id.pan);

        iv = (ImageView) findViewById(R.id.btn);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!luckyPan.isStart()) {
                    iv.setImageResource(R.drawable.stop);
                    luckyPan.mSpeed = 38;
                } else {
                    iv.setImageResource(R.drawable.start);
                    luckyPan.isShouldEnd = true;
                }
            }
        });
    }
}
