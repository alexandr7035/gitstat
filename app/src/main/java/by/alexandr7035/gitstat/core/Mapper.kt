package by.alexandr7035.gitstat.core

interface Mapper<SRC, DST> {
    fun map(data: SRC): DST
}