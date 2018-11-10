package com.nanjsdk.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanjcoin.sdk.nanj.NANJConvert;
import com.nanjcoin.sdk.model.NANJTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private String address;
    private List<NANJTransaction> transactions = new ArrayList<>();


    class TransactionViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView title, address, coin, time, fee;

        TransactionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            coin = itemView.findViewById(R.id.coin);
            time = itemView.findViewById(R.id.time);
            fee = itemView.findViewById(R.id.fee);
        }
    }



    TransactionAdapter(String address) {
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setData(List<NANJTransaction> transactions) {
        this.transactions.addAll(transactions);
        notifyDataSetChanged();
    }

    void clearData() {
        this.transactions.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        NANJTransaction data = transactions.get(position);
        if (data == null) return;
        boolean isSend = Objects.equals(address, data.getFrom());
        Log.d("ahehe", "onBindViewHolder: " + address);
        holder.title.setText(data.getMessage());
        holder.time.setText(android.text.format.DateFormat.format("dd MMM yyyy kk:mm:ss", data.getTimeStamp() * 1000L));
        holder.fee.setText("-" + NANJConvert.fromWei(data.getTxFee(), NANJConvert.Unit.NANJ).toPlainString() + data.getSymbol());
        if (isSend) {
            holder.fee.setVisibility(View.VISIBLE);
            holder.address.setText("To: " + data.getTo());
            holder.coin.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
            holder.coin.setText("-" + NANJConvert.fromWei(data.getValue(), NANJConvert.Unit.NANJ).toPlainString() + data.getSymbol());
        } else {
            holder.fee.setVisibility(View.GONE);
            holder.address.setText("From: " + data.getFrom());
            holder.coin.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.coin.setText(NANJConvert.fromWei(data.getValue(), NANJConvert.Unit.NANJ).toPlainString() + data.getSymbol());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MoreDetailActivity.class);
            intent.putExtra("NANJTransaction", data);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return transactions == null ? 0 : transactions.size();
    }
}
