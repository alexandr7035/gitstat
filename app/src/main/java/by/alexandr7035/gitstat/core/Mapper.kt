package by.alexandr7035.gitstat.core

interface Mapper<SRC, DST> {
    fun transform(data: SRC): DST
}