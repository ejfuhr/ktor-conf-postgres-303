package com.example

import kotlin.test.Test

/**
 * from https://kt.academy/article/power-assert
 */
class PowerAssertTest {
    @Test
    fun testCoffeePrice() {
        val coffeeToPrice = mapOf(
            Coffee(1, "Espresso") to 5.0,
            Coffee(2, "Espresso") to 5.0,
            Coffee(1, "Americano") to 5.0,
            Coffee(2, "Americano") to 5.0,
            Coffee(1, "Latte") to 5.0000,
            Coffee(2, "Latte") to 5.0,
        )
        for ((coffee, expectedPrice) in coffeeToPrice) {
            assert(value = expectedPrice == coffee.calculatedPrice()) { "Price should be 5" }
        }
    }

    @Test
    fun test2Coffees() {

        val cof1 = Coffee(2, "Espresso")
        val cof2 = Coffee(1, "Americano")
        assert(cof1.calculatedPrice() == cof2.calculatedPrice()) { "Prices should be equal" }
    }
}