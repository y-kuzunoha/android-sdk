package com.nanjcoin.sdk.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat


/**
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */


internal object PermissionUtil {
	private fun hasThisPermission(context : Context, permission : String) : Boolean {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
		}
		return true // default
	}

	fun isPermissionRequestNeeded(activity : Activity, permission : String, requestCode: Int = 0) : Boolean {
		if(!hasThisPermission(activity, permission) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			activity.requestPermissions(arrayOf(permission), requestCode)
			return true
		}
		return false // default
	}
}