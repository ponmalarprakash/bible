package com.bible.core

interface TaskCallbackProgress<T> {
    fun onComplete(result: T)
    fun onException(t: Throwable?)
}