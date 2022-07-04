package com.dzhosef.communitystore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class My_adapter extends RecyclerView.Adapter<My_adapter.MyViewHolder>{


    final Context context;

    final ArrayList<Product> list;

    private final int[] backgroundColors = {
            R.color.cardView_color_a,
            R.color.cardView_colors,
            R.color.cardView_color_d,
            R.color.cardView_color_f,
            R.color.cardView_color_g,
            R.color.cardView_color_h,
            R.color.cardView_color_j};

    public My_adapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;

       
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);

        return  new MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Product product=list.get(i);
        myViewHolder.products.setText(product.getProduct());
        int index = i % backgroundColors.length;
        int color = ContextCompat.getColor(context, backgroundColors[index]);
        myViewHolder.uhh.setBackgroundColor(color);

        myViewHolder.mainLayout.setRadius(50);

        String get_unit="/"+product.getUnit();
        myViewHolder.textview_unit.setText(get_unit);
        myViewHolder.textview_amount.setText(product.getAmount());
        myViewHolder.name.setText(product.getName());
        myViewHolder.location.setText(product.getLocation());
        String get_price="$"+ product.getPrice();
        myViewHolder.price.setText(get_price);
        long seconds = System.currentTimeMillis() / 1000L -product.getTime();
        String time= seconds / 86400L +"days ago";
        myViewHolder.last_update.setText(time);

        myViewHolder.mainLayout.setOnClickListener(v -> {

            Intent intent=new Intent(context, ProductClicked.class);
            intent.putExtra("data1",product.getProduct());
            intent.putExtra("data2",product.getName());
            intent.putExtra("data3",String.valueOf(product.getPrice()));
            intent.putExtra("data4",String.valueOf(product.getUserid()));
            intent.putExtra("data5",String.valueOf(product.getTask()));
            intent.putExtra("data6",String.valueOf(product.getAmount()));
            intent.putExtra("data7",String.valueOf(product.getUnit()));

            context.startActivity(intent);
        });


    }

    public int filter(String text) {

        ArrayList<Product> list_copy = new ArrayList<>(list);

        if(text.isEmpty()){

            Intent i = new Intent(context, Home.class);
            context.startActivity(i);
        } else{
            text = text.toLowerCase();



            list.clear();

            for(Product item: list_copy){
                if(item.product.toLowerCase().contains(text) ){
                         list.add(item);
                }
                }


        }

        notifyDataSetChanged();
        return list.size();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        final CardView mainLayout;
        final LinearLayout uhh;
        final TextView products;
        final TextView name;
        final TextView location;
        final TextView price;
        final TextView last_update;
        final TextView textview_amount;
        final TextView textview_unit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            products=itemView.findViewById(R.id.productid);
            name=itemView.findViewById(R.id.sellerid);
            location =itemView.findViewById(R.id.locationid);
                    price =itemView.findViewById(R.id.priceid);
                    last_update =itemView.findViewById(R.id.lastupdateid);
            textview_unit =itemView.findViewById(R.id.priceunit);
            textview_amount =itemView.findViewById(R.id.amountunit);
            mainLayout=itemView.findViewById(R.id.mainLayout);
            uhh=itemView.findViewById(R.id.uhh);
        }
    }
}
