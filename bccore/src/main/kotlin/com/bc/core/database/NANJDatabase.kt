package com.bc.core.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bc.core.database.DatabaseQuery.SQL_CREATE_WALLET
import com.bc.core.database.DatabaseQuery.SQL_DATABASE_NAME
import com.bc.core.database.DatabaseQuery.SQL_DATABASE_VERSION
import com.bc.core.database.DatabaseQuery.SQL_WALLET_TABLE
import com.bc.core.nanj.NANJWallet

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/20/18
 * ____________________________________
 */

object DatabaseQuery {
	const val SQL_DATABASE_NAME = "DB_NAME.db"
	const val SQL_DATABASE_VERSION = 2

	const val SQL_WALLET_TABLE = "wallet"

	const val SQL_CREATE_WALLET = "CREATE TABLE $SQL_WALLET_TABLE (" +
		"_id INTEGER PRIMARY KEY," +
		"_address TEXT," +
		"_wallet TEXT," +
		"_name TEXT)"
	const val SQL_LOAD_WALLET = "SELECT * FROM $SQL_WALLET_TABLE"

}

class NANJDatabase(context : Context) {
	val db = NANJDatabaseHelper(context)
	val dbWrite = db.writableDatabase
	val dbRead = db.readableDatabase

	fun loadWallets() : MutableMap<String, NANJWallet> {
		val walletsCursor = dbRead.query(
			SQL_WALLET_TABLE,
			null,
			null,
			null,
			null,
			null,
			null
		)
		val wallets : MutableMap<String, NANJWallet> = mutableMapOf()
		if (walletsCursor.moveToFirst()) {
			do {
				val wallet = NANJWallet().apply {
					setAddress(walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("_address")))
					setName(walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("_name")))
				}
				wallets[wallet.getAddress()] = wallet
			} while (walletsCursor.moveToNext())
		}
		walletsCursor.close()
		return wallets
	}

	fun saveWallet(wallet : NANJWallet) {
		val values = ContentValues().apply {
			put("_address", wallet.getAddress())
			put("_name", wallet.getName())
		}
		dbWrite.insert(SQL_WALLET_TABLE, null, values)
	}
}

class NANJDatabaseHelper(context : Context) : SQLiteOpenHelper(context, SQL_DATABASE_NAME, null, SQL_DATABASE_VERSION) {
	override fun onCreate(db : SQLiteDatabase) {
		db.execSQL(SQL_CREATE_WALLET)
	}

	override fun onUpgrade(db : SQLiteDatabase, oldVersion : Int, newVersion : Int) {
		
	}

}
