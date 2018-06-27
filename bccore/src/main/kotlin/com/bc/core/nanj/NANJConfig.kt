package com.bc.core.nanj

object NANJConfig  {
    const val TX_RELAY_ADDRESS          = "0x0d9275fb19c42cfe403d7a2b7ae55dac4a15bc60"
    const val META_NANJCOIN_MANAGER     = "0x6a1b05e6d219b84184ab76acc373706763e3bcfd"
    const val NANJCOIN_ADDRESS          = "0xf7afb89bef39905ba47f3877e588815004f7c861"
    const val WHITE_LIST_OWNER          = "0x0000000000000000000000000000000000000000"
    const val UNKNOWN_NANJ_WALLET       = "0x0000000000000000000000000000000000000000"

    const val NANJ_SERVER_ADDRESS = "https://nanj-demo.herokuapp.com/api/relayTx"
    const val URL_NANJ_RATE = "https://api.coinmarketcap.com/v2/ticker/1/?convert=NANJ"
    const val URL_YEN_RATE = "http://free.currencyconverterapi.com/api/v5/convert?q=USD_JPY&compact=y"
    const val SMART_CONTRACT_ADDRESS = "0x39d575711bbca97d554f57801de3090afe74dc12"
    const val URL_SERVER = "https://ropsten.infura.io/faF0xSQUt0ezsDFYglOe"
    const val URL_TRANSACTION = "https://api-ropsten.etherscan.io/api?module=account&action=tokentx&contractaddress=%s&address=%s&page=%s&offset=%s&sort=asc&apikey=YourApiKeyToken"

    @JvmStatic var NANJWALLET_APP_ID = "575958089608922877"
    @JvmStatic var NANJWALLET_SECRET_KEY = "fF5MSugBFsUEoTiFIiRdUa1rFc5Y8119JVzyWUzJ"
}