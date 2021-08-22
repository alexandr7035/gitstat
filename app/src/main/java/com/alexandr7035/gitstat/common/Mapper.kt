package com.alexandr7035.gitstat.common

interface Mapper<SRC, DST> {
    fun transform(data: SRC): DST
}