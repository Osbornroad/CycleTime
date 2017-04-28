package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.model.Part;
import com.gmail.osbornroad.cycletime.service.PartService;


/**
 * Created by User on 28.04.2017.
 */

public class PartListAdapter extends RecyclerView.Adapter<PartListAdapter.PartViewHolder> {

    private PartService partService;
    private Part[] parts;
    final private ListItemClickListener mOnClickListener;

    public PartListAdapter(PartService partService, ListItemClickListener mOnClickListener) {
        this.partService = partService;
        this.parts = this.partService.getPartArray();
        this.mOnClickListener = mOnClickListener;
    }

    interface ListItemClickListener {
        void onListItemClick(int clickedPartId);
    }

    @Override
    public PartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.card_part;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new PartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PartViewHolder holder, int position) {
        holder.bind(parts[position].getId(), parts[position].getPartName());
    }

    @Override
    public int getItemCount() {
        return parts.length;
    }

    class PartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemPartView;
        int id;

        public PartViewHolder(View itemView) {
            super(itemView);
            listItemPartView = (TextView) itemView.findViewById(R.id.tv_part_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(id);
        }

        public void bind(int partId, String partName) {
            id = partId;
            listItemPartView.setText(partName);
        }
    }
}
