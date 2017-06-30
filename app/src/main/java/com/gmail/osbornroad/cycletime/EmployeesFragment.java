package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

    boolean sortedByName;
    private Paint p = new Paint();

    public static final String EMPLOYEE_PREFERENCE = "EmployeePreference";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public EmployeesFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sortedByName = false;

        SharedPreferences sharedPreferences = context.getSharedPreferences(EMPLOYEE_PREFERENCE, 0);
        sortedByName = sharedPreferences.getBoolean("sortedByName", sortedByName);
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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_employee);
        fab.setOnClickListener(this);
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
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                Employee employee = ((EmployeeListAdapter.EmployeeViewHolder) viewHolder).getEmployee();
                int id = employee.getId();

                Employee targetEmployee = ((EmployeeListAdapter.EmployeeViewHolder) target).getEmployee();
                int targetOrder = targetEmployee.getOrderNumber();

                if (fromPos < toPos) {
                    for (int i = fromPos + 1; i <= toPos; i++) {
                        Employee empl = ((EmployeeListAdapter.EmployeeViewHolder)recyclerView.findViewHolderForAdapterPosition(i)).getEmployee();
                        int emplId = empl.getId();
                        mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.EmployeeEntry.TABLE_NAME +
                                " SET " + StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER + " = " +
                                StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER + " - " + 1 + " WHERE " +
                                StopWatchContract.EmployeeEntry._ID + " = " + emplId);
                    }
                    mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.EmployeeEntry.TABLE_NAME +
                            " SET " + StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER + " = " +
                            targetOrder  + " WHERE " +
                            StopWatchContract.EmployeeEntry._ID + " = " + id);
                } else {
                    for (int i = toPos; i < fromPos; i++) {
                        Employee proc = ((EmployeeListAdapter.EmployeeViewHolder)recyclerView.findViewHolderForAdapterPosition(i)).getEmployee();
                        int procId = proc.getId();
                        mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.EmployeeEntry.TABLE_NAME +
                                " SET " + StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER + " = " +
                                StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER + " + " + 1 + " WHERE " +
                                StopWatchContract.EmployeeEntry._ID + " = " + procId);
                    }
                    mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.EmployeeEntry.TABLE_NAME +
                            " SET " + StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER + " = " +
                            targetOrder /*+ " + " + 1*/ + " WHERE " +
                            StopWatchContract.EmployeeEntry._ID + " = " + id);
                }

                employeeListAdapter.notifyItemMoved(fromPos, toPos);

                return true;
            }
            
            @Override
            public boolean isLongPressDragEnabled() {
                return !sortedByName;
            }
            
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                if (direction == ItemTouchHelper.START) {
                    mainActivity.deleteRowFromDatabase(id);
                } else {
                    Employee employee = getEmployeeById(id);
                    DialogEmployeeFragment dialogEmployeeFragment = new DialogEmployeeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("employeeSelectedForEdit", employee);
                    dialogEmployeeFragment.setArguments(bundle);
                    dialogEmployeeFragment.show(mainActivity.getSupportFragmentManager(), "dialogEmployeeFragment");
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(mainActivity.getResources().getColor(R.color.result_exists_data));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mode_edit_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                    if(dX < 0) {
                        p.setColor(mainActivity.getResources().getColor(R.color.result_no_data));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

        }).attachToRecyclerView(recyclerView);

        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(EMPLOYEE_PREFERENCE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sortedByName", sortedByName);
        editor.commit();
    }

    @Override
    public void onListItemClick(Employee employee) {
/*        if (mainActivity.longClickEmployeeSelected != null) {
            return;
        }*/
        mainActivity.selectedEmployee = employee;
        mainActivity.switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));
    }



    @Override
    public void onListItemLongClick(Employee employee) {
/*        if (mainActivity.mActionMode != null) {
            return;
        }
        mainActivity.longClickEmployeeSelected = employee;
        mainActivity.mActionMode = mainActivity.startSupportActionMode(mainActivity.mActionModeCallBack);*/
    }

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }

    Cursor getAllEmployees() {
        return mainActivity.mDb.query(
                StopWatchContract.EmployeeEntry.TABLE_NAME,
                null,
                StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE + " = ? OR ?",
                mainActivity.showAll ? new String[]{"0", "1"} : new String[]{"1"},
                null,
                null,
                sortedByName ?
                        StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME :
                        StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER
        );
    }

    Employee getEmployeeById(int id) {
        Cursor cursor = mainActivity.mDb.query(
                StopWatchContract.EmployeeEntry.TABLE_NAME,
                null,
                StopWatchContract.EmployeeEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        if (!cursor.moveToFirst()) {
            return null;
        }
        int orderNumber = cursor.getInt(cursor.getColumnIndex(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ORDER_NUMBER));
        String name = cursor.getString(cursor.getColumnIndex(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME));
        boolean enable = cursor.getInt(cursor.getColumnIndex(StopWatchContract.EmployeeEntry.COLUMN_EMPLOYEE_ENABLE)) == 1;
        return new Employee(id, orderNumber, name, enable);
    }

    @Override
    public void onClick(View v) {
        DialogEmployeeFragment dialogEmployeeFragment = new DialogEmployeeFragment();
        dialogEmployeeFragment.show(mainActivity.getSupportFragmentManager(), "dialogEmployeeFragment");
    }

    @Override
    public void updateView() {
        employeeListAdapter.swapCursor(getAllEmployees());
    }

    @Override
    public boolean getSavedSorting() {
        return sortedByName;
    }

    @Override
    public void setSortingType() {
        sortedByName = !sortedByName;
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
    public boolean isEmployeeSortedByName() {
        return sortedByName;
    }
}
