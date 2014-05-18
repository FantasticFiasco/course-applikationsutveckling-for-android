package com.kindborg.mattias.dialpadapplication;

import java.util.*;

import android.app.*;
import android.os.*;
import android.widget.*;

public class RecentActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CallsDataSource callsDataSource = new CallsDataSource(this);
        callsDataSource.open();
        List<Call> values = callsDataSource.getAll();
        callsDataSource.close();

        ArrayAdapter<Call> adapter = new ArrayAdapter<Call>(
            this,
            android.R.layout.simple_list_item_1,
            values);

        setListAdapter(adapter);
    }
}
