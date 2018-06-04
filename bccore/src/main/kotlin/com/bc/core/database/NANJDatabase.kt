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
    const val SQL_DATABASE_VERSION = 3
    const val SQL_WALLET_TABLE = "wallet"
    const val SQL_CREATE_WALLET = "CREATE TABLE $SQL_WALLET_TABLE (" +
            "_id TEXT PRIMARY KEY," +
            "_nanj_address TEXT," +
            "_eth_address TEXT," +
            "_private_key TEXT," +
            "_name TEXT)"
}

class NANJDatabase(context: Context) {
    private val db = NANJDatabaseHelper(context)
    private val dbWrite = db.writableDatabase
    private val dbRead = db.readableDatabase

    fun loadWallets(): MutableMap<String, NANJWallet> {
        val walletsCursor = dbRead.query(SQL_WALLET_TABLE, null, null, null, null, null, null)
        val wallets: MutableMap<String, NANJWallet> = mutableMapOf()
        if (walletsCursor.moveToFirst()) {
            do {
                val wallet = NANJWallet().apply {
                    nanjAddress = walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("_nanj_address"))
                    address = walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("_eth_address"))
                    name = walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("_name"))
                    privatekey = walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("_private_key"))
                }
                wallets[wallet.address] = wallet
            } while (walletsCursor.moveToNext())
        }
        walletsCursor.close()
        return wallets
    }

    fun saveWallet(wallet: NANJWallet) {
        val values = ContentValues().apply {
            put("_id", wallet.address)
            put("_eth_address", wallet.address)
            put("_nanj_address", wallet.nanjAddress)
            put("_name", wallet.name)
            put("_private_key", wallet.privatekey)
        }
        dbWrite.insertWithOnConflict(SQL_WALLET_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun removeWallet(wallet: NANJWallet) {
        dbWrite.delete(SQL_WALLET_TABLE, "_eth_address = '${wallet.address}'", null)
    }
}

class NANJDatabaseHelper(context: Context) : SQLiteOpenHelper(context, SQL_DATABASE_NAME, null, SQL_DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_WALLET)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

}
