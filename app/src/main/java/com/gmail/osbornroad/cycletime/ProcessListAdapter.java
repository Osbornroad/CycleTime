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

import com.gmail.osbornroad.cycletime.dao.StopWatchContract.ProcessEntry;
import com.gmail.osbornroad.cycletime.model.Process;


/**
 * Created by User on 27.04.2017.
 */

public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.ProcessViewHolder> {

    final private ListItemClickListener mOnClickListener;
    final private ProcessListAdapter.ListItemLongClickListener mOnLongClickListener;
    private Cursor mCursor;
    private Resources resources;
    private LongItemClickAccessor longItemClickAccessor;


    public ProcessListAdapter(ListItemClickListener mOnClickListener, ListItemLongClickListener mOnLongClickListener,
                              Cursor cursor, Resources resources, LongItemClickAccessor longItemClickAccessor) {
        this.mOnClickListener = mOnClickListener;
        this.mOnLongClickListener = mOnLongClickListener;
        this.mCursor = cursor;
        this.resources = resources;
        this.longItemClickAccessor = longItemClickAccessor;
    }

    interface LongItemClickAccessor {
        boolean isProcessSortedByName();
    }

    interface ListItemClickListener {
        void onListItemClick(Process process);
    }

    interface ListItemLongClickListener {
        void onListItemLongClick(Process process);
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
    public ProcessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.card_process;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ProcessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProcessViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        int id = mCursor.getInt(mCursor.getColumnIndex(ProcessEntry._ID));
        int orderNumber = mCursor.getInt(mCursor.getColumnIndex(ProcessEntry.COLUMN_PROCESS_ORDER_NUMBER));
        String name = mCursor.getString(mCursor.getColumnIndex(ProcessEntry.COLUMN_PROCESS_NAME));
        boolean enable = mCursor.getInt(mCursor.getColumnIndex(ProcessEntry.COLUMN_PROCESS_ENABLE)) == 1;
        holder.itemView.setTag(id);
        holder.bind(id, orderNumber, name, enable);    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ProcessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView listName;
        Process process;
        ImageView reorder;
        ImageView visible;

        public Process getProcess() {
            return process;
        }

        public ProcessViewHolder(View itemView) {
            super(itemView);
            listName = (TextView)itemView.findViewById(R.id.tv_process_name);
            reorder = (ImageView) itemView.findViewById(R.id.element_reorder_process);
            visible = (ImageView) itemView.findViewById(R.id.element_hidden_process);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if (longItemClickAccessor.isProcessSortedByName()) {
                return false;
            }
            mOnLongClickListener.onListItemLongClick(process);
            v.setBackgroundColor(resources.getColor(R.color.colorPrimaryLight));
            return true;
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(process);
        }


        public void bind(int processId, int orderNumber, String processName, boolean processEnable) {
            process = new Process(processId, orderNumber, processName, processEnable);
            listName.setText(processName/* + " order: " + orderNumber*/);
            reorder.setVisibility(longItemClickAccessor.isProcessSortedByName() ? View.INVISIBLE : View.VISIBLE);
            if (process.isEnable()) {
                visible.setVisibility(View.INVISIBLE);
                listName.setTextColor(resources.getColor(R.color.colorPrimary));
            } else {
                visible.setVisibility(View.VISIBLE);
                listName.setTextColor(resources.getColor(android.R.color.darker_gray));
            }
        }
    }
}
