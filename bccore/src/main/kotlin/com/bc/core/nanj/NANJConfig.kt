package com.bc.core.nanj

object NANJConfig  {
    const val TX_RELAY_ADDRESS          = "0x7e861e36332693f271c66bdab40cda255a50d005"
    const val META_NANJCOIN_MANAGER     = "0x8801307aec8ed852ea23d3d4e69f475f4f2dcb6e"
    const val NANJCOIN_ADDRESS          = "0xf7afb89bef39905ba47f3877e588815004f7c861"
    const val WHITE_LIST_OWNER          = "0x0000000000000000000000000000000000000000"
    const val UNKNOWN_NANJ_WALLET       = "0x0000000000000000000000000000000000000000"

    const val NANJ_SERVER_ADDRESS = "https://staging.nanjcoin.com/api/relayTx"
    const val NANJ_SERVER_CONFIG = "https://staging.nanjcoin.com/api/authorise"
    const val URL_NANJ_RATE = "https://api.coinmarketcap.com/v2/ticker/1/?convert=NANJ"
    const val URL_YEN_RATE = "http://free.currencyconverterapi.com/api/v5/convert?q=USD_JPY&compact=y"
    const val SMART_CONTRACT_ADDRESS = "0xf7afb89bef39905ba47f3877e588815004f7c861"
    const val URL_SERVER = "https://ropsten.infura.io/faF0xSQUt0ezsDFYglOe"
    const val URL_TRANSACTION = "https://api-ropsten.etherscan.io/api?module=account&action=tokentx&contractaddress=%s&address=%s&page=%s&offset=%s&sort=asc&apikey=YourApiKeyToken"

    @JvmStatic var NANJWALLET_APP_ID = ""
    @JvmStatic var NANJWALLET_SECRET_KEY = ""
}