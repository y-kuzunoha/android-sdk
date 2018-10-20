# NANJCOIN GUILDELINE FOR ANDROID
## Features
- [x] Create NANJ Wallet
- [x] Import NANJ Wallet via Private Key / Key Store
- [x] Export Private Key/ Key Store from NANJ Wallet
- [x] Transfer NANJ Coin
- [x] Transaction History
- [x] Get NANJ Rate in JPY
- [x] Capture Wallet Address via QRCode
- [x] Capture Wallet Address via NFC Tap

## Requirements
- Tool: [Android studio 3](https://developer.android.com/studio/)
- Minimum Android SDK support: 18

## Communication
- If you **need help** or **ask a general question**, use [Discord](https://discord.gg/xa94m8F). (Channel  'nanj-sdk')
- If you **found a bug**, open an issue.
- If you **have a feature request**, open an issue.
- If you **want to contribute**, submit a pull request.

## Usage

### Installation
Download AAR file at Release page [NANJCOIN SDK Version 1.0](https://github.com/NANJ-COIN/android-sdk/releases). Please do step by step by following intructions. 

### Initialization
- In Application class, add following lines of code to onCreate() method

```kotlin
new NANJWalletManager.Builder()
      .setContext(getApplicationContext())
      .setDevelopmentMode(true)
      .setNANJAppId("AppId")
      .setNANJSecret("SecretKey")
      .build();     
```

In order to test with development environment. Use `setDevelopmentMode` with boolean value. Production mode: `setDevelopmentMode(false)`

### Create New NANJ Wallet
In order to create a wallet, call createWallet in NANJWalletManager shared instance.

```
NANJWalletManager.instance.createWallet(NANJCreateWalletListener callback)
```

Once error raise: 

```
void onCreateWalletFailure()
```

Once wallet created, `private key` will be passed to parametter, at this time, NANJ Address will be generated from remote API.

```
void onCreateProcess(String privateKey)
```

Get NANJWallet address from specific ETH Wallet Address,

```
NANJWalletManager.instance.getNANJWallet(ETH address)
```

**Notes:** Creating wallet now will take 1-2 mins to complete for development mode. It will dispatch a transaction on Ropsten Testnet. If creating wallet on mainnet, it will take much longer.

### Import Wallet
- By Private Key
```
NANJWalletManager.instance.importWallet(String privateKey, NANJImportWalletListener callback)
```

- By Key store (Use password to unlock keystore)
```
NANJWalletManager.instance.importWallet(String password, String keystore, NANJImportWalletListener callback)
```

+ Error catch

```onImportWalletFailure()```

+ On success

```onImportWalletSuccess()```

### Export Wallet
Export Private Key

```
NANJWalletManager.instance.exportPrivateKey() return private key or null 
```

Export KeyStore

```
NANJWalletManager.instance.exportKeystore(Password) return keystore or null 
```

### Transfer NANJ Coin
Use following method in a `NANJWallet` instance to send NANJCOIN

```
wallet.sendNANJCoin(String toAddress, String amount, String message, SendNANJCoinListener callback)
```

To receive an event after invoking `sendNANJCoin`. Below is an example of registering a Listener

```
new SendNANJCoinListener() {
    @Override
    public void onError() {
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

 ```
 wallet.sendNANJCoinByQrCode(Activity activity)
 ```
 ```
 wallet.sendNANJCoinByNfcCode(Activity activity)
 ```
 
 Once, wallet address is captured 
 
 - Create `WalletHandler walletHandler = new WalletHandler();`
 
 - In `onActivityResult` method
 ```
 walletHandler.onActivityResult(requestCode, resultCode, data);
 ```
 
 - The captured wallet address will be returned via
 
 ```
 walletHandler.setWalletAddressListener(new WalletHandler.WalletAddressListener() {
            @Override
            public void onWalletAddress(String address) {
            }
        });
 ```
 
### Get NANJCOIN Rate in JPY

```kotlin
_nanjWalletManager.getNANJRate(new NANJRateListener() {
	@Override
	public void onSuccess(@NonNull BigDecimal value) {
	        nanjRate.setText("Yen: " + realCoin.multiply(value).toBigInteger());
	}
	
	@Override
	public void onFailure(String e) {
	
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

```
interface NANJTransactionsListener {
	fun onTransferSuccess(transactions: DataTransaction?)
	fun onTransferFailure()
}
```

NANJTransaction Object under JSON format

```json
{
    "id": 4,
    "TxHash": "0xb47942a71df96102f74e17254a44736d4977ecc5dec02583162f42409946bf2b",
    "status": 1,
    "created_at": "2018-07-26 13:29:00",
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
```swift 
NANJWalletManager.instance.getErc20List()
```

Result returned will be list of 

```
class Erc20(
    val id: Int = 0,
    val name: String = "",
    val address: String = ""
)
```

To set ERC20/ERC223 by index

```
NANJWalletManager.instance.setErc20(index)
```

This feature allows the SDK can switch among multiple ERC20/ERC223 coins.
At the moment of writing this document, NANJCOIN SDK only support NANJCOIN (ERC223).

## Author
NANJCOIN, support@nanjcoin.com

## License
please read our license at this link. [LICENSE](https://nanjcoin.com/sdk)
you can change Language JP/EN
