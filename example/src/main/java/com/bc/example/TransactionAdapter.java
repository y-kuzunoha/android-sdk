package com.bc.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.core.nanj.NANJConvert;
import com.bc.core.nanj.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    class TransactionViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView title, address, coin;

        TransactionViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            coin = itemView.findViewById(R.id.coin);
        }
    }


    private String address;
    private List<Transaction> transactions = new ArrayList<>();

    TransactionAdapter(String address) {
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setData(List<Transaction> transactions) {
        this.transactions .addAll(transactions);
        notifyDataSetChanged();
    }

    public void clearData() {
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
        Transaction data = transactions.get(position);
        if (data == null) return;
        boolean isSend = Objects.equals(address, data.getFrom());
        holder.title.setText(data.getTokenName());
        if (isSend) {
            holder.address.setText("To: " + data.getTo());
            holder.coin.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
            holder.coin.setText("-" + NANJConvert.INSTANCE.fromWei(data.getValue(), NANJConvert.Unit.NANJ).toPlainString());
        } else {
            holder.address.setText("From: " + data.getFrom());
            holder.coin.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
            holder.coin.setText(NANJConvert.INSTANCE.fromWei(data.getValue(), NANJConvert.Unit.NANJ).toPlainString());
        }
    }

    @Override
    public int getItemCount() {
        return transactions == null ? 0 : transactions.size();
    }
}
