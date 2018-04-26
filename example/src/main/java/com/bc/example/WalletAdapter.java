package com.bc.example;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bc.core.nanj.NANJWallet;

import java.util.ArrayList;
import java.util.List;

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/19/18
 * ____________________________________
 */
public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletHolder> {

	public interface OnItemClickListener {
		void onItemClick(int position, NANJWallet wallet);
	}

	private List<NANJWallet> nanjWalletList = new ArrayList<>();
	private OnItemClickListener onClickListener;
	
	public void setData(List<NANJWallet> data) {
		this.nanjWalletList = data;
		notifyDataSetChanged();
	}

	public void setOnItemClickListener(OnItemClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	@NonNull
	@Override
	public WalletHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, parent, false);
		return new WalletHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull WalletHolder holder, int position) {
		NANJWallet wallet = nanjWalletList.get(position);
		holder.name.setText(wallet.getName());
		holder.address.setText(wallet.getAddress());
		holder.itemView.setOnClickListener(view ->
			onClickListener.onItemClick(position, wallet)
		);
	}

	@Override
	public int getItemCount() {
		return nanjWalletList.size();
	}

	class WalletHolder extends RecyclerView.ViewHolder{
		
		public AppCompatTextView name;
		public AppCompatTextView address;

		WalletHolder(View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			address = itemView.findViewById(R.id.address);
		}
	}
	
}
