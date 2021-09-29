package wagner.jasper.watersporttracker.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <A, B> LiveData<A>.combineLatest(b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        addSource(this@combineLatest) {
            if (it == null && value != null) value = null
            lastA = it
            val notNullA = lastA ?: return@addSource
            val notNullB = lastB ?: return@addSource
            value = notNullA to notNullB
        }

        addSource(b) {
            if (it == null && value != null) value = null
            lastB = it
            val notNullA = lastA ?: return@addSource
            val notNullB = lastB ?: return@addSource
            value = notNullA to notNullB
        }
    }
}