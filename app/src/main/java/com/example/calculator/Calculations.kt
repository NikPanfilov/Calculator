package com.example.calculator

import java.math.BigDecimal
import kotlin.math.floor

class Calculations {
    fun calculate(screenText: String): String {
        val signs = Regex("""[+÷X\-]""")
        val regexNum = Regex("""((-?\d+(\.\d+)?)|(\(-?\d+(\.\d+)?\)))""")
        val operationText = Regex("""$regexNum$signs$regexNum""")

        var text = screenText.replace(",", ".").replace("(", "").replace(")", "")

        var expression: String
        var operation: (Float, Float) -> BigDecimal

        var x: Float
        var y: Float

        while (text.contains(signs)) {
            operation = operationBySign(text, signs)

            x = text.substringBefore(signs.find(text, 0)!!.value).toFloat()
            y = text.substringAfter(signs.find(text, 0)!!.value).toFloat()

            expression = operationText.find(text, 0)!!.value
            text = text.replaceFirst(expression, operation(x, y).toPlainString())
        }

        return text.removeZeroFraction().replace('.', ',')
    }

    private fun operationBySign(text: String, signs: Regex): (Float, Float) -> BigDecimal {
        return when (signs.find(text, 0)!!.value) {
            "-" -> { a: Float, b: Float ->
                (a - b).toBigDecimal()
            }
            "+" -> { a: Float, b: Float ->
                (a + b).toBigDecimal()
            }
            "÷" -> { a: Float, b: Float ->
                if (a != 0F) (a / b).toBigDecimal() else 0.toBigDecimal()
            }
            else -> { a: Float, b: Float ->
                (a * b).toBigDecimal()
            }
        }
    }

    private fun String.removeZeroFraction(): String {
        if (this.toFloat() - floor(this.toFloat()) == 0F) {
            return this.substringBefore(',')
        }
        return this
    }
}