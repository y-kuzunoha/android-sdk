# NANJCOIN GUILDELINE FOR ANDROID
## Features
- [x] Create NANJ Wallet
- [x] Import NANJ Wallet via Private Key / Key Store
- [x] Export Private Key/ Key Store from a specific NANJ Wallet
- [x] Transfer NANJ Coin
- [x] Transaction history
- [x] Capture Wallet Address via QRCode
- [x] Capture Wallet Address by NFC Tapping
## Requirements
- Tool: [Android studio 3](https://developer.android.com/studio/)
- Minimum sdk support: 18
## Usage
### Initialization
- In Application class, add following lines of code to onCreate() method
```
new NANJWalletManager.Builder()
      .setContext(getApplicationContext())
      .setNANJAppId("AppId")
      .setNANJSecret("SecretKey")
      .build();
```
### Create New NANJ Wallet
In order to create a wallet, call createWallet in NANJWalletManager shared instance.
```
NANJWalletManager.instance.createWallet(NANJCreateWalletListener callback)
```
Once error happend: 
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
### Import Wallet
- By Private Key
```
NANJWalletManager.instance.importWallet(String privateKey, NANJImportWalletListener callback)
```
- By Key store (Use password to unlock keystore)
```
NANJWalletManager.instance.importWallet(String password, String keystore, NANJImportWalletListener callback)
```
+ Error catching
```onImportWalletFailure()```
+ On success
```onImportWalletSuccess()```
### Export Wallet
- Export Private Key
```
NANJWalletManager.instance.exportPrivateKey() return private key or null 
```
Export KeyStore
```
NANJWalletManager.instanc.exportKeystore(Password) return keystore or null 
```
### Transfer NANJ Coin
Use following method in a `NANJWallet` instance to send NANJCOIN
```
sendNANJCoin(String toAddress, String amount, String message, SendNANJCoinListener callback)
```
+ Capture Wallet Address by QR and NFC
 ```
 sendNANJCoinByQrCode(Activity activity)
 ```
 ```
 sendNANJCoinByNfcCode(Activity activity)
 ```
 
 + Once, wallet address is captured 
 - Create handle `WalletHandle walletHandle = new WalletHandle();`
 - In `onActivityResult` method
 ```
 walletHandle.onActivityResult(requestCode, resultCode, data);
 ```
 - The captured wallet address will be returned via
 ```
 walletHandle.setWalletAddressListener(new WalletHandle.WalletAddressListener() {
            @Override
            public void onWalletAddress(String address) {
            }
        });
 ```
## Author
NANJCOIN, support@nanjcoin.com
## License
NANJ SDK is available under the MIT license. See the LICENSE file for more info.