package wnanj

import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.tx.TransactionManager
import java.math.BigInteger


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/28/18
 * ____________________________________
 */

class NANJTransactionManager(web3j : Web3j, fromAddress: String) : TransactionManager(web3j, fromAddress) {
	override fun sendTransaction(gasPrice : BigInteger?, gasLimit : BigInteger?, to : String?, data : String?, value : BigInteger?) : EthSendTransaction? {
		return null
	}
}