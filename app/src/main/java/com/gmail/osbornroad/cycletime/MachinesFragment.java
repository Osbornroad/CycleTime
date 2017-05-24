package com.gmail.osbornroad.cycletime;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract;
import com.gmail.osbornroad.cycletime.model.Machine;


/**
 * A simple {@link Fragment} subclass.
 */
public class MachinesFragment extends Fragment
        implements MachineListAdapter.ListItemClickListener,
        NavigationFragment,
        SavableToDatabase {

    private static final int NUM_LIST_ITEMS = 100;
    private MachineListAdapter machineListAdapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;

    private final int FRAGMENT_ID = 3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public MachinesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_machine_choose, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_machine);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /**
         * Get adapter, set to RecyclerView
         */

        mainActivity = (MainActivity) getActivity();

        Cursor cursor = getAllMachines();
        machineListAdapter = new MachineListAdapter(this, cursor);
        recyclerView.setAdapter(machineListAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(Machine machine) {
        mainActivity.selectedMachine = machine;
        mainActivity.switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));
    }

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }

    @Override
    public void updateView() {

    }


    private Cursor getAllMachines() {
        return mainActivity.mDb.query(
                StopWatchContract.MachineEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME
        );
    }

    @Override
    public String getTableName() {
        return StopWatchContract.MachineEntry.TABLE_NAME;
    }

    @Override
    public String getRowIdFromDatabase() {
        return StopWatchContract.MachineEntry._ID;
    }
}
