package com.tinuproject.tinu.domain.enum

enum class PaymentMethod(var code : Int, var text : String) {

    ACCOUNTTRANSFER(0, "계좌이체"),
    CASH(1, "현금결제"),

}