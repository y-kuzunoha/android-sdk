package com.nanjcoin.sdk.smartcontract


import java.math.BigInteger
import java.util.ArrayList
import java.util.Arrays
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.abi.datatypes.generated.Uint8
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import rx.Observable

/**
 *
 * Auto generated code.
 *
 * **Do not modify!**
 *
 * Please use the [web3j command line tools](https://docs.web3j.io/command_line.html),
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * [codegen module](https://github.com/web3j/web3j/tree/master/codegen) to update.
 *
 *
 * Generated with web3j version 3.4.0.
 */
class TxRelay : Contract {

    protected constructor(contractAddress: String, web3j: Web3j, credentials: Credentials, gasPrice: BigInteger, gasLimit: BigInteger) : super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit) {}

    protected constructor(contractAddress: String, web3j: Web3j, transactionManager: TransactionManager, gasPrice: BigInteger, gasLimit: BigInteger) : super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit) {}

    fun whitelist(param0: String, param1: String): RemoteCall<Boolean> {
        val function = Function(FUNC_WHITELIST,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.Address(param0),
                        org.web3j.abi.datatypes.Address(param1)),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Bool>() {

                }))
        return executeRemoteCallSingleValueReturn(function, Boolean::class.java)
    }

    fun getRelayedMetaTxEvents(transactionReceipt: TransactionReceipt): List<RelayedMetaTxEventResponse> {
        val valueList = extractEventParametersWithLog(RELAYEDMETATX_EVENT, transactionReceipt)
        val responses = ArrayList<RelayedMetaTxEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = RelayedMetaTxEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.sigV = eventValues.nonIndexedValues[0].value as BigInteger
            typedResponse.sigR = eventValues.nonIndexedValues[1].value as ByteArray
            typedResponse.sigS = eventValues.nonIndexedValues[2].value as ByteArray
            typedResponse.destination = eventValues.nonIndexedValues[3].value as String
            typedResponse.data = eventValues.nonIndexedValues[4].value as ByteArray
            typedResponse.listOwner = eventValues.nonIndexedValues[5].value as String
            responses.add(typedResponse)
        }
        return responses
    }

    fun relayedMetaTxEventObservable(filter: EthFilter): Observable<RelayedMetaTxEventResponse> {
        return web3j.ethLogObservable(filter).map { log ->
            val eventValues = extractEventParametersWithLog(RELAYEDMETATX_EVENT, log)
            val typedResponse = RelayedMetaTxEventResponse()
            typedResponse.log = log
            typedResponse.sigV = eventValues.nonIndexedValues[0].value as BigInteger
            typedResponse.sigR = eventValues.nonIndexedValues[1].value as ByteArray
            typedResponse.sigS = eventValues.nonIndexedValues[2].value as ByteArray
            typedResponse.destination = eventValues.nonIndexedValues[3].value as String
            typedResponse.data = eventValues.nonIndexedValues[4].value as ByteArray
            typedResponse.listOwner = eventValues.nonIndexedValues[5].value as String
            typedResponse
        }
    }

    fun relayedMetaTxEventObservable(startBlock: DefaultBlockParameter, endBlock: DefaultBlockParameter): Observable<RelayedMetaTxEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(RELAYEDMETATX_EVENT))
        return relayedMetaTxEventObservable(filter)
    }

    fun getLogAddressEvents(transactionReceipt: TransactionReceipt): List<LogAddressEventResponse> {
        val valueList = extractEventParametersWithLog(LOGADDRESS_EVENT, transactionReceipt)
        val responses = ArrayList<LogAddressEventResponse>(valueList.size)
        for (eventValues in valueList) {
            val typedResponse = LogAddressEventResponse()
            typedResponse.log = eventValues.log
            typedResponse.addr = eventValues.nonIndexedValues[0].value as String
            typedResponse.claimedSender = eventValues.nonIndexedValues[1].value as String
            typedResponse.h = eventValues.nonIndexedValues[2].value as ByteArray
            responses.add(typedResponse)
        }
        return responses
    }

    fun logAddressEventObservable(filter: EthFilter): Observable<LogAddressEventResponse> {
        return web3j.ethLogObservable(filter).map { log ->
            val eventValues = extractEventParametersWithLog(LOGADDRESS_EVENT, log)
            val typedResponse = LogAddressEventResponse()
            typedResponse.log = log
            typedResponse.addr = eventValues.nonIndexedValues[0].value as String
            typedResponse.claimedSender = eventValues.nonIndexedValues[1].value as String
            typedResponse.h = eventValues.nonIndexedValues[2].value as ByteArray
            typedResponse
        }
    }

    fun logAddressEventObservable(startBlock: DefaultBlockParameter, endBlock: DefaultBlockParameter): Observable<LogAddressEventResponse> {
        val filter = EthFilter(startBlock, endBlock, getContractAddress())
        filter.addSingleTopic(EventEncoder.encode(LOGADDRESS_EVENT))
        return logAddressEventObservable(filter)
    }

    fun relayMetaTx(sigV: BigInteger, sigR: ByteArray, sigS: ByteArray, destination: String, data: ByteArray, listOwner: String): RemoteCall<TransactionReceipt> {
        val function = Function(
                FUNC_RELAYMETATX,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.generated.Uint8(sigV),
                        org.web3j.abi.datatypes.generated.Bytes32(sigR),
                        org.web3j.abi.datatypes.generated.Bytes32(sigS),
                        org.web3j.abi.datatypes.Address(destination),
                        org.web3j.abi.datatypes.DynamicBytes(data),
                        org.web3j.abi.datatypes.Address(listOwner)),
                emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun getAddress(b: ByteArray): RemoteCall<String> {
        val function = Function(FUNC_GETADDRESS,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.DynamicBytes(b)),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Address>() {

                }))
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun getNonce(add: String): RemoteCall<BigInteger> {
        val function = Function(FUNC_GETNONCE,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.Address(add)),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Uint256>() {

                }))
        return executeRemoteCallSingleValueReturn(function, BigInteger::class.java)
    }

    fun addToWhitelist(sendersToUpdate: List<String>): RemoteCall<TransactionReceipt> {
        val function = Function(
                FUNC_ADDTOWHITELIST,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.DynamicArray(
                        org.web3j.abi.Utils.typeMap(sendersToUpdate, org.web3j.abi.datatypes.Address::class.java))),
                emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun removeFromWhitelist(sendersToUpdate: List<String>): RemoteCall<TransactionReceipt> {
        val function = Function(
                FUNC_REMOVEFROMWHITELIST,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.DynamicArray(
                        org.web3j.abi.Utils.typeMap(sendersToUpdate, org.web3j.abi.datatypes.Address::class.java))),
                emptyList())
        return executeRemoteCallTransaction(function)
    }

    class RelayedMetaTxEventResponse {
        var log: Log? = null

        var sigV: BigInteger? = null

        var sigR: ByteArray? = null

        var sigS: ByteArray? = null

        var destination: String? = null

        var data: ByteArray? = null

        var listOwner: String? = null
    }

    class LogAddressEventResponse {
        var log: Log? = null

        var addr: String? = null

        var claimedSender: String? = null

        var h: ByteArray? = null
    }

    companion object {

        private val GAS_PRICE = BigInteger.valueOf(20_000_000L)
        private val GAS_LIMIT = BigInteger.valueOf(1_000_000L)

        private val BINARY = "0x608060405234801561001057600080fd5b50610bf4806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632d0335ab1461007d578063548db174146100d45780637f6497831461013a578063b092145e146101a0578063c3f44c0a1461021b578063c47cf5de146102ed575b600080fd5b34801561008957600080fd5b506100be600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610396565b6040518082815260200191505060405180910390f35b3480156100e057600080fd5b50610138600480360381019080803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091929192905050506103de565b005b34801561014657600080fd5b5061019e600480360381019080803590602001908201803590602001908080602002602001604051908101604052809392919081815260200183836020028082843782019150505050505091929192905050506103ec565b005b3480156101ac57600080fd5b50610201600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103fa565b604051808215151515815260200191505060405180910390f35b34801561022757600080fd5b506102eb600480360381019080803560ff16906020019092919080356000191690602001909291908035600019169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610429565b005b3480156102f957600080fd5b50610354600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610ac2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b6103e9816000610afc565b50565b6103f7816001610afc565b50565b60016020528160005260406000206020528060005260406000206000915091509054906101000a900460ff1681565b60008060007fb09956932555b3e322f575a5e5b679b569ff8ad8acdc74ea47511440732a04fa898989898989604051808760ff1660ff168152602001866000191660001916815260200185600019166000191681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828103825284818151815260200191508051906020019080838360005b83811015610521578082015181840152602081019050610506565b50505050905090810190601f16801561054e5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a160008473ffffffffffffffffffffffffffffffffffffffff16148061060b5750600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff165b151561061657600080fd5b61061f85610ac2565b925060197f01000000000000000000000000000000000000000000000000000000000000000260007f01000000000000000000000000000000000000000000000000000000000000000230866000808873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020548a8a60405180887effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19168152600101877effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff19167effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191681526001018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c010000000000000000000000000281526014018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c010000000000000000000000000281526014018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c0100000000000000000000000002815260140182805190602001908083835b60208310151561083e5780518252602082019150602081019050602083039250610819565b6001836020036101000a038019825116818451168082178552505050505050905001975050505050505050604051809103902091506001828a8a8a604051600081526020016040526040518085600019166000191681526020018460ff1660ff1681526020018360001916600019168152602001826000191660001916815260200194505050505060206040516020810390808403906000865af11580156108ea573d6000803e3d6000fd5b5050506020604051035190507fc063db5ba3814dca4c8bb33e3b0420c016bb8b80a45c983e1cfbe7279287c933818484604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018260001916600019168152602001935050505060405180910390a18073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415156109d757600080fd5b6000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600081548092919060010191905055508573ffffffffffffffffffffffffffffffffffffffff168560405180828051906020019080838360005b83811015610a6a578082015181840152602081019050610a4f565b50505050905090810190601f168015610a975780820380516001836020036101000a031916815260200191505b509150506000604051808303816000865af19150501515610ab757600080fd5b505050505050505050565b6000602482511015610ad75760009050610af7565b73ffffffffffffffffffffffffffffffffffffffff602483015181169150505b919050565b60008090505b8251811015610bc35781600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008584815181101515610b5b57fe5b9060200190602002015173ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055508080600101915050610b02565b5050505600a165627a7a723058203a4f40ec4fada04c47dac979b7a9042775141a98fcfdf42a1070342b11f490ea0029"

        val FUNC_WHITELIST = "whitelist"

        val FUNC_RELAYMETATX = "relayMetaTx"

        val FUNC_GETADDRESS = "getAddress"

        val FUNC_GETNONCE = "getNonce"

        val FUNC_ADDTOWHITELIST = "addToWhitelist"

        val FUNC_REMOVEFROMWHITELIST = "removeFromWhitelist"

        val RELAYEDMETATX_EVENT = Event("RelayedMetaTx",
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Uint8>() {

                }, object : TypeReference<Bytes32>() {

                }, object : TypeReference<Bytes32>() {

                }, object : TypeReference<Address>() {

                }, object : TypeReference<DynamicBytes>() {

                }, object : TypeReference<Address>() {

                }))

        val LOGADDRESS_EVENT = Event("LogAddress",
                Arrays.asList(),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Address>() {

                }, object : TypeReference<Address>() {

                }, object : TypeReference<Bytes32>() {

                }))

        fun deploy(web3j: Web3j, credentials: Credentials, gasPrice: BigInteger = GAS_PRICE, gasLimit: BigInteger = GAS_LIMIT): RemoteCall<TxRelay> {
            return Contract.deployRemoteCall(TxRelay::class.java, web3j, credentials, gasPrice, gasLimit, BINARY, "")
        }

        fun deploy(web3j: Web3j, transactionManager: TransactionManager, gasPrice: BigInteger = GAS_PRICE, gasLimit: BigInteger = GAS_LIMIT): RemoteCall<TxRelay> {
            return Contract.deployRemoteCall(TxRelay::class.java, web3j, transactionManager, gasPrice, gasLimit, BINARY, "")
        }

        fun load(contractAddress: String, web3j: Web3j, credentials: Credentials, gasPrice: BigInteger = GAS_PRICE, gasLimit: BigInteger = GAS_LIMIT): TxRelay {
            return TxRelay(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        fun load(contractAddress: String, web3j: Web3j, transactionManager: TransactionManager, gasPrice: BigInteger = GAS_PRICE, gasLimit: BigInteger = GAS_LIMIT): TxRelay {
            return TxRelay(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }
    }
}
