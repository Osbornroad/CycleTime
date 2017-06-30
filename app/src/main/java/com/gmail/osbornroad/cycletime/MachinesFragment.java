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
import com.gmail.osbornroad.cycletime.model.Machine;


/**
 * A simple {@link Fragment} subclass.
 */
public class MachinesFragment extends Fragment
        implements MachineListAdapter.ListItemClickListener,
        MachineListAdapter.ListItemLongClickListener,
        NavigationFragment,
        SavableToDatabase,
        MachineListAdapter.LongItemClickAccessor,
        View.OnClickListener{

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

    boolean sortedByName;
    private Paint p = new Paint();

    public static final String MACHINE_PREFERENCE = "MachinePreference";

    public MachinesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sortedByName = false;

        SharedPreferences sharedPreferences = context.getSharedPreferences(MACHINE_PREFERENCE, 0);
        sortedByName = sharedPreferences.getBoolean("sortedByName", sortedByName);
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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_machine);
        fab.setOnClickListener(this);
        /**
         * Get adapter, set to RecyclerView
         */

        mainActivity = (MainActivity) getActivity();

        Cursor cursor = getAllMachines();
        machineListAdapter = new MachineListAdapter(this, this, cursor, getResources(), this);
        recyclerView.setAdapter(machineListAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                Machine machine = ((MachineListAdapter.MachineViewHolder) viewHolder).getMachine();
                int id = machine.getId();

                Machine targetMachine = ((MachineListAdapter.MachineViewHolder) target).getMachine();
                int targetOrder = targetMachine.getOrderNumber();

                if (fromPos < toPos) {
                    for (int i = fromPos + 1; i <= toPos; i++) {
                        Machine mchn = ((MachineListAdapter.MachineViewHolder)recyclerView.findViewHolderForAdapterPosition(i)).getMachine();
                        int mchnId = mchn.getId();
                        mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.MachineEntry.TABLE_NAME +
                                " SET " + StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER + " = " +
                                StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER + " - " + 1 + " WHERE " +
                                StopWatchContract.MachineEntry._ID + " = " + mchnId);
                    }
                    mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.MachineEntry.TABLE_NAME +
                            " SET " + StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER + " = " +
                            targetOrder  + " WHERE " +
                            StopWatchContract.MachineEntry._ID + " = " + id);
                } else {
                    for (int i = toPos; i < fromPos; i++) {
                        Machine proc = ((MachineListAdapter.MachineViewHolder)recyclerView.findViewHolderForAdapterPosition(i)).getMachine();
                        int procId = proc.getId();
                        mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.MachineEntry.TABLE_NAME +
                                " SET " + StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER + " = " +
                                StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER + " + " + 1 + " WHERE " +
                                StopWatchContract.MachineEntry._ID + " = " + procId);
                    }
                    mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.MachineEntry.TABLE_NAME +
                            " SET " + StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER + " = " +
                            targetOrder /*+ " + " + 1*/ + " WHERE " +
                            StopWatchContract.MachineEntry._ID + " = " + id);
                }

                machineListAdapter.notifyItemMoved(fromPos, toPos);

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
                    Machine machine = getMachineById(id);
                    DialogMachineFragment dialogMachineFragment = new DialogMachineFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("machineSelectedForEdit", machine);
                    dialogMachineFragment.setArguments(bundle);
                    dialogMachineFragment.show(mainActivity.getSupportFragmentManager(), "dialogMachineFragment");
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
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(MACHINE_PREFERENCE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sortedByName", sortedByName);
        editor.commit();
    }
    
    @Override
    public void onListItemClick(Machine machine) {
/*        if (mainActivity.longClickMachineSelected != null) {
            return;
        }*/
        mainActivity.selectedMachine = machine;
        mainActivity.switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));
    }

    @Override
    public void onListItemLongClick(Machine machine) {
   /*     if (mainActivity.mActionMode != null) {
            return;
        }
        mainActivity.longClickMachineSelected = machine;
        mainActivity.mActionMode = mainActivity.startSupportActionMode(mainActivity.mActionModeCallBack);*/
    }

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }

    private Cursor getAllMachines() {
        return mainActivity.mDb.query(
                StopWatchContract.MachineEntry.TABLE_NAME,
                null,
                StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE + " = ? OR ?",
                mainActivity.showAll ? new String[]{"0", "1"} : new String[]{"1"},
                null,
                null,
                sortedByName ?
                        StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME :
                        StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER
        );
    }

    Machine getMachineById(int id) {
        Cursor cursor = mainActivity.mDb.query(
                StopWatchContract.MachineEntry.TABLE_NAME,
                null,
                StopWatchContract.MachineEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        if (!cursor.moveToFirst()) {
            return null;
        }
        int orderNumber = cursor.getInt(cursor.getColumnIndex(StopWatchContract.MachineEntry.COLUMN_MACHINE_ORDER_NUMBER));
        String name = cursor.getString(cursor.getColumnIndex(StopWatchContract.MachineEntry.COLUMN_MACHINE_NAME));
        int parentProcess = 0;
        boolean enable = cursor.getInt(cursor.getColumnIndex(StopWatchContract.MachineEntry.COLUMN_MACHINE_ENABLE)) == 1;
        return new Machine(id, orderNumber, name, parentProcess, enable);
    }

    @Override
    public void onClick(View v) {
        DialogMachineFragment dialogMachineFragment = new DialogMachineFragment();
        dialogMachineFragment.show(mainActivity.getSupportFragmentManager(), "dialogMachineFragment");
    }
    
    @Override
    public void updateView() {
        machineListAdapter.swapCursor(getAllMachines());
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
        return StopWatchContract.MachineEntry.TABLE_NAME;
    }

    @Override
    public String getRowIdFromDatabase() {
        return StopWatchContract.MachineEntry._ID;
    }


    @Override
    public boolean isMachineSortedByName() {
        return sortedByName;
    }
}
