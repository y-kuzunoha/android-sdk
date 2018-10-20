package com.nanjsdk.sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanjcoin.sdk.model.Erc20;

import java.util.ArrayList;
import java.util.List;

public class CoinTypeAdapter extends RecyclerView.Adapter<CoinTypeAdapter.CoinHolder> {
    private OnItemClickListener onItemClickListener;
    private List<Erc20> data = new ArrayList<>();

    interface OnItemClickListener {
        void onItemClicked(Erc20 erc20, int position);
    }

    public void setData(List<Erc20> d) {
        data = d;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_coin, parent, false);
        return new CoinHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) onItemClickListener.onItemClicked(data.get(position), position);
        });
        holder.name.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CoinHolder extends RecyclerView.ViewHolder {
        private TextView name;

        CoinHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
        }
    }
}
