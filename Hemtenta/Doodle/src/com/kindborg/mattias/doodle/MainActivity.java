package com.kindborg.mattias.doodle;

import android.app.*;
import android.os.*;

public class MainActivity extends Activity {

    private DoodleView doodleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doodleView = (DoodleView) findViewById(R.id.mainview_doodleview);
    }
}
