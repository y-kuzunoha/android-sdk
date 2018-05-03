package com.bc.core.ui.barcodereader

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import com.bc.core.R
import com.bc.core.nanj.NANJWallet
import com.bc.core.util.PermissionUtil
import com.bc.core.util.uiThread
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_barcode_reader.*
import org.web3j.crypto.WalletUtils


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 3/24/18
 * ____________________________________
 */

class NANJQrCodeActivity : AppCompatActivity() {

	private lateinit var mBarcodeDetector : BarcodeDetector

	private lateinit var mCameraSource : CameraSource
	private var isDetector = false

	override fun onCreate(savedInstanceState : Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_barcode_reader)
		initBarcodeDetection()
		initCamera()
	}

	private fun initBarcodeDetection() {
		mBarcodeDetector = BarcodeDetector.Builder(this)
			.setBarcodeFormats(Barcode.ALL_FORMATS)
			.build()
	}

	override fun onDestroy() {
		mCameraSource.release()
		mBarcodeDetector.release()
		super.onDestroy()
	}

	private fun initCamera() {
		surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
			override fun surfaceChanged(p0 : SurfaceHolder?, p1 : Int, p2 : Int, p3 : Int) {}
			override fun surfaceDestroyed(p0 : SurfaceHolder?) {
				mCameraSource.stop()
			}
			override fun surfaceCreated(p0 : SurfaceHolder?) {
				checkCameraPermission()
			}
		})
	}

	private fun checkCameraPermission() {
		val isPermissionRequestNeeded = PermissionUtil.isPermissionRequestNeeded(this, Manifest.permission.CAMERA)
		if(!isPermissionRequestNeeded) {
			startCamera()
		}
	}

	override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<out String>, grantResults : IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			startCamera()
		}
	}

	@SuppressLint("MissingPermission")
	private fun startCamera() {
		mCameraSource = CameraSource.Builder(this@NANJQrCodeActivity, mBarcodeDetector)
			.setAutoFocusEnabled(true)
			.build()
		mCameraSource.start(surfaceView.holder)
		mBarcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
			override fun release() {}
			override fun receiveDetections(detections : Detector.Detections<Barcode>) {
				val barcodes = detections.detectedItems
				println("barcode detect size -->  ${barcodes.size()}")
				if (barcodes.size() > 0 && isDetector.not()) {
					isDetector = true
//					barcodeReader(barcodes.valueAt(0).displayValue)
					val address = barcodes.valueAt(0).displayValue
					if (WalletUtils.isValidAddress(address)) {
						val intent = Intent()
						intent.putExtra(NANJWallet.WALLET_ADDRESS, address)
						setResult(NANJWallet.QRCODE_RESULT_CODE, intent)
						finish()
					} else {
						isDetector = false
					}
				}
			}
		})
	}

	private fun barcodeReader(info : String) {
		uiThread {
			AlertDialog.Builder(this@NANJQrCodeActivity)
				.setTitle("wallet address")
				.setMessage("Do you want send to this wallet address ?\n$info")
				.setCancelable(false)
				.setNegativeButton("Cancel", { _,_ -> isDetector = false })
				.setPositiveButton("Ok") { _, _ ->
					val intent = Intent()
					intent.putExtra(NANJWallet.WALLET_ADDRESS, info)
					setResult(NANJWallet.QRCODE_RESULT_CODE, intent)
					finish()
				}
				.show()
		}
	}
}

