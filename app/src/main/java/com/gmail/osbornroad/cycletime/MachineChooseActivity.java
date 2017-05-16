/*
package com.gmail.osbornroad.cycletime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gmail.osbornroad.cycletime.dao.FakeMachineDaoImpl;
import com.gmail.osbornroad.cycletime.service.FakeMachineServiceImpl;

public class MachineChooseActivity extends AppCompatActivity implements MachineListAdapter.ListItemClickListener {

    private MachineListAdapter machineListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_choose);

        recyclerView = (RecyclerView) findViewById(R.id.rv_machine);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        machineListAdapter = new MachineListAdapter(new FakeMachineServiceImpl(new FakeMachineDaoImpl()), this);
        recyclerView.setAdapter(machineListAdapter);
    }

    @Override
    public void onListItemClick(int clickedMachineId) {
        Intent intent = new Intent();
        intent.putExtra("machineId", clickedMachineId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
*/
