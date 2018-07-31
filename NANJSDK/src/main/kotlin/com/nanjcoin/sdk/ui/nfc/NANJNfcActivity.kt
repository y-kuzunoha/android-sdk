package com.nanjcoin.sdk.ui.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import com.nanjcoin.sdk.R
import android.app.Activity
import android.nfc.tech.Ndef
import com.nanjcoin.sdk.nanj.NANJWallet
import org.web3j.crypto.WalletUtils


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 3/26/18
 * ____________________________________
 */

open class NANJNfcActivity : Activity() {

	companion object {
		const val REQUEST_CODE = 0
		const val FLAGS = 0
	}

	private var nfcAdapter : NfcAdapter? = null

	private lateinit var pendingIntent : PendingIntent

	private var filterIntent : Array<IntentFilter> = arrayOf(
		IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
	)

	private var mTechLists : Array<Array<String>>? = arrayOf(arrayOf(Ndef::class.java.name))

	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_nfc)
		intNfcAdapter()
	}

	private fun intNfcAdapter() {
		pendingIntent  = PendingIntent.getActivity(
		this,
		REQUEST_CODE,
		Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
		FLAGS
		)
		nfcAdapter = NfcAdapter.getDefaultAdapter(this)
	}

	override fun onResume() {
		super.onResume()
		dispatch(true)
	}

	override fun onPause() {
		dispatch(false)
		super.onPause()
	}

	override fun onNewIntent(intent : Intent?) {
		intent?.let {
			val tag = it.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag
			val ndef = Ndef.get(tag)
			/*ndef.connect()
			println("nfc can write ${ndef.isWritable}")
			if(ndef.isWritable) {
				val payload = "0xb66e92f4713de200bc9cb61269a746aa005cbec3"
				val nfcRecord = NdefRecord(NdefRecord.TNF_MIME_MEDIA,ByteArray(0), ByteArray(0), payload.toByteArray())
				val nfcMessage = NdefMessage(arrayOf(nfcRecord))
				ndef.writeNdefMessage(nfcMessage)
				ndef.close()
			}*/


			val ndefMessage = ndef.cachedNdefMessage
			val records = ndefMessage.records
			for (ndefRecord in records) {
					try {
						val payload = ndefRecord.payload
						val s = String(payload)
						if(WalletUtils.isValidAddress(s)) {
							val intent = Intent()
							intent.putExtra(NANJWallet.WALLET_ADDRESS, s)
							setResult(NANJWallet.QRCODE_RESULT_CODE, intent)
							finish()
							return
						}
					} catch (e : Exception) {
						e.printStackTrace()
					}
			}
		}
	}

	private fun dispatch(enable : Boolean) {
		if (enable) {
			nfcAdapter?.enableForegroundDispatch(
				this,
				pendingIntent,
				filterIntent,
				mTechLists
			)
		} else {
			nfcAdapter?.disableForegroundDispatch(this)
		}
	}

//	private fun isSupport() = (nfcAdapter != null)
//	private fun isEnabled() = nfcAdapter?.isEnabled ?: false

}