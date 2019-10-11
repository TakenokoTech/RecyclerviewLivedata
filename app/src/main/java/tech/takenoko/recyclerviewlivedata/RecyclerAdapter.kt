package tech.takenoko.recyclerviewlivedata

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import tech.takenoko.recyclerviewlivedata.databinding.ItemLastBinding
import tech.takenoko.recyclerviewlivedata.databinding.ItemRecyclerBinding

@SuppressLint("ClickableViewAccessibility")
class RecyclerAdapter(
    private val context: Context?,
    private var liveList: MutableLiveData<List<MainViewModel.LiveSampleEntity>>,
    private val action: MainActivity.MainViewAction,
    private val owner: LifecycleOwner,
    private val layer: Int
): Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = when(viewType) {
        VIEW_TYPE_RECYCLER ->
            RecyclerAdapterHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_recycler,
                    parent,
                    false
                )
            )
        else ->
            LastAdapterHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_last,
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder) {
            is RecyclerAdapterHolder -> {
                holder.binding.entity = liveList.value?.get(position)
                holder.binding.layer = layer
                holder.binding.lifecycleOwner = owner
                holder.binding.recyclerView.setOnTouchListener{ v, _ -> v.parent.requestDisallowInterceptTouchEvent(true); false }
                holder.binding.recyclerView.create(liveList.value?.get(position)?.list!!, action, owner, layer+1)
                holder.binding.addButton.setOnClickListener { liveList.value?.get(position)?.add() }
            }
            is LastAdapterHolder -> {
                holder.binding.entity = liveList.value?.get(position)
                holder.binding.layer = layer
                holder.binding.lifecycleOwner = owner
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if(liveList.value?.get(position)?.list != null) VIEW_TYPE_RECYCLER else VIEW_TYPE_LAST
    override fun getItemCount(): Int = liveList.value?.size ?: 0

    fun setData(liveList: MutableLiveData<List<MainViewModel.LiveSampleEntity>>) {
        this.liveList = liveList
        notifyDataSetChanged()
    }

    class RecyclerAdapterHolder(val binding: ItemRecyclerBinding): ViewHolder(binding.root)
    class LastAdapterHolder(val binding: ItemLastBinding): ViewHolder(binding.root)

    companion object {
        const val VIEW_TYPE_RECYCLER = 0
        const val VIEW_TYPE_LAST = 1
    }
}

fun RecyclerView.create(liveList: MutableLiveData<List<MainViewModel.LiveSampleEntity>>, action: MainActivity.MainViewAction, owner: LifecycleOwner, layer: Int = 1) {
    addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
    layoutManager = LinearLayoutManager(context)
    adapter = RecyclerAdapter(context, liveList, action, owner, layer)
    liveList.observe(owner, Observer { (this.adapter as RecyclerAdapter?)?.setData(liveList) })
}
