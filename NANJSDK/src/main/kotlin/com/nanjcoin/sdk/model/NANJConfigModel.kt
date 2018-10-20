package com.nanjcoin.sdk.model

import com.google.gson.annotations.SerializedName

internal data class NANJConfigModel
(
        @SerializedName("statusCode")
        val status: Int = 0,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("data")
        val data: NANJDataConfig? = null
)

internal data class NANJDataConfig(
        @SerializedName("client_id")
        val clientId: String  = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("eth_address")
        val ethAddress: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("version")
        val version: String = "",
        @SerializedName("appHash")
        val appHash: String = "",
        @SerializedName("env")
        val env: String = "",
        @SerializedName("chain_id")
        val chainId: String = "",
        @SerializedName("smartContracts")
        val smartContracts: Map<String, String> = mutableMapOf(),
        @SerializedName("supportedERC20")
        val erc20s: MutableList<Erc20> = mutableListOf()
)

data class Erc20(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("address")
        val address: String = "",
        @SerializedName("minimumTransferAmount")
        val minimumAmount: Int = 5000,

        @SerializedName("maxFee")
        val maxFee: Int = 5000
)