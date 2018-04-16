@file:Suppress("NOTHING_TO_INLINE")

package com.bc.core.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.annotation.DimenRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import java.util.concurrent.Executors


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 3/24/18
 * ____________________________________
 */

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
private val POOL_EXECUTOR = Executors.newCachedThreadPool()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(function : () -> Unit) {
	IO_EXECUTOR.execute(function)
}

fun poolThread(function : () -> Unit) {
	POOL_EXECUTOR.execute(function)
}

inline fun AppCompatActivity.showToast(string : String) = Toast.makeText(this, string, Toast.LENGTH_LONG).show()
inline fun Fragment.showToast(string : String) = Toast.makeText(context, string, Toast.LENGTH_LONG).show()
inline fun Context.showToast(string : String) = Toast.makeText(this, string, Toast.LENGTH_LONG).show()


/** Extensions for simpler launching of Activities */
inline fun <reified T : Any> Activity.launchActivity(
	requestCode : Int = -1,
	options : Bundle? = null,
	noinline init : Intent.() -> Unit = {}) {

	val intent = newIntent<T>(this)
	intent.init()
	startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity(
	options : Bundle? = null,
	noinline init : Intent.() -> Unit = {}) {

	val intent = newIntent<T>(this)
	intent.init()
	startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context : Context) : Intent = Intent(context, T::class.java)

/** Fragment transaction */
inline fun FragmentManager.inTransaction(func : FragmentTransaction.() -> Unit) {
	val fragmentTransaction = this.beginTransaction()
	fragmentTransaction.func()
	fragmentTransaction.commitAllowingStateLoss()
}

/** SharedPreference */
inline fun Context.sharedPreferences() = getSharedPreferences("base-storage", Context.MODE_PRIVATE)!!
inline fun Activity.sharedPreferences() = (this as Context).sharedPreferences()
inline fun Fragment.sharedPreferences() = context.sharedPreferences()

/**  Converter */
//dip to pixel
inline fun dip(dp : Float) : Int = (Resources.getSystem().displayMetrics.density * dp).toInt()
inline fun dip(dp : Int) : Int = (Resources.getSystem().displayMetrics.density * dp).toInt()
//pixel to dip
inline fun px2dip(px : Int) : Float = px.toFloat() / Resources.getSystem().displayMetrics.density
inline fun dimen(@DimenRes resource: Int): Int = Resources.getSystem().getDimensionPixelSize(resource)
