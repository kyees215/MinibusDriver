package com.hkminibus.minibusdriver;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.StopViewHolder> {

    public List<stop_data> mStopList;
    private Context mContext;
    public class StopViewHolder extends RecyclerView.ViewHolder {
        TextView mWaiting;
        //TextView mText;
        TextView mStopName;

        StopViewHolder(View itemView) {
            super(itemView);
            mWaiting = (TextView) itemView.findViewById(R.id.waiting);
            //mText = (TextView) itemView.findViewById(R.id.textView9);
            mStopName = (TextView) itemView.findViewById(R.id.stop_name);
        }
        public void setValues(stop_data mStopList) {
            mWaiting.setText(mStopList.getRank()+" 人在等候");
            if (mStopList.getRank()>0){
                mWaiting.setBackgroundResource(R.drawable.waitingbackground);
            }
            mStopName.setText(mStopList.getName());
        }
    }

    public StopAdapter(Context mContext, List<stop_data> mStopList){
        this.mStopList = mStopList;
        this.mContext = mContext;
    }

    @Override
    public StopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.stop_recycleview,
                parent, false);
        return new StopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StopViewHolder holder, int position) {
        final stop_data mStop = mStopList.get(position);
        holder.mStopName.setText(mStop.getName());
        holder.mWaiting.setText(mStop.getRank()+" 人在等候");
        if (mStop.getRank()>0){
            holder.mWaiting.setBackgroundResource(R.drawable.waitingbackground);
        }
    }

    @Override
    public int getItemCount() {
        return mStopList.size();
    }



}
