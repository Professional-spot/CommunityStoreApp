package com.dzhosef.communitystore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class My_adapter_2 extends RecyclerView.Adapter<My_adapter_2.MyViewHolder>{


    final Context context;

    final ArrayList<Product2> list;



    public My_adapter_2(Context context, ArrayList<Product2> list) {
        this.context = context;
        this.list = list;

       
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item2,viewGroup,false);

        return  new MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Product2 product=list.get(i);
        myViewHolder.products.setText(product.getProduct());
        String get_unit="/"+product.getUnit();
        myViewHolder.textview_unit.setText(get_unit);
        myViewHolder.textview_amount.setText(product.getAmount());
        myViewHolder.name.setText(product.getName());
        myViewHolder.location.setText(product.getLocation());
        String get_price="$"+ product.getPrice();
        myViewHolder.price.setText(get_price);
        long seconds = System.currentTimeMillis() / 1000L-product.getTime();
        String time= seconds / 86400L +"days ago ";
        myViewHolder.last_update.setText(time);

        myViewHolder.mainLayout.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("")
                .setMessage("delete?")
                .setNegativeButton("No",(dialog, which)->{

                })
                .setPositiveButton("Yes", (dialog, which) -> {
                    StoreActivity s=new StoreActivity();
                    s.delete(product.getTask(),product.getUserid());



                })
                .show());


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        final CardView mainLayout;
        final TextView products;
        final TextView name;
        final TextView location;
        final TextView price;
        final TextView last_update;
        final TextView textview_amount;
        final TextView textview_unit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            products=itemView.findViewById(R.id.product_id_2);
            name=itemView.findViewById(R.id.seller_id_2);
            location =itemView.findViewById(R.id.location_id_2);
            textview_unit =itemView.findViewById(R.id.price_unit_2);
            textview_amount =itemView.findViewById(R.id.amount_unit_2);
            price =itemView.findViewById(R.id.price_id_2);
            last_update =itemView.findViewById(R.id.last_update_id_2);
            mainLayout=itemView.findViewById(R.id.mainLayout2);
        }
    }

    public void filter(String text) {

        ArrayList<Product2> list_copy = new ArrayList<>(list);

        if(text.isEmpty()){
            Intent i = new Intent(context, Home.class);
            context.startActivity(i);
        } else{
            text = text.toLowerCase();



            list.clear();

            for(Product2 item: list_copy){
                if(item.userid.toLowerCase().contains(text) ){
                    list.add(item);
                }
            }


        }

        notifyDataSetChanged();
    }
}
