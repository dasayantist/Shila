package com.dasayantist.shila;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;


public class ProducerListAdapter extends BaseAdapter {


    Context pContext;
    ArrayList<Producer> temporaryArray;
    ArrayList<Producer> permanentArray;

    public ProducerListAdapter(Context context, ArrayList<Producer> data) {
        this.pContext = context;
        this.temporaryArray = data;
        this.permanentArray = new ArrayList<>();
        this.permanentArray.addAll(data);
    }

    public void refresh(ArrayList<Producer> data){
        permanentArray.clear();
        permanentArray.addAll(data);
    }
    @Override
    public int getCount() {
        return temporaryArray.size();// # of items in your arraylist
    }

    @Override
    public Object getItem(int position) {
        return temporaryArray.get(position);// get the actual movie
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ProducerListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) pContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.producer_item_layout, parent, false);

            viewHolder = new ProducerListAdapter.ViewHolder();
            viewHolder.itemNames = (TextView) convertView.findViewById(R.id.itemNames);
            viewHolder.itemPhone = (TextView) convertView.findViewById(R.id.itemPhome);
            viewHolder.itemLocation = (TextView) convertView.findViewById(R.id.itemLocation);
            viewHolder.itemSpec = (TextView) convertView.findViewById(R.id.itemSpec);
            viewHolder.imgCall = (ImageView) convertView.findViewById(R.id.imgCall);
            viewHolder.imgSMS = (ImageView) convertView.findViewById(R.id.imgSMS);
           // viewHolder.imgLocation = (ImageView) convertView.findViewById(R.id.imgLocation);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProducerListAdapter.ViewHolder) convertView.getTag();
        }
        final Producer producer = temporaryArray.get(position);
        viewHolder.itemNames.setText(producer.getName());
        viewHolder.itemPhone.setText(producer.getPhone());
        viewHolder.itemLocation.setText(producer.getLocation());
        viewHolder.itemSpec.setText(producer.getArea());

        viewHolder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(pContext);
                dialog.setMessage("Do you really want to call?");
                dialog.setTitle("Call?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String phone = producer.getPhone();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                        if (ActivityCompat.checkSelfPermission(pContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        pContext.startActivity(intent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        viewHolder.imgSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = producer.getPhone();
                pContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null)));
            }
        });
        return convertView;
    }

    public void filter(String text){
        text=text.toLowerCase();
        temporaryArray.clear();

        if(text.trim().length()==0) {
            temporaryArray.addAll(permanentArray);
        }
        else {
            Log.d("SEARCH", "perma_array: "+permanentArray.size());
            for (Producer p:permanentArray) {//p.getName().toLowerCase().contains(text)||
                if(p.getLocation().toLowerCase().contains(text) ||
                        p.getArea().toLowerCase().contains(text) ) {
                    temporaryArray.add(p);
                }
            }
            Log.d("SEARCH","COUNT  "+temporaryArray.size());
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView itemNames;
        TextView itemPhone;
        TextView itemLocation;
        TextView itemSpec;
        ImageView imgCall;
        ImageView imgSMS;

    }
}