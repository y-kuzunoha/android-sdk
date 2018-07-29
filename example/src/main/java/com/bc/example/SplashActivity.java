package com.bc.example;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.widget.Toast;

import com.bc.core.model.NANJConfigModel;
import com.bc.core.nanj.NANJConfig;
import com.bc.core.nanj.NANJWalletManager;
import com.bc.core.util.Api;
import com.bc.core.util.NetworkUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ____________________________________
 * <p>
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/19/18
 * ____________________________________
 */
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        findViewById(R.id.btnLogin)
                .setOnClickListener(v -> getNANJConfig());

    }

    private void login(String password) {
        if (!TextUtils.isEmpty(password)) {
            try {
                String sha512Psw = StringUtil.sha512(password);
                if (!sharedPreferences.contains(Const.BUNDLE_KEY_PASSWORD)) {
                    savePassword(sha512Psw);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(Const.BUNDLE_KEY_PASSWORD, password);
                    startActivity(intent);
                    finish();
                } else if (sharedPreferences.contains(Const.BUNDLE_KEY_PASSWORD)
                        && sha512Psw.equalsIgnoreCase(sharedPreferences.getString(Const.BUNDLE_KEY_PASSWORD, ""))) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(Const.BUNDLE_KEY_PASSWORD, password);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Password is not correct.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void savePassword(String psw) {
        sharedPreferences.edit()
                .putString(Const.BUNDLE_KEY_PASSWORD, psw)
                .apply();
    }

    private void getNANJConfig() {
        NetworkUtil.getRetrofit().create(Api.class)
                .getNANJCoinConfig(
                        NANJConfig.NANJ_SERVER_CONFIG
                )
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NANJConfigModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NANJConfigModel responseBody) {
                        if (responseBody.getStatus() == 200) {
                            NANJWalletManager.instance.setConfig(responseBody);
                            NANJWalletManager.instance.setSmartContract();
                            NANJWalletManager.instance.setTxReplay();
                            NANJWalletManager.instance.setErc20(0);
                            String psw = ((AppCompatEditText) findViewById(R.id.inputPassword)).getText().toString();
                            login(psw);
                        } else {
                            Toast.makeText(SplashActivity.this, "Load config fail", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SplashActivity.this, "Load config fail", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
