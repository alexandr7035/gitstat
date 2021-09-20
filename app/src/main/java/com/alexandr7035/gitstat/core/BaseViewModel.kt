package com.alexandr7035.gitstat.core

interface BaseViewModel {
    // Presumably, we should logout when receive 401 code in any fragment
    // (E.g. the token has expired
    // So make it necessary by default
    fun doLogOut()
}