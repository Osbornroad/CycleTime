package com.gmail.osbornroad.cycletime;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract;
import com.gmail.osbornroad.cycletime.model.Process;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProcessesFragment extends Fragment
        implements ProcessListAdapter.ListItemClickListener,
        ProcessListAdapter.ListItemLongClickListener,
        NavigationFragment,
        SavableToDatabase{

    private static final int NUM_LIST_ITEMS = 100;
    protected ProcessListAdapter processListAdapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;

    private final int FRAGMENT_ID = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public ProcessesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_process_choose, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_process);
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

        Cursor cursor = getAllProcesses();
        processListAdapter = new ProcessListAdapter(this, this, cursor);
        recyclerView.setAdapter(processListAdapter);

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
    public void onListItemClick(Process process) {
        if (mainActivity.longClickProcessSelected != null) {
            return;
        }
        mainActivity.selectedProcess = process;
        mainActivity.switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));
    }

    @Override
    public void onListItemLongClick(Process process) {
        if (mainActivity.mActionMode != null) {
            return;
        }
        mainActivity.longClickProcessSelected = process;
        mainActivity.mActionMode = mainActivity.startSupportActionMode(mainActivity.mActionModeCallBack);
    }

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }

    protected Cursor getAllProcesses() {
        return mainActivity.mDb.query(
                StopWatchContract.ProcessEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                StopWatchContract.ProcessEntry.COLUMN_PROCESS_NAME
        );
    }

    @Override
    public void updateView() {
        processListAdapter.swapCursor(getAllProcesses());
    }

    @Override
    public String getTableName() {
        return StopWatchContract.ProcessEntry.TABLE_NAME;
    }

    @Override
    public String getRowIdFromDatabase() {
        return StopWatchContract.ProcessEntry._ID;
    }
}
