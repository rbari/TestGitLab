package com.dhakariders.user.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhakariders.R;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Rezwan on 24-Jul-17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.SimpleViewHolder> {


    private Context context;
    private ArrayList<HistoryDTO> historyDTOs;
    private RequestMoreData requestMoreData;
    private String lastRequestedIndex;

    public HistoryAdapter(Context context){
        this.context = context;
        historyDTOs  =  new ArrayList<>();
        lastRequestedIndex = "0";
    }

    public void addHistoryDTOs(Collection<HistoryDTO> historyDTOs){
        this.historyDTOs.addAll(historyDTOs);
        notifyDataSetChanged();
    }


    @Override

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item,parent,false);
        SimpleViewHolder viewHolder = new SimpleViewHolder(view);
        return viewHolder;

    }



    public static class SimpleViewHolder extends RecyclerView.ViewHolder{

        private TextView orderId, driverName, driverNumber, bill, distance;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            orderId =  (TextView)itemView.findViewById(R.id.orderIdTV);
            driverName =  (TextView)itemView.findViewById(R.id.driverNameTV);
            driverNumber =  (TextView)itemView.findViewById(R.id.driverNumberTV);
            bill =  (TextView)itemView.findViewById(R.id.bill);
            distance =  (TextView)itemView.findViewById(R.id.distance);
        }
    }



    @Override

    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.orderId.setText("ID: "+historyDTOs.get(position).orderId);
        holder.driverName.setText(historyDTOs.get(position).driverName);
        holder.driverNumber.setText(historyDTOs.get(position).driverNumber);
        holder.bill.setText(historyDTOs.get(position).bill);
        holder.distance.setText(historyDTOs.get(position).distance);
        if(position == historyDTOs.size()-1 &&  !historyDTOs.get(position).orderId.equals(lastRequestedIndex)){
            lastRequestedIndex  = historyDTOs.get(position).orderId;
            requestMoreData.request(historyDTOs.get(position).orderId);
        }

    }



    @Override

    public int getItemCount() {
        return historyDTOs.size();

    }
    public void setListener(RequestMoreData requestMoreData){
        this.requestMoreData = requestMoreData;
    }

    public interface RequestMoreData{
        void request(String index);
    }

}