package com.example

data class Coffee(val size:Int, val type: String){

    fun calculatedPrice(): Double {
        return 5.00//size * 1.25
    }
}
