package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.model.Machine;
import com.gmail.osbornroad.cycletime.service.MachineService;


/**
 * Created by User on 28.04.2017.
 */

public class MachineListAdapter extends RecyclerView.Adapter<MachineListAdapter.MachineViewHolder> {

    private MachineService machineService;
    private Machine[] machines;
    final private ListItemClickListener mOnClickListener;

    public MachineListAdapter(MachineService machineService, ListItemClickListener mOnClickListener) {
        this.machineService = machineService;
        this.machines = this.machineService.getMachineArray();
        this.mOnClickListener = mOnClickListener;
    }

    interface ListItemClickListener {
        void onListItemClick(int clickedMachineId);
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
        holder.bind(machines[position].getId(), machines[position].getMachineName());
    }

    @Override
    public int getItemCount() {
        return machines.length;
    }

    class MachineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemMachineName;
        int id;

        public MachineViewHolder(View itemView) {
            super(itemView);
            listItemMachineName = (TextView) itemView.findViewById(R.id.tv_machine_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(id);
        }

        public void bind(int machineId, String machineName) {
            id = machineId;
            listItemMachineName.setText(machineName);
        }
    }
}
