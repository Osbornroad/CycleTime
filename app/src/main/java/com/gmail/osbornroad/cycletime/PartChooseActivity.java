package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gmail.osbornroad.cycletime.dao.FakePartDaoImpl;
import com.gmail.osbornroad.cycletime.service.FakePartServiceImpl;

public class PartChooseActivity extends AppCompatActivity implements PartListAdapter.ListItemClickListener {

    private PartListAdapter partListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_choose);

        recyclerView = (RecyclerView) findViewById(R.id.rv_part);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        partListAdapter = new PartListAdapter(new FakePartServiceImpl(new FakePartDaoImpl()), this);
        recyclerView.setAdapter(partListAdapter);
    }

    @Override
    public void onListItemClick(int clickedPartId) {
        Intent intent = new Intent();
        intent.putExtra("partId", clickedPartId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
