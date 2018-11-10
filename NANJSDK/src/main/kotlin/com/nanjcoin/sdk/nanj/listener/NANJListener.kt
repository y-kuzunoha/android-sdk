package com.nanjcoin.sdk.nanj.listener

/**
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/24/18
 * ____________________________________
 */

/**
 * NANJ Wallet Creation Listener
 */
interface NANJCreateWalletListener {
    /**
     * on created successfully
     */
	fun onCreatedWalletSuccess(backup: String?)

    /**
     * on created failed
     */
	fun onWalletCreationError()
}

/**
 * NANJ Wallet Import Listener
 */
interface NANJImportWalletListener {
    /**
     * on imported successfully
     */
	fun onImportWalletSuccess()

    /**
     * on imported failed
     */
	fun onImportWalletError()
}