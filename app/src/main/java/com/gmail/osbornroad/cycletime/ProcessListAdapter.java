package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.model.Process;
import com.gmail.osbornroad.cycletime.service.ProcessService;

/**
 * Created by User on 27.04.2017.
 */

public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.ProcessViewHolder> {

    private ProcessService processService;
    private Process[] processes;
    final private ListItemClickListener mOnClickListener;

    public ProcessListAdapter(ProcessService processService, ListItemClickListener mOnClickListener) {
        this.processService = processService;
        this.processes = processService.getProcessArray();
        this.mOnClickListener = mOnClickListener;
    }

    /**
     * Interface ListItemClickListener for clickable of list items
     */
    interface ListItemClickListener {
        void onListItemClick(int clickedProcessId);
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
        holder.bind(processes[position].getId(), processes[position].getProcessName());
    }

    @Override
    public int getItemCount() {
        return processes.length;
    }

    public class ProcessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemProcessName;
        int id;

        public ProcessViewHolder(View itemView) {
            super(itemView);
            listItemProcessName = (TextView)itemView.findViewById(R.id.tv_process_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(id);
        }

        public void bind(int processId, String processName) {
            id = processId;
            listItemProcessName.setText(processName);
        }
    }
}
