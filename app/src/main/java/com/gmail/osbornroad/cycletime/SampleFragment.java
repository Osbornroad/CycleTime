package com.gmail.osbornroad.cycletime;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class SampleFragment extends Fragment
        implements SampleListAdapter.ListItemClickListener,
        SampleListAdapter.ListItemLongClickListener,
        NavigationFragment,
        SavableToDatabase{

    private static final int NUM_LIST_ITEMS = 100;
    private SampleListAdapter sampleListAdapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;

    private final int FRAGMENT_ID = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public SampleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_sample);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
/*        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mainActivity = (MainActivity) getActivity();
        Cursor cursor = getAllSamples();
        sampleListAdapter = new SampleListAdapter(this, this, cursor, getResources());
        recyclerView.setAdapter(sampleListAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                mainActivity.deleteRowFromDatabase(id);
            }
        }).attachToRecyclerView(recyclerView);

        return rootView;
    }

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }

    @Override
    public void updateView() {
        sampleListAdapter.swapCursor(getAllSamples());
    }

    @Override
    public void onListItemClick() {

    }

    @Override
    public void onListItemLongClick() {

    }

    @Override
    public String getTableName() {
        return StopWatchContract.SampleEntry.TABLE_NAME;
    }

    @Override
    public String getRowIdFromDatabase() {
        return StopWatchContract.SampleEntry._ID;
    }

    private Cursor getAllSamples() {
        return mainActivity.mDb.rawQuery(
            "SELECT sample_table._id, quantity, startDateTime, duration," +
                    "    employee_table.employeeName, process_table.processName, machine_table.machineName, parts_table.partsName\n" +
                    "    FROM sample_table" +
                    "    LEFT JOIN employee_table ON sample_table.employeeId = employee_table._id" +
                    "    LEFT JOIN process_table ON sample_table.processId = process_table._id" +
                    "    LEFT JOIN machine_table ON sample_table.machineId = machine_table._id" +
                    "    LEFT JOIN parts_table ON sample_table.partId = parts_table._id" +
                    "    ORDER BY process_table.processName, machine_table.machineName, parts_table.partsName",
            null
        );
    }

/*    SELECT quantity, startDateTime, duration,
    employee_table.employeeName, process_table.processName, machine_table.machineName, parts_table.partsName
    FROM sample_table
    LEFT JOIN employee_table ON sample_table.employeeId = employee_table._id
    LEFT JOIN process_table ON sample_table.processId = process_table._id
    LEFT JOIN machine_table ON sample_table.machineId = machine_table._id
    LEFT JOIN parts_table ON sample_table.partId = parts_table._id*/

}
