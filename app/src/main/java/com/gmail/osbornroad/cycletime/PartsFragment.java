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
import com.gmail.osbornroad.cycletime.model.Part;



/**
 * A simple {@link Fragment} subclass.
 */
public class PartsFragment extends Fragment
        implements PartListAdapter.ListItemClickListener,
        PartListAdapter.ListItemLongClickListener,
        NavigationFragment,
        SavableToDatabase,
        PartListAdapter.LongItemClickAccessor,
        View.OnClickListener {

    private static final int NUM_LIST_ITEMS = 100;
    private PartListAdapter partListAdapter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;

    private final int FRAGMENT_ID = 4;

    boolean sortedByName;
    private Paint p = new Paint();

    public static final String PART_PREFERENCE = "PartPreference";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public PartsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sortedByName = false;

        SharedPreferences sharedPreferences = context.getSharedPreferences(PART_PREFERENCE, 0);
        sortedByName = sharedPreferences.getBoolean("sortedByName", sortedByName);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_part_choose, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_part);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_part);
        fab.setOnClickListener(this);
        /**
         * Get adapter, set to RecyclerView
         */

        mainActivity = (MainActivity) getActivity();

        Cursor cursor = getAllParts();
        partListAdapter = new PartListAdapter(this, this, cursor, getResources(), this);
        recyclerView.setAdapter(partListAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                Part part = ((PartListAdapter.PartViewHolder) viewHolder).getPart();
                int id = part.getId();

                Part targetPart = ((PartListAdapter.PartViewHolder) target).getPart();
                int targetOrder = targetPart.getOrderNumber();

                if (fromPos < toPos) {
                    for (int i = fromPos + 1; i <= toPos; i++) {
                        Part prt = ((PartListAdapter.PartViewHolder)recyclerView.findViewHolderForAdapterPosition(i)).getPart();
                        int prtId = prt.getId();
                        mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.PartsEntry.TABLE_NAME +
                                " SET " + StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER + " = " +
                                StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER + " - " + 1 + " WHERE " +
                                StopWatchContract.PartsEntry._ID + " = " + prtId);
                    }
                    mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.PartsEntry.TABLE_NAME +
                            " SET " + StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER + " = " +
                            targetOrder  + " WHERE " +
                            StopWatchContract.PartsEntry._ID + " = " + id);
                } else {
                    for (int i = toPos; i < fromPos; i++) {
                        Part prt = ((PartListAdapter.PartViewHolder)recyclerView.findViewHolderForAdapterPosition(i)).getPart();
                        int prtId = prt.getId();
                        mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.PartsEntry.TABLE_NAME +
                                " SET " + StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER + " = " +
                                StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER + " + " + 1 + " WHERE " +
                                StopWatchContract.PartsEntry._ID + " = " + prtId);
                    }
                    mainActivity.mDb.execSQL("UPDATE " + StopWatchContract.PartsEntry.TABLE_NAME +
                            " SET " + StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER + " = " +
                            targetOrder /*+ " + " + 1*/ + " WHERE " +
                            StopWatchContract.PartsEntry._ID + " = " + id);
                }

                partListAdapter.notifyItemMoved(fromPos, toPos);

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
                    Part part = getPartById(id);
                    DialogPartFragment dialogPartFragment = new DialogPartFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("partSelectedForEdit", part);
                    dialogPartFragment.setArguments(bundle);
                    dialogPartFragment.show(mainActivity.getSupportFragmentManager(), "dialogPartFragment");
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
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences(PART_PREFERENCE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sortedByName", sortedByName);
        editor.commit();
    }

    @Override
    public void onListItemClick(Part part) {
        if(mainActivity.longClickPartSelected != null) {
            return;
        }
        mainActivity.selectedPart = part;
        mainActivity.switchFragment(StopWatchFragment.class, getResources().getString(R.string.stopwatch_fragment_title));
    }

    @Override
    public void onListItemLongClick(Part part) {
/*        if (mainActivity.mActionMode != null) {
            return;
        }
        mainActivity.longClickPartSelected = part;
        mainActivity.mActionMode = mainActivity.startSupportActionMode(mainActivity.mActionModeCallBack);*/
    }

    @Override
    public int getMenuId() {
        return FRAGMENT_ID;
    }

    private Cursor getAllParts() {
        return mainActivity.mDb.query(
                StopWatchContract.PartsEntry.TABLE_NAME,
                null,
                StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE + " = ? OR ?",
                mainActivity.showAll ? new String[]{"0", "1"} : new String[]{"1"},
                null,
                null,
                sortedByName ?
                        StopWatchContract.PartsEntry.COLUMN_PARTS_NAME :
                        StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER
        );
    }

    Part getPartById(int id) {
        Cursor cursor = mainActivity.mDb.query(
                StopWatchContract.PartsEntry.TABLE_NAME,
                null,
                StopWatchContract.PartsEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        if (!cursor.moveToFirst()) {
            return null;
        }
        int orderNumber = cursor.getInt(cursor.getColumnIndex(StopWatchContract.PartsEntry.COLUMN_PARTS_ORDER_NUMBER));
        String name = cursor.getString(cursor.getColumnIndex(StopWatchContract.PartsEntry.COLUMN_PARTS_NAME));
        boolean enable = cursor.getInt(cursor.getColumnIndex(StopWatchContract.PartsEntry.COLUMN_PARTS_ENABLE)) == 1;
        return new Part(id, orderNumber, name, enable);    }

    @Override
    public void onClick(View v) {
        DialogPartFragment dialogPartFragment = new DialogPartFragment();
        dialogPartFragment.show(mainActivity.getSupportFragmentManager(), "dialogPartFragment");
    }

    @Override
    public void updateView() {
        partListAdapter.swapCursor(getAllParts());
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
        return StopWatchContract.PartsEntry.TABLE_NAME;
    }

    @Override
    public String getRowIdFromDatabase() {
        return StopWatchContract.PartsEntry._ID;
    }

    @Override
    public boolean isPartSortedByName() {
        return sortedByName;
    }
}
