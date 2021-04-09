package com.bible.core

interface TaskCallback<T> {
    fun onComplete(result: T)
    fun onException(t: Throwable?)
}