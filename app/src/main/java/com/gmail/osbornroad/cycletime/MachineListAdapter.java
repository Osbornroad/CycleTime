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

    public MachineListAdapter(ListItemClickListener mOnClickListener, ListItemLongClickListener mOnLongClickListener, Cursor cursor, Resources resources) {
        this.mOnClickListener = mOnClickListener;
        this.mOnLongClickListener = mOnLongClickListener;
        this.mCursor = cursor;
        this.resources = resources;
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
        String name = mCursor.getString(mCursor.getColumnIndex(MachineEntry.COLUMN_MACHINE_NAME));
        int parentProcessId = mCursor.getInt(mCursor.getColumnIndex(MachineEntry.COLUMN_PARENT_PROCESS_ID));
        boolean enable = mCursor.getInt(mCursor.getColumnIndex(MachineEntry.COLUMN_MACHINE_ENABLE)) == 1;
        holder.itemView.setTag(id);
        holder.bind(id, name, parentProcessId, enable);    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class MachineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView listName;
        Machine machine;
        ImageView visible;

        public MachineViewHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.tv_machine_name);
            visible = (ImageView) itemView.findViewById(R.id.element_hidden_machine);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnLongClickListener.onListItemLongClick(machine);
            v.setBackgroundColor(resources.getColor(R.color.colorPrimaryLight));
            return true;
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(machine);
        }

        public void bind(int machineId, String machineName, int parentProcessId, boolean enable) {
            machine = new Machine(machineId, machineName, parentProcessId, enable);
            listName.setText(machineName);
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
