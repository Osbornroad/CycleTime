package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract.MachineEntry;
import com.gmail.osbornroad.cycletime.model.Part;


/**
 * Created by User on 28.04.2017.
 */

public class PartListAdapter extends RecyclerView.Adapter<PartListAdapter.PartViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private Cursor mCursor;

    public PartListAdapter(ListItemClickListener mOnClickListener, Cursor cursor) {
        this.mOnClickListener = mOnClickListener;
        this.mCursor = cursor;
    }

    interface ListItemClickListener {
        void onListItemClick(Part part);
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
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        int id = mCursor.getInt(mCursor.getColumnIndex(MachineEntry._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(MachineEntry.COLUMN_MACHINE_NAME));
        boolean enable = mCursor.getInt(mCursor.getColumnIndex(MachineEntry.COLUMN_MACHINE_ENABLE)) == 1;
        holder.bind(id, name, enable);    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class PartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView listItemPartView;
        Part part;

        public PartViewHolder(View itemView) {
            super(itemView);
            listItemPartView = (TextView) itemView.findViewById(R.id.tv_part_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(part);
        }

        public void bind(int partId, String partName, boolean partEnable) {
            part = new Part(partId, partName, partEnable);
            listItemPartView.setText(partName);
        }
    }
}
