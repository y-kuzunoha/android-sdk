package com.nanjcoin.sdk.nanj


internal object NANJConfig {

    const val NANJCOIN_MAINNET = "https://mainnet.infura.io/faF0xSQUt0ezsDFYglOe/"
    const val NANJCOIN_TESTNET = "https://ropsten.infura.io/faF0xSQUt0ezsDFYglOe"
    const val NANJCOIN_URL_MAINNET = "https://api.nanjcoin.com"
    const val NANJCOIN_URL_TESTNET = "https://staging.nanjcoin.com"
    const val NANJCOIN_ETHERSCAN_TESTNET = "https://ropsten.etherscan.io/tx/"
    const val NANJCOIN_ETHERSCAN_MAINNET = "https://etherscan.io/tx/"

    const val WHITE_LIST_OWNER = "0x0000000000000000000000000000000000000000"
    const val UNKNOWN_NANJ_WALLET = "0x0000000000000000000000000000000000000000"



    @JvmStatic var NANJCOIN_URL = NANJCOIN_URL_MAINNET
    @JvmStatic var NANJ_SERVER_ADDRESS = "$NANJCOIN_URL/api/relayTx"
    @JvmStatic var NANJ_SERVER_CONFIG = "$NANJCOIN_URL/api/authorise"
    @JvmStatic var URL_YEN_RATE = "$NANJCOIN_URL/api/coins/markets"
    @JvmStatic var URL_TRANSACTION = "$NANJCOIN_URL/api/tx/list/%s?limit=%s&page=%s&order_by=desc"
    @JvmStatic var URL_GET_RATE = "$NANJCOIN_URL/api/coin/%s/currency/%s"
    @JvmStatic var URL_ETHERSCAN = "https://etherscan.io/tx/"


    @JvmStatic
    var URL_SERVER = NANJCOIN_MAINNET
    @JvmStatic
    var TX_RELAY_ADDRESS = ""
    @JvmStatic
    var META_NANJCOIN_MANAGER = ""
    @JvmStatic
    var SMART_CONTRACT_ADDRESS = ""
    @JvmStatic
    var NANJWALLET_APP_ID = ""
    @JvmStatic
    var NANJWALLET_SECRET_KEY = ""
    @JvmStatic
    var NANJWALLET_NAME = ""

    @JvmStatic
    var MAX_FEE = 5000

    @JvmStatic
    var MINIMUM_TRANSFER_AMOUNT = 5000

    @JvmStatic fun setDevelopMode(isDevMode : Boolean) {
        NANJCOIN_URL = if(isDevMode) NANJCOIN_URL_TESTNET else NANJCOIN_URL_MAINNET
        URL_ETHERSCAN = if(isDevMode) NANJCOIN_ETHERSCAN_TESTNET else NANJCOIN_ETHERSCAN_MAINNET

        URL_SERVER = if(isDevMode) NANJCOIN_TESTNET else NANJCOIN_MAINNET
        NANJ_SERVER_ADDRESS = "$NANJCOIN_URL/api/relayTx"
        NANJ_SERVER_CONFIG = "$NANJCOIN_URL/api/authorise"
        URL_YEN_RATE = "$NANJCOIN_URL/api/coins/markets"
        URL_TRANSACTION = "$NANJCOIN_URL/api/tx/list/%s?limit=%s&page=%s&order_by=desc"
        URL_GET_RATE = "$NANJCOIN_URL/api/coin/%s/currency/%s"

    }
}