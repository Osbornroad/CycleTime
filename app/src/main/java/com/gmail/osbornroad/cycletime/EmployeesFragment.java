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
import com.gmail.osbornroad.cycletime.model.Employee;

public class EmployeesFragment extends Fragment
        implements EmployeeListAdapter.ListItemClickListener,
        EmployeeListAdapter.ListItemLongClickListener,
        NavigationFragment,
        SavableToDatabase,
        EmployeeListAdapter.LongItemClickAccessor,
        View.OnClickListener{

    private static final int NUM_LIST_ITEMS = 100;
    protected EmployeeListAdapter employeeListAdapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;

    private final int FRAGMENT_ID = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public EmployeesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_employee_choose, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_employee);
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

        Cursor cursor = getAllEmployees();
        employeeListAdapter = new EmployeeListAdapter(this, this, cursor, getResources(), this);
        recyclerView.setAdapter(employeeListAdapter);

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
    public void onListItemClick(Employee employee) {
        if (mainActivity.longClickEmployeeSelected != null) {
            return;
        }
        mainActivity.selectedEmployee = employee;
        mainActivity.switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));
    }



    @Override
    public void onListItemLongClick(Employee employee) {
        if (mainActivity.mActionMode != null) {
            return;
        }
        mainActivity.longClickEmployeeSelected = employee;
        mainActivity.mActionMode = mainActivity.startSupportActionMode(mainActivity.mActionModeCallBack);
    }

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }

    protected Cursor getAllEmployees() {
        return mainActivity.mDb.query(
                StopWatchContract.EmployeeEntry.TABLE_NAME,
                null,
                StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE + " = ? OR ?",
                mainActivity.showAll ? new String[]{"0", "1"} : new String[]{"1"},
                null,
                null,
                StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME
        );
    }

    @Override
    public void updateView() {
        employeeListAdapter.swapCursor(getAllEmployees());
    }

    @Override
    public boolean getSavedSorting() {
        return false;
    }

    @Override
    public void setSortingType() {

    }

    @Override
    public String getTableName() {
        return StopWatchContract.EmployeeEntry.TABLE_NAME;
    }

    @Override
    public String getRowIdFromDatabase() {
        return StopWatchContract.EmployeeEntry._ID;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean isEmployeeSortedByName() {
        return false;
    }
}
