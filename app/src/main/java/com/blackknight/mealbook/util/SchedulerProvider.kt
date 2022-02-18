package com.blackknight.mealbook.util

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface SchedulerProvider {
    fun main(): Scheduler
    fun io(): Scheduler
    fun computation(): Scheduler
}

class SchedulerProviderImpl @Inject constructor() : SchedulerProvider {
    override fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }
}