package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract.MachineEntry;
import com.gmail.osbornroad.cycletime.model.Machine;

/**
 * Created by User on 28.04.2017.
 */

public class MachineListAdapter extends RecyclerView.Adapter<MachineListAdapter.MachineViewHolder> {

    final private ListItemClickListener mOnClickListener;
    final private ListItemLongClickListener mOnLongClickListener;
    private Cursor mCursor;
    private Resources resources;
    private LongItemClickAccessor longItemClickAccessor;

    public MachineListAdapter(ListItemClickListener mOnClickListener, ListItemLongClickListener mOnLongClickListener,
                              Cursor cursor, Resources resources, LongItemClickAccessor longItemClickAccessor) {
        this.mOnClickListener = mOnClickListener;
        this.mOnLongClickListener = mOnLongClickListener;
        this.mCursor = cursor;
        this.resources = resources;
        this.longItemClickAccessor = longItemClickAccessor;
    }

    interface LongItemClickAccessor {
        boolean isMachineSortedByName();
    }

    interface ListItemClickListener {
        void onListItemClick(Machine machine);
    }

    interface ListItemLongClickListener {
        void onListItemLongClick(Machine machine);
    }

    void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.card_machine;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MachineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MachineViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        int id = mCursor.getInt(mCursor.getColumnIndex(MachineEntry._ID));
        int orderNumber = mCursor.getInt(mCursor.getColumnIndex(MachineEntry.COLUMN_MACHINE_ORDER_NUMBER));
        String name = mCursor.getString(mCursor.getColumnIndex(MachineEntry.COLUMN_MACHINE_NAME));
        int parentProcessId = mCursor.getInt(mCursor.getColumnIndex(MachineEntry.COLUMN_PARENT_PROCESS_ID));
        boolean enable = mCursor.getInt(mCursor.getColumnIndex(MachineEntry.COLUMN_MACHINE_ENABLE)) == 1;
        holder.itemView.setTag(id);
        holder.bind(id, orderNumber, name, parentProcessId, enable);    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class MachineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView listName;
        Machine machine;
        ImageView reorder;
        ImageView visible;

        public Machine getMachine() {
            return machine;
        }

        public MachineViewHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.tv_machine_name);
            reorder = (ImageView) itemView.findViewById(R.id.element_reorder_machine);
            visible = (ImageView) itemView.findViewById(R.id.element_hidden_machine);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (longItemClickAccessor.isMachineSortedByName()) {
                return false;
            }
            mOnLongClickListener.onListItemLongClick(machine);
            v.setBackgroundColor(resources.getColor(R.color.colorPrimaryLight));
            return true;
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(machine);
        }

        public void bind(int machineId, int orderNumber, String machineName, int parentProcessId, boolean enable) {
            machine = new Machine(machineId, orderNumber, machineName, parentProcessId, enable);
            listName.setText(machineName);
            reorder.setVisibility(longItemClickAccessor.isMachineSortedByName() ? View.INVISIBLE : View.VISIBLE);
            if (machine.isEnable()) {
                visible.setVisibility(View.INVISIBLE);
                listName.setTextColor(resources.getColor(R.color.colorPrimary));
            } else {
                visible.setVisibility(View.VISIBLE);
                listName.setTextColor(resources.getColor(android.R.color.darker_gray));
            }
        }
    }
}
