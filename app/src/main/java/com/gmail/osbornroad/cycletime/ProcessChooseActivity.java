package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gmail.osbornroad.cycletime.dao.FakeProcessDaoImpl;
import com.gmail.osbornroad.cycletime.service.FakeProcessServiceImpl;

public class ProcessChooseActivity extends AppCompatActivity implements ProcessListAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 100;
    private ProcessListAdapter processListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_choose);

        recyclerView = (RecyclerView) findViewById(R.id.rv_process);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        processListAdapter = new ProcessListAdapter(new FakeProcessServiceImpl(new FakeProcessDaoImpl()), this);
        recyclerView.setAdapter(processListAdapter);
    }

    @Override
    public void onListItemClick(int clickedProcessId) {
        Intent intent = new Intent();
        intent.putExtra("processId", clickedProcessId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
