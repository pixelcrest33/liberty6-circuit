package com.Vantavu6.Sevlod.presentation.common

object Formats {

    fun grouped(value: Int): String {
        val digits = value.toString()
        if (digits.length <= 3) return digits
        val builder = StringBuilder()
        var counter = 0
        for (index in digits.lastIndex downTo 0) {
            builder.append(digits[index])
            counter++
            if (counter % 3 == 0 && index != 0) {
                builder.append(',')
            }
        }
        return builder.reverse().toString()
    }

    fun clock(millis: Float): String {
        val total = (millis / 1000f).toInt().coerceAtLeast(0)
        val minutes = total / 60
        val seconds = total % 60
        val paddedSeconds = if (seconds < 10) "0$seconds" else seconds.toString()
        return "$minutes:$paddedSeconds"
    }

    fun multiplier(value: Int): String = "x$value"

    fun metres(distanceDp: Int): String = grouped(distanceDp)
}
