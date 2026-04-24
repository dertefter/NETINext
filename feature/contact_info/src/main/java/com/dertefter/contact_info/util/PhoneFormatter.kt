package com.dertefter.contact_info.util

object PhoneFormatter {
    fun format(input: String): String {
        var digits = input.filter { it.isDigit() }
        
        if (digits.isEmpty()) return ""

        if (digits.startsWith("8")) {
            digits = "7" + digits.substring(1)
        }
        
        val result = StringBuilder()
        
        for (i in digits.indices) {
            if (i >= 11) break
            
            when (i) {
                0 -> result.append("+")
                1 -> result.append(" (")
                4 -> result.append(") ")
                7 -> result.append("-")
            }
            result.append(digits[i])
        }
        
        return result.toString()
    }
}
