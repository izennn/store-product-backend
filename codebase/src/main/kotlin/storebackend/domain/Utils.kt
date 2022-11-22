package storebackend.domain

import java.math.RoundingMode
import java.text.DecimalFormat

object Utils {
    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.FLOOR
        return df.format(number).toDouble()
    }
}