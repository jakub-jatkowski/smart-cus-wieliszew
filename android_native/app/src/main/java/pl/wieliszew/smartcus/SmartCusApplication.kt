package pl.wieliszew.smartcus

import android.app.Application

class SmartCusApplication : Application() {
    lateinit var repository: pl.wieliszew.smartcus.data.repository.SmartCusRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = pl.wieliszew.smartcus.data.repository.SmartCusRepository()
    }
}

