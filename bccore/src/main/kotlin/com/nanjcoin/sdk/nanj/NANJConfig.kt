package com.nanjcoin.sdk.nanj


object NANJConfig {

    const val WHITE_LIST_OWNER = "0x0000000000000000000000000000000000000000"
    const val UNKNOWN_NANJ_WALLET = "0x0000000000000000000000000000000000000000"

    const val NANJCOIN_URL = "https://api.nanjcoin.com"
    const val NANJ_SERVER_ADDRESS = "$NANJCOIN_URL/api/relayTx"
    const val NANJ_SERVER_CONFIG = "$NANJCOIN_URL/api/authorise"
    const val URL_NANJ_RATE = "https://api.coinmarketcap.com/v2/ticker/1/?convert=NANJ"
    const val URL_YEN_RATE = "$NANJCOIN_URL/api/coins/markets"

    const val URL_SERVER = "https://ropsten.infura.io/faF0xSQUt0ezsDFYglOe"
    const val URL_TRANSACTION = "$NANJCOIN_URL/api/tx/list/%s?limit=%s&page=%s&order_by=desc"

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