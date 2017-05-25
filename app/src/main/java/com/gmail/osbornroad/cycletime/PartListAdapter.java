package com.gmail.osbornroad.cycletime;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.osbornroad.cycletime.dao.StopWatchContract.PartsEntry;
import com.gmail.osbornroad.cycletime.model.Part;


/**
 * Created by User on 28.04.2017.
 */

public class PartListAdapter extends RecyclerView.Adapter<PartListAdapter.PartViewHolder> {

    final private ListItemClickListener mOnClickListener;
    final private ListItemLongClickListener mOnLongClickListener;
    private Cursor mCursor;

    public PartListAdapter(ListItemClickListener mOnClickListener, ListItemLongClickListener mOnLongClickListener, Cursor cursor) {
        this.mOnClickListener = mOnClickListener;
        this.mOnLongClickListener = mOnLongClickListener;
        this.mCursor = cursor;
    }

    interface ListItemClickListener {
        void onListItemClick(Part part);
    }

    interface ListItemLongClickListener {
        void onListItemLongClick(Part part);
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
        int id = mCursor.getInt(mCursor.getColumnIndex(PartsEntry._ID));
        String name = mCursor.getString(mCursor.getColumnIndex(PartsEntry.COLUMN_PARTS_NAME));
        boolean enable = mCursor.getInt(mCursor.getColumnIndex(PartsEntry.COLUMN_PARTS_ENABLE)) == 1;
        holder.itemView.setTag(id);
        holder.bind(id, name, enable);    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class PartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView listItemPartView;
        Part part;

        public PartViewHolder(View itemView) {
            super(itemView);
            listItemPartView = (TextView) itemView.findViewById(R.id.tv_part_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnLongClickListener.onListItemLongClick(part);
            v.setBackgroundColor(R.color.colorPrimary);
            return true;        }

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
