package com.bc.core.nanj

import java.math.BigInteger
import java.util.Arrays
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Bool
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.RemoteCall
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager

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
class MetaNANJCOINManager : Contract {

    protected constructor(contractAddress: String, web3j: Web3j, credentials: Credentials?, gasPrice: BigInteger, gasLimit: BigInteger) : super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit)

    protected constructor(contractAddress: String, web3j: Web3j, transactionManager: TransactionManager, gasPrice: BigInteger, gasLimit: BigInteger) : super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit)

    fun createWallet(owner: String): RemoteCall<TransactionReceipt> {
        val function = Function(
                FUNC_CREATEWALLET,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.Address(owner)),
                emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun getWallet(owner: String): RemoteCall<String> {
        val function = Function(FUNC_GETWALLET,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.Address(owner)),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Address>() {

                }))
        return executeRemoteCallSingleValueReturn(function, String::class.java)
    }

    fun forwardTo(sender: String, wallet: String, destination: String, value: BigInteger, data: ByteArray, feeAmount: BigInteger): RemoteCall<TransactionReceipt> {
        val function = Function(
                FUNC_FORWARDTO,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.Address(sender),
                        org.web3j.abi.datatypes.Address(wallet),
                        org.web3j.abi.datatypes.Address(destination),
                        org.web3j.abi.datatypes.generated.Uint256(value),
                        org.web3j.abi.datatypes.DynamicBytes(data),
                        org.web3j.abi.datatypes.generated.Uint256(feeAmount)),
                emptyList())
        return executeRemoteCallTransaction(function)
    }

    fun isOwner(wallet: String, owner: String): RemoteCall<Boolean> {
        val function = Function(FUNC_ISOWNER,
                Arrays.asList<Type<*>>(org.web3j.abi.datatypes.Address(wallet),
                        org.web3j.abi.datatypes.Address(owner)),
                Arrays.asList<TypeReference<*>>(object : TypeReference<Bool>() {

                }))
        return executeRemoteCallSingleValueReturn(function, Boolean::class.java)
    }

    companion object {

        private val GAS_PRICE = BigInteger.valueOf(20_000_000L)
        private val GAS_LIMIT = BigInteger.valueOf(1_000_000L)

        private val BINARY = "0x608060405234801561001057600080fd5b5060405160208061123183398101806040528101908080519060200190929190505050806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506111ae806100836000396000f300608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806304d0a647146100675780635db1a965146100ea5780637ddc02d4146101c7578063b054a9e814610242575b600080fd5b34801561007357600080fd5b506100a8600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610285565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156100f657600080fd5b506101c5600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001909291905050506102ee565b005b3480156101d357600080fd5b50610228600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061049a565b604051808215151515815260200191505060405180910390f35b34801561024e57600080fd5b50610283600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610532565b005b6000600160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561034957600080fd5b8486610355828261049a565b151561036057600080fd5b8673ffffffffffffffffffffffffffffffffffffffff1663d10de031878787876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200184815260200180602001838152602001828103825284818151815260200191508051906020019080838360005b8381101561042957808201518184015260208101905061040e565b50505050905090810190601f1680156104565780820380516001836020036101000a031916815260200191505b5095505050505050600060405180830381600087803b15801561047857600080fd5b505af115801561048c573d6000803e3d6000fd5b505050505050505050505050565b60008273ffffffffffffffffffffffffffffffffffffffff16600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614905092915050565b600081600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415151561057157600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156105cc57600080fd5b600073ffffffffffffffffffffffffffffffffffffffff16600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561066657600080fd5b61066e610710565b604051809103906000f08015801561068a573d6000803e3d6000fd5b50915081600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050565b604051610a6280610721833901905600608060405273980063874bbf3c211d6c814571636184c8721f1f600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506109ba806100a86000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631a695230146100da5780632f54bf6e1461011d5780638da5cb5b14610178578063c0ee0b8a146101cf578063d10de03114610262575b7f88a5966d370b9919b20f3e2c13ff65706f196a4e32cc2c12bf57088f885258743334604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a1005b3480156100e657600080fd5b5061011b600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506102ff565b005b34801561012957600080fd5b5061015e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061038c565b604051808215151515815260200191505060405180910390f35b34801561018457600080fd5b5061018d6103e5565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156101db57600080fd5b50610260600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061040a565b005b34801561026e57600080fd5b506102fd600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001909291905050506106fa565b005b6103083361038c565b151561031357600080fd5b3073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614151561038957806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b50565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16149050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b610412610930565b600084826000019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff1681525050838260200181815250508282604001819052506000835111156106f357601883600081518110151561047a57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027f0100000000000000000000000000000000000000000000000000000000000000900463ffffffff169060020a02601084600181518110151561050457fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027f0100000000000000000000000000000000000000000000000000000000000000900463ffffffff169060020a02600885600281518110151561058e57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027f0100000000000000000000000000000000000000000000000000000000000000900463ffffffff169060020a0285600381518110151561061657fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027f010000000000000000000000000000000000000000000000000000000000000090040101019050807c01000000000000000000000000000000000000000000000000000000000282606001907bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191690817bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815250505b5050505050565b60006107053361038c565b151561071057600080fd5b61071b858585610918565b151561072657600080fd5b60008211151561073557600080fd5b8490508073ffffffffffffffffffffffffffffffffffffffff1663a9059cbb600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b1580156107fd57600080fd5b505af1158015610811573d6000803e3d6000fd5b505050506040513d602081101561082757600080fd5b8101908080519060200190929190505050507fc1de93dfa06362c6a616cde73ec17d116c0d588dd1df70f27f91b500de207c41858585604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156108d55780820151818401526020810190506108ba565b50505050905090810190601f1680156109025780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050505050565b600080600083516020850186885af190509392505050565b608060405190810160405280600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016060815260200160007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916815250905600a165627a7a72305820f8095797aa28d535857b03e37c6fe38d6c219f2c13a1f750c159fe1636e6a7bb0029a165627a7a7230582058fccca4f968639594314ff7fb80a262d99e48f6587e875ec33faed3bad130f70029"

        val FUNC_CREATEWALLET = "createWallet"

        val FUNC_GETWALLET = "getWallet"

        val FUNC_FORWARDTO = "forwardTo"

        val FUNC_ISOWNER = "isOwner"

        fun deploy(web3j: Web3j, credentials: Credentials, gasPrice: BigInteger = GAS_PRICE, gasLimit: BigInteger = GAS_LIMIT, _relayAddress: String = NANJConfig.TX_RELAY_ADDRESS): RemoteCall<MetaNANJCOINManager> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(org.web3j.abi.datatypes.Address(_relayAddress)))
            return Contract.deployRemoteCall(MetaNANJCOINManager::class.java, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor)
        }

        fun deploy(web3j: Web3j, transactionManager: TransactionManager, gasPrice: BigInteger = GAS_PRICE, gasLimit: BigInteger = GAS_LIMIT, _relayAddress: String  =  NANJConfig.TX_RELAY_ADDRESS): RemoteCall<MetaNANJCOINManager> {
            val encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList<Type<*>>(org.web3j.abi.datatypes.Address(_relayAddress)))
            return Contract.deployRemoteCall(MetaNANJCOINManager::class.java, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor)
        }

        fun load(contractAddress: String = NANJConfig.META_NANJCOIN_MANAGER, web3j: Web3j, credentials: Credentials?, gasPrice: BigInteger = GAS_PRICE, gasLimit: BigInteger = GAS_LIMIT): MetaNANJCOINManager {
            return MetaNANJCOINManager(contractAddress, web3j, credentials, gasPrice, gasLimit)
        }

        fun load(contractAddress: String = NANJConfig.META_NANJCOIN_MANAGER, web3j: Web3j, transactionManager: TransactionManager, gasPrice: BigInteger = GAS_PRICE, gasLimit: BigInteger = GAS_LIMIT): MetaNANJCOINManager {
            return MetaNANJCOINManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit)
        }
    }
}
