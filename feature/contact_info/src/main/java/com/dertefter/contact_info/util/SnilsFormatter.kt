package com.dertefter.contact_info.util

object SnilsFormatter {
    fun format(input: String): String {
        val digits = input.filter { it.isDigit() }.take(11)
        val result = StringBuilder()
        
        for (i in digits.indices) {
            result.append(digits[i])
            when (i) {
                2, 5 -> if (i != digits.lastIndex) result.append("-")
                8 -> if (i != digits.lastIndex) result.append(" ")
            }
        }
        return result.toString()
    }
}
