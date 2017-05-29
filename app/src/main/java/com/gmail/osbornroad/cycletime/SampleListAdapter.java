package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 29.05.2017.
 */

public class SampleListAdapter extends RecyclerView.Adapter<SampleListAdapter.SampleViewHolder> {
    final private EmployeeListAdapter.ListItemClickListener mOnClickListener;
    final private EmployeeListAdapter.ListItemLongClickListener mOnLongClickListener;
    private Cursor mCursor;
    private Resources resources;

    public SampleListAdapter(EmployeeListAdapter.ListItemClickListener mOnClickListener, EmployeeListAdapter.ListItemLongClickListener mOnLongClickListener, Cursor mCursor, Resources resources) {
        this.mOnClickListener = mOnClickListener;
        this.mOnLongClickListener = mOnLongClickListener;
        this.mCursor = mCursor;
        this.resources = resources;
    }

    interface ListItemClickListener {
        void onListItemClick();
    }

    interface ListItemLongClickListener {
        void onListItemLongClick();
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.card_sample;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String employeeName;
        String processName;
        String machineName;
        String partName;
        String quantity;
        String duration;
        String startDateTime;
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class SampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView employeeName;
        TextView processName;
        TextView machineName;
        TextView partName;
        TextView quantity;
        TextView duration;
        TextView startDateTime;

        public SampleViewHolder(View itemView) {
            super(itemView);
            employeeName = (TextView) itemView.findViewById(R.id.sample_employee_name);
            processName = (TextView) itemView.findViewById(R.id.sample_process_name);
            machineName = (TextView) itemView.findViewById(R.id.sample_machine_name);
            partName = (TextView) itemView.findViewById(R.id.sample_part_name);
            quantity = (TextView) itemView.findViewById(R.id.sample_quantity);
            duration = (TextView) itemView.findViewById(R.id.sample_duration);
            startDateTime = (TextView) itemView.findViewById(R.id.sample_start_date_time);
        }


        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
