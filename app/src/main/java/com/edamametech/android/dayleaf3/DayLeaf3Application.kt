package com.edamametech.android.dayleaf3

import android.app.Application
import com.edamametech.android.dayleaf3.data.AppContainer
import com.edamametech.android.dayleaf3.data.AppDataContainer

class DayLeaf3Application : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}