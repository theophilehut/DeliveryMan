package com.example.thophile.deliveryman;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thophile.deliveryman.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter {
    ArrayList<DeliveryData> deliveries;
    Context context;
    public CustomAdapter(Context context, ArrayList<DeliveryData> deliveries) {
        this.context = context;
        this.deliveries = deliveries;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pastdelivery, parent, false);
// set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        MyViewHolder vh = (MyViewHolder)holder;
        vh.restaurant.setText(deliveries.get(i).getRestaurantAdress());
        vh.customer.setText(deliveries.get(i).getDeliveryAdress());
        Log.d("ADAPTER", "deliveryDisplayed");
    }

    @Override
    public int getItemCount() {

        Log.d("ADAPTER", "Rows to display : " + deliveries.size());
        return deliveries.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView restaurant;// init the item view's
        TextView customer;
        public MyViewHolder(View itemView) {
            super(itemView);
            restaurant = (TextView) itemView.findViewById(R.id.restaurant);
            customer = (TextView) itemView.findViewById(R.id.customer);
        }
    }
}