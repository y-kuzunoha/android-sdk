package com.bc.example;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
	public interface OnBackupWalletListener {
		void onBackupWalletClick(NANJWallet wallet);
	}

	public interface OnRemoveWalletListener {
		void onRemoveWalletClick(NANJWallet wallet);
	}

	private List<NANJWallet> nanjWalletList = new ArrayList<>();
	private OnItemClickListener onClickListener;
	private OnBackupWalletListener onBackupWalletListener;
	private OnRemoveWalletListener onRemoveWalletListener;

	public void setData(List<NANJWallet> data) {
		this.nanjWalletList = data;
		notifyDataSetChanged();
	}

	public void setOnItemClickListener(OnItemClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void setOnBackupWalletListener(OnBackupWalletListener onBackupWalletListener) {
		this.onBackupWalletListener = onBackupWalletListener;
	}

	public void setOnRemoveWalletListener(OnRemoveWalletListener onRemoveWalletListener) {
		this.onRemoveWalletListener = onRemoveWalletListener;
	}

	@NonNull
	@Override
	public WalletHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, parent, false);
		return new WalletHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull WalletHolder holder, int position) {
		Context context = holder.itemView.getContext();
		NANJWallet wallet = nanjWalletList.get(position);
		holder.name.setText(wallet.getName());
		holder.address.setText(wallet.getNanjAddress());
		holder.itemView.setOnClickListener(view ->
			onClickListener.onItemClick(position, wallet)
		);
		holder.walletMenu.setOnClickListener(view -> {
			PopupMenu popupMenu = new PopupMenu(context, view);
			popupMenu.getMenuInflater().inflate(R.menu.walelt_menu, popupMenu.getMenu());
			popupMenu.setOnMenuItemClickListener(item -> {
				switch (item.getItemId()) {
					case R.id.backupWallet:
						if(onBackupWalletListener != null) {
							onBackupWalletListener.onBackupWalletClick(wallet);
						}
						break;
					case R.id.removeWallet:
						if(onRemoveWalletListener != null) {
							onRemoveWalletListener.onRemoveWalletClick(wallet);
						}
						break;
				}
				return true;
			});
			popupMenu.show();
		});
	}

	@Override
	public int getItemCount() {
		return nanjWalletList.size();
	}

	class WalletHolder extends RecyclerView.ViewHolder{
		public AppCompatImageView walletMenu;
		public AppCompatTextView name;
		public AppCompatTextView address;

		WalletHolder(View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			address = itemView.findViewById(R.id.address);
			walletMenu = itemView.findViewById(R.id.walletMenu);
		}
	}
	
}
