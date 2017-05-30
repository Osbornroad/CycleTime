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
    final private SampleListAdapter.ListItemClickListener mOnClickListener;
    final private SampleListAdapter.ListItemLongClickListener mOnLongClickListener;
    private Cursor mCursor;
    private Resources resources;

    public SampleListAdapter(SampleListAdapter.ListItemClickListener mOnClickListener, SampleListAdapter.ListItemLongClickListener mOnLongClickListener, Cursor mCursor, Resources resources) {
        this.mOnClickListener = mOnClickListener;
        this.mOnLongClickListener = mOnLongClickListener;
        this.mCursor = mCursor;
        this.resources = resources;
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
        int id = mCursor.getInt(0);
        String employeeName = mCursor.getString(4);
        String processName = mCursor.getString(5);
        String machineName = mCursor.getString(6);
        String partName = mCursor.getString(7);
        int quantity = mCursor.getInt(1);
        String startDateTime = mCursor.getString(2);
        int duration = mCursor.getInt(3);
        holder.itemView.setTag(id);
        holder.bind(employeeName, processName, machineName, partName, quantity, startDateTime, duration);
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
        TextView cycleTime;
        TextView startDateTime;

        public SampleViewHolder(View itemView) {
            super(itemView);
            employeeName = (TextView) itemView.findViewById(R.id.sample_employee_name);
            processName = (TextView) itemView.findViewById(R.id.sample_process_name);
            machineName = (TextView) itemView.findViewById(R.id.sample_machine_name);
            partName = (TextView) itemView.findViewById(R.id.sample_part_name);
            quantity = (TextView) itemView.findViewById(R.id.sample_quantity);
            cycleTime = (TextView) itemView.findViewById(R.id.sample_cycletime_result);
            startDateTime = (TextView) itemView.findViewById(R.id.sample_start_date_time);
        }


        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }


        public void bind(String employeeName, String processName, String machineName, String partName, int quantity, String startDateTime, int duration) {
            if (!("".equals(employeeName))) {
                this.employeeName.setText(employeeName);
//                this.employeeName.setTextColor(resources.getColor(R.color.result_exists_data));
            }
            if (!("".equals(processName))) {
                this.processName.setText(processName);
//                this.processName.setTextColor(resources.getColor(R.color.result_exists_data));
            }
            if (!("".equals(machineName))) {
                this.machineName.setText(machineName);
//                this.machineName.setTextColor(resources.getColor(R.color.result_exists_data));
            }
            if (!("".equals(partName))) {
                this.partName.setText(partName);
//                this.partName.setTextColor(resources.getColor(R.color.result_exists_data));
            }
            this.quantity.setText(String.valueOf(quantity));
//            this.quantity.setTextColor(resources.getColor(R.color.result_exists_data));

            this.startDateTime.setText(startDateTime.split(" ")[0]);

            String cycleTimeString = resources.getString(R.string.no_data);
            try {
                int cycleTime = duration % quantity == 0 ? duration / quantity : duration / quantity + 1;
                int hours = (int) (cycleTime / 3600);
                int minutes = (int) ((cycleTime % 3600) / 60);
                int seconds = (int) (cycleTime % 60);
                cycleTimeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            } catch (Exception e) {

            }

            this.cycleTime.setText(cycleTimeString);
        }
    }
}
