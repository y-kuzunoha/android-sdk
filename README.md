# NANJCOIN SDK FOR ANDROID

## Features

-   [x]  Create NANJ Wallet
-   [x]  Import NANJ Wallet via Private Key / Key Store
-   [x]  Export Private Key/ Key Store from NANJ Wallet
-   [x]  Transfer NANJ Coin
-   [x]  Transaction History
-   [x]  Get ERC20/ERC223 Token Rate in JPY or USD
-   [x]  Capture Wallet Address via QRCode
-   [x]  Capture Wallet Address via NFC Tap
-   [x]  Get max fee amount, minimum amount on specific ERC20 Token on
    transfer ERC20 token  

## Requirements

-   Tool: [Android studio 3](https://developer.android.com/studio/)
-   Minimum Android SDK support: 18

## Communication

-   If you **need help** or **ask a general question**, use
    [Discord](https://discord.gg/xa94m8F). (Channel  'nanj-sdk')
-   If you **found a bug**, open an issue.
-   If you **have a feature request**, open an issue.
-   If you **want to contribute**, submit a pull request.

## Usage

### Installation

Add following line of code to your dependencies in gradle file

`api 'com.nanjcoin.sdk:NANJSDK:1.1.0'`

Add following lines of code for NANJ SDK Dependencies
`api 'io.reactivex.rxjava2:rxjava:2.1.9'`
`api 'io.reactivex.rxjava2:rxandroid:2.0.2'`
`api('org.web3j:core:3.3.1-android')`

`api 'com.squareup.retrofit2:retrofit:2.4.0'`

`api 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'`

`api 'com.squareup.retrofit2:converter-gson:2.3.0'`



```
api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version") {
	exclude group: 'org.jetbrains', module: 'annotations'
}
```

```gradle
api("org.jetbrains.anko:anko-common:$anko_version") {
    exclude group: 'org.jetbrains', module: 'annotations'
}
```



Please do step by step by following instructions.

### Initialization

-   Configuration settings, In Application class, add following lines of
    code to onCreate() method

```kotlin
new NANJWalletManager.Builder()
      .setContext(getApplicationContext())
      .setDevelopmentMode(true)
      .setNANJAppId("AppId")
      .setNANJSecret("SecretKey")
      .build();     
```

-   Set master password

```java
NANJWalletManager.instance.setMasterPassword(masterPassword);
```

-   Initialize the SDK after setting master password

```java
NANJWalletManager.instance.initialize(new NANJInitializationListener() {
            @Override
            public void onError() {
                // handle error here...
            }

            @Override
            public void onSuccess() {
                // handle successful Installation here...
            }
        });
```

In order to test with development environment. Use `setDevelopmentMode`
with boolean value.

Production mode: `setDevelopmentMode(false)`

***Notes:*** *Please  ensure that your AppId and SecretKey were approved by*
*NANJ Team. In case of concerning about this, please drop an email to*
*support@nanjcoin.com. For production approval, it would take longer time than usual.*

### Create New NANJ Wallet

In order to create a wallet, call createWallet in `
shared instance.

`NANJWalletManager.instance.createWallet(NANJCreateWalletListener callback)`

Once error raise:

`void onWalletCreationError()`

Once wallet created, `private key` will be called back by parameters, at this
time.

`void onCreatedWalletSuccess(String privateKey)`

At the call back above, this is not final step of wallet creation. The created wallet metioned above is local Ethereum wallet (technically it is a Ethereum keypair). Whenver SDK creates a wallet, it will take a few minutes to interact with Ethereum network to create NANJ Address for 

Get NANJWallet address from specific ETH Wallet Address. In Wallet Class, SDK has 2 properties:

`nanjAddress` : created NANJ Address for sending and receving NANJ

`address`: Ethereum wallet address.

In order to retreive NANJ Address, developer can get NANJWallet from Ethereum wallet address

`NANJWalletManager.instance.getNANJWallet(ETH address)`

***Notes:*** *Creating wallet now will take 1-2 mins to complete for*
*development mode. It will dispatch a transaction on Ethereum Blockchain. If*
*creating wallet in production mode, it will take much longer (&lt; 10*
*mins)*

### Import Wallet

-   By Private Key

`NANJWalletManager.instance.importWallet(String privateKey, NANJImportWalletListener callback)`

-   By Key store (Use password to unlock keystore)
`NANJWalletManager.instance.importWallet(String password, String keystore, NANJImportWalletListener callback)`

-   Error catch 
`onImportWalletError()`

-   On success `onImportWalletSuccess()`

### Export Wallet

-   Export Private Key

    ```java
    String privateKey = NANJWalletManager.instance.exportPrivateKey();
    ```

-   Export KeyStore

    ```java
    String ks = NANJWalletManager.instance.exportKeystore(Password);
    ```

### Transfer NANJ Coin

Use following method in a `NANJWallet` instance to send NANJCOIN

`wallet.sendNANJCoin(String toAddress, String amount, String message, SendNANJCoinListener callback)`

To receive an event after invoking `sendNANJCoin`. Below is an example of registering a Listener

```java
 new SendNANJCoinListener() {
     @Override
     public void onWalletCreationError() {
         runOnUiThread(() -> {
                     loading.dismiss();
                     status.setText("Failure!");
                 }
         );
     }

     @Override
     public void onSuccess() {
         runOnUiThread(() -> {
             status.setText("Sent success!");
             loading.dismiss();
         });
     }
 } 
```

Capture Wallet Address by QR and NFC

`wallet.sendNANJCoinByQrCode(Activity activity)`

`wallet.sendNANJCoinByNfcCode(Activity activity)`

Once, wallet address is captured

-   Create `WalletHandler walletHandler = new WalletHandler();`

-   In `onActivityResult` method

`walletHandler.onActivityResult(requestCode, resultCode, data);`

-   The captured wallet address will be returned via

```java 
walletHandler.setWalletAddressListener(new WalletHandler.WalletAddressListener() {          	@Override                        
	public void onWalletAddress(String address) {
	
	}
});
```

### Get ERC20/ERC223 Token Rate in JPY or USD

Get Rate  (`usd`: US dollar, `jpy`: Japanese Yen)

```kotlin
fun getNANJRate(coinName : String, currencySymbol : String, listener: NANJRateListener)
```

```java
_nanjWalletManager.getNANJRate("nanj","usd",new NANJRateListener() {         	
	@Override
	public void onSuccess(@NonNull BigDecimal value) {
	        // handle onSuccess
	}

	@Override
	public void onFailure(String e) {
    // handle onFailure
	}
});
```

Or implement NANJRateListener

```kotlin
interface NANJRateListener {
    fun onSuccess(values : BigDecimal)
    fun onFailure(e : String)
}
```


### Get transaction list

Use `NANJWallet` instance to call the following method

```kotlin
getTransactions(page: Int, offset: Int = 20, listener: NANJTransactionsListener)
```

To capture the data returned, implement a NANJTransactionsListener:

```kotlin
interface NANJTransactionsListener {
    	fun onLoadedTransactions(transactions: DataTransaction?)
    	fun onFailure()
}
```

NANJTransaction Object under JSON format

```json
{
    "id": 4,
    "TxHash":
"0xb47942a71df96102f74e17254a44736d4977ecc5dec02583162f42409946bf2b",   
 "status": 1,     "created_at": "2018-07-26 13:29:00",
    "symbol": "NANJT",
    "from": "0xd6ea2eeac84c771327eaf7868f65fe984f985f8c",
    "to": "0xb8d5c2b945721f1e37a03c946ca2497f9e2f9775",
    "value": "5500000000",
    "message": "",
    "tx_fee": "55000000",
    "time_stamp": 1532579340
}
```

### NANJ SDK Support mutiple ERC20/ERC223

To retreive supported ERC20/ERC223

```java
NANJWalletManager.instance.getErc20List()
```

Result returned will be list of

```kotlin
    class Erc20(
        val id: Int = 0,
        val name: String = "",
        val address: String = "",
        val minimumAmount: Int = 5000,
        val maxFee: Int = 5000
    )
```

To set ERC20/ERC223 by index

```java
    // Set ERC20 token by index
    NANJWalletManager.instance.setErc20(index);

    // Get current ERC20 Token
    NANJWalletManager.instance.getCurrentErc20(index);
```

This feature allows the SDK to switch among multiple ERC20/ERC223
coins. At the moment of writing this document, NANJCOIN SDK only support
NANJCOIN (ERC223).

### Get max fee and minimum amount for an ERC20/ERC223 Token

Using NANJ SDK, user have to pay 1% of transfer amount, but our sdk limits
max fee is 5,000, that means when user pay for a transaction fee which
is greater than 5,000. User would pay only 5,000 only for transaction
fee.

In the SDK, we limit a minimum amount for transfer. Currently we set
5,000 for NANJCOIN.

```java
// Get current ERC20 Token
Erc20 currentToken = NANJWalletManager.instance.getCurrentErc20(index);
Int minAmount = currentToken.minimumAmount;
Int maxFee = currentToken.maxFee;
```

## Author

NANJCOIN, support@nanjcoin.com

## License

please read our license at this link.
[LICENSE](https://nanjcoin.com/sdk) you can change Language JP/EN
