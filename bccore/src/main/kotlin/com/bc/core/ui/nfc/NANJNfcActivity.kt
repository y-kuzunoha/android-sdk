package com.bc.core.ui.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bc.core.R
import kotlinx.android.synthetic.main.activity_nfc.*

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 3/26/18
 * ____________________________________
 */

class NANJNfcActivity : AppCompatActivity() {

	companion object {
		const val REQUEST_CODE = 0
		const val FLAGS = 0
	}

	private var nfcAdapter : NfcAdapter? = null

	private lateinit var pendingIntent : PendingIntent

	private var filterIntent : Array<IntentFilter> = arrayOf(
		IntentFilter.create(NfcAdapter.ACTION_NDEF_DISCOVERED, "*/*")
	)

	private var mTechLists : Array<Array<String>>? = null

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
		println("nfc read : wwwwwwwwwwwwwwwwwwwwwww")
		nfcInfo.text = intent.toString()
		intent?.let {
			val tag = it.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag
			println("tag -> ${tag.id}")
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

	private fun isSupport() = (nfcAdapter != null)
	private fun isEnabled() = nfcAdapter?.isEnabled ?: false

}