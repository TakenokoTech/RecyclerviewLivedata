package tech.takenoko.recyclerviewlivedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData


class MainViewModel: ViewModel(), MainActivity.MainViewAction {

    var liveList = MutableLiveData<List<LiveSampleEntity>>().apply {
        val list1 = (0..4).map { LiveSampleEntity("$it", null) }
        val list2 = (0..4).map { LiveSampleEntity("$it", MutableLiveData<List<LiveSampleEntity>>().apply { value = list1 }) }
        val list3 = (0..4).map { LiveSampleEntity("$it", MutableLiveData<List<LiveSampleEntity>>().apply { value = list2 }) }
        value = list3
    }

    data class LiveSampleEntity(val position: String, val list: MutableLiveData<List<LiveSampleEntity>>?) {
        fun add() {
            println("add")
            list?.postValue(list.value?.toMutableList().apply { this?.add(first())})
        }
    }
}


