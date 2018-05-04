package com.bc.core.nanj

import java.math.BigDecimal


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 5/4/18
 * ____________________________________
 */

object NANJConvert {

	fun fromWei(number : String, unit : Unit) : BigDecimal {
		return fromWei(BigDecimal(number), unit)
	}

	fun fromWei(number : BigDecimal, unit : Unit) : BigDecimal {
		return number.divide(unit.weiFactor)
	}

	fun toWei(number : String, unit : Unit) : BigDecimal {
		return toWei(BigDecimal(number), unit)
	}

	fun toWei(number : BigDecimal, unit : Unit) : BigDecimal {
		return number.multiply(unit.weiFactor)
	}

	enum class Unit private constructor(name : String, factor : Int) {
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
 