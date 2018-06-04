package com.bc.core.nanj

object NANJConfig  {
    const val NANJ_SERVER_ADDRESS = "https://nanj-demo.herokuapp.com/api/relayTx"
    const val URL_NANJ_RATE = "https://api.coinmarketcap.com/v2/ticker/1/?convert=NANJ"
    const val URL_YEN_RATE = "http://free.currencyconverterapi.com/api/v5/convert?q=USD_JPY&compact=y"
    private const val API_KEY = "1Sxab6iBbbiFHwtnbZfO"
    const val SMART_CONTRACT_ADDRESS = "0x39d575711bbca97d554f57801de3090afe74dc12"
    const val URL_SERVER = "https://rinkeby.infura.io/$API_KEY"
    const val URL_TRANSACTION = "http://api-rinkeby.etherscan.io/api?module=account&action=tokentx&contractaddress=%s&address=%s&page=%s&offset=%s&sort=desc&apikey=$API_KEY"
}