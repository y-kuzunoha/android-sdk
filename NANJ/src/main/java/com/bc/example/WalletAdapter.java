package com.bc.example;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanjcoin.sdk.nanj.NANJConfig;
import com.nanjcoin.sdk.nanj.NANJWallet;
import com.nanjcoin.sdk.nanj.NANJWalletManager;
import com.nanjcoin.sdk.nanj.listener.GetNANJWalletListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/19/18
 * ____________________________________
 */
public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletHolder> {

	private NANJWalletManager nanjWalletManager;

	WalletAdapter(Context context) {
		nanjWalletManager = NANJWalletManager.instance;
	}

	public interface OnItemClickListener {
		void onItemClick(int position, NANJWallet wallet);
	}
	public interface OnBackupWalletListener {
		void onBackupWalletClick(NANJWallet wallet, boolean isPrivateKey);
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
		if(TextUtils.isEmpty(wallet.getNanjAddress())) {
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					nanjWalletManager.getNANJWalletAsync(wallet.getAddress(), new GetNANJWalletListener() {
						@Override
						public void onError() {
							timer.cancel();
						}

						@Override
						public void onSuccess(String address) {
							if(!Objects.equals(NANJConfig.UNKNOWN_NANJ_WALLET, address)) {
								timer.cancel();
								wallet.setNanjAddress(address);
								holder.address.setText(address);
							}
						}
					});
				}
			}, 0, 10000);
		}
		String nanjAddress = TextUtils.isEmpty(wallet.getNanjAddress()) ? "Initializing …" : wallet.getNanjAddress();
		holder.address.setText(nanjAddress);
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
							onBackupWalletListener.onBackupWalletClick(wallet, true);
						}
						break;
					case R.id.removeWallet:
						if(onRemoveWalletListener != null) {
							onRemoveWalletListener.onRemoveWalletClick(wallet);
						}
						break;
					case R.id.backupKeystore:
						if(onRemoveWalletListener != null) {
							onBackupWalletListener.onBackupWalletClick(wallet, false);
						}
						break;

					case R.id.copyAddress:
						ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
						ClipData clip = ClipData.newPlainText("address", wallet.getNanjAddress());
						clipboard.setPrimaryClip(clip);
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
