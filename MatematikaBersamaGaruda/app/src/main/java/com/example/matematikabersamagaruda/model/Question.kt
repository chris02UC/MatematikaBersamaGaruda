package com.example.matematikabersamagaruda.model

import kotlin.random.Random

enum class Operator(val symbol: String) {
    ADD("+"), SUBTRACT("-"), MULTIPLY("×"), DIVIDE("÷");
}

data class Question(
    val operand1: Int,
    val operand2: Int,
    val operator: Operator
) {
    val answer: Int get() = when (operator) {
        Operator.ADD -> operand1 + operand2
        Operator.SUBTRACT -> operand1 - operand2
        Operator.MULTIPLY -> operand1 * operand2
        Operator.DIVIDE -> operand1 / operand2
    }

    fun displayText(): String = "$operand1 ${operator.symbol} $operand2"

    companion object {
        fun generate(isHard: Boolean): Question {
            val op = Operator.values().random()

            return when (op) {
                Operator.ADD -> {
                    if (isHard) {
                        // Hard: 2-3 digits, result max 999
                        val a = Random.nextInt(10, 900)
                        val b = Random.nextInt(10, 1000 - a)
                        Question(a, b, op)
                    } else {
                        // Easy: 1 digit
                        Question(Random.nextInt(1, 10), Random.nextInt(1, 10), op)
                    }
                }
                Operator.SUBTRACT -> {
                    if (isHard) {
                        // Hard: 2-3 digits, result positive and max 999
                        val a = Random.nextInt(10, 1000)
                        val b = Random.nextInt(1, a)
                        Question(a, b, op)
                    } else {
                        // Easy: Result must be 1 digit (1..9) and positive
                        val ans = Random.nextInt(1, 10)
                        val b = Random.nextInt(1, 10)
                        val a = ans + b // e.g., 9 + 9 = 18. 18 - 9 = 9
                        Question(a, b, op)
                    }
                }
                Operator.MULTIPLY -> {
                    if (isHard) {
                        // Hard: 2 digits, result max 4 digits
                        Question(Random.nextInt(10, 100), Random.nextInt(10, 100), op)
                    } else {
                        // Easy: 1 x 1 digit
                        Question(Random.nextInt(1, 10), Random.nextInt(1, 10), op)
                    }
                }
                Operator.DIVIDE -> {
                    if (isHard) {
                        // Hard: Dividend max 4 digits (9999), whole number answer
                        val divisor = Random.nextInt(2, 500)
                        val maxMultiplier = 9999 / divisor
                        val multiplier = Random.nextInt(1, maxMultiplier + 1)
                        Question(divisor * multiplier, divisor, op)
                    } else {
                        // Easy: Result must be 1 digit (1..9) and whole number
                        val ans = Random.nextInt(1, 10)
                        val divisor = Random.nextInt(1, 10)
                        Question(ans * divisor, divisor, op)
                    }
                }
            }
        }
    }
}