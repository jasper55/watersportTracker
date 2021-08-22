package wagner.jasper.watersporttracker.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class CombinedLiveData<R>(vararg liveDatas: LiveData<*>,
                          private val combine: (List<Any?>) -> R?
) : MediatorLiveData<R>() {

    private val data: MutableList<Any?> = MutableList(liveDatas.size) { null }

    init {
        for (i in liveDatas.indices) {
            liveDatas[i]?.let { liveData ->
                super.addSource(liveData) {
                    data[i] = it
                    value = combine(data)
                }
            }
        }
    }
}