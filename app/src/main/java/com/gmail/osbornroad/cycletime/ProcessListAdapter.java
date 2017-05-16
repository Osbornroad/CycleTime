package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract.ProcessEntry;
import com.gmail.osbornroad.cycletime.model.Process;


/**
 * Created by User on 27.04.2017.
 */

public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.ProcessViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private Cursor mCursor;

    public ProcessListAdapter(ListItemClickListener mOnClickListener, Cursor cursor) {
        this.mOnClickListener = mOnClickListener;
        this.mCursor = cursor;
    }

    /**
     * Interface ListItemClickListener for clickable of list items
     */
    interface ListItemClickListener {
        void onListItemClick(Process process);
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
        String name = mCursor.getString(mCursor.getColumnIndex(ProcessEntry.COLUMN_PROCESS_NAME));
        boolean enable = mCursor.getInt(mCursor.getColumnIndex(ProcessEntry.COLUMN_PROCESS_ENABLE)) == 1;
        holder.bind(id, name, enable);    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ProcessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemProcessName;
        Process process;

        public ProcessViewHolder(View itemView) {
            super(itemView);
            listItemProcessName = (TextView)itemView.findViewById(R.id.tv_process_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(process);
        }

        public void bind(int processId, String processName, boolean processEnable) {
            process = new Process(processId, processName, processEnable);
            listItemProcessName.setText(processName);
        }
    }
}
