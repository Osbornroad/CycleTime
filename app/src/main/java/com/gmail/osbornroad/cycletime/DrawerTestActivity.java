package com.gmail.osbornroad.cycletime;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

public class DrawerTestActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_test);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }
}
