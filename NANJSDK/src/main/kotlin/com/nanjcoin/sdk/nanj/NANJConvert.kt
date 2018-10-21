package com.nanjcoin.sdk.nanj

import org.web3j.utils.Convert
import java.math.BigDecimal


/**
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 5/4/18
 * ____________________________________
 */

object NANJConvert {

	/**
	 * convert from wei to eth
	 * @param number of wei as String
	 * @param unit
	 * @return eth as BigDecimal
	 */
	@JvmStatic fun fromWei(number : String, unit : Unit) : BigDecimal {
		return fromWei(BigDecimal(number), unit)
	}

	/**
	 *  convert from wei to eth
	 *  @param number: number of ether
	 *  @param unit : ethereum unit
	 *  @return eth as BigDecimal
	 */
	@JvmStatic fun fromWei(number : BigDecimal, unit : Unit) : BigDecimal {
		return number.divide(unit.weiFactor)
	}

	/**
	 * from eth to wei
	 * @param number as String
	 * @param unit as Ethereum Unit
	 * @return wei amount as big decimal
	 */
	@JvmStatic fun toWei(number : String, unit : Unit) : BigDecimal {
		return toWei(BigDecimal(number), unit)
	}

	/**
	 * from eth to wei
	 * @param number as amount of Eth
	 * @param unit as ethereum unit
	 * @return wei amount as big decimal
	 */
	@JvmStatic fun toWei(number : BigDecimal, unit : Unit) : BigDecimal {
		return number.multiply(unit.weiFactor)
	}

	/**
	 * ethereum units
	 */
	 enum class Unit private constructor(name : String, factor : Int) {
		WEI("wei", 0),
		KWEI("kwei", 3),
		MWEI("mwei", 6),
		GWEI("gwei", 9),
		SZABO("szabo", 12),
		FINNEY("finney", 15),
		ETHER("ether", 18),
		KETHER("kether", 21),
		METHER("mether", 24),
		GETHER("gether", 27),
		NANJ("nanj", 8);

		val weiFactor : BigDecimal
		private val nameWei : String

		init {
			this.nameWei = name
			this.weiFactor = BigDecimal.TEN.pow(factor)
		}

		override fun toString() : String {
			return nameWei
		}

		fun fromString(name : String) : Unit {
				for (unit in Unit.values()) {
					if (name.equals(unit.name, ignoreCase = true)) {
						return unit
					}
				}
			return Unit.valueOf(name)
		}
	}
}
 