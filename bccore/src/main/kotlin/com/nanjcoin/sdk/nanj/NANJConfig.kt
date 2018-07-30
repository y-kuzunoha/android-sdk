package com.nanjcoin.sdk.nanj


object NANJConfig {

    const val WHITE_LIST_OWNER = "0x0000000000000000000000000000000000000000"
    const val UNKNOWN_NANJ_WALLET = "0x0000000000000000000000000000000000000000"

    const val NANJ_SERVER_ADDRESS = "https://api.nanjcoin.com/api/relayTx"
    const val NANJ_SERVER_CONFIG = "https://api.nanjcoin.com/api/authorise"
    const val URL_NANJ_RATE = "https://api.coinmarketcap.com/v2/ticker/1/?convert=NANJ"
    const val URL_YEN_RATE = "http://free.currencyconverterapi.com/api/v5/convert?q=USD_JPY&compact=y"

    const val URL_SERVER = "https://ropsten.infura.io/faF0xSQUt0ezsDFYglOe"
    const val URL_TRANSACTION = "https://api.nanjcoin.com/api/tx/list/%s?limit=%s&page=%s&order_by=desc"

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
}