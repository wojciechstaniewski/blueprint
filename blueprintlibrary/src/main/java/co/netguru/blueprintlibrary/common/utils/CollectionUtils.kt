package co.netguru.blueprintlibrary.common.utils

fun <T> T.asList(): List<T> {
    return listOf(this)
}