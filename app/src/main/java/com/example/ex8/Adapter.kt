package com.example.ex8

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter(var itemsHolder: RootHolderImpl) : RecyclerView.Adapter<RootHolder>() {
    interface OnDelCallBck{
        public fun onDel(pos:Int)
    }
    public var onCheckBoxCallback: ((Int) -> Unit)? = null
    public var onDelCallback: ((Int) -> Unit)? = null
    public var onItemClickCallBack: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RootHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.root_holder, parent, false)
        val holder = RootHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return itemsHolder.getCurrentItems().size
    }

    override fun onBindViewHolder(holder: RootHolder, position: Int) {
        val rootItem = itemsHolder.getCurrentItems().get(position)
        if (rootItem._status){
            holder.description.setText(rootItem._number.toString()+": "+rootItem._nswer)
            holder.progressBar.visibility = View.INVISIBLE
        }
        else{
            holder.description.setText("calculating root for: "+rootItem._number.toString())
            holder.progressBar.max = rootItem.sqrt_num.toInt()
            holder.progressBar.setProgress(rootItem.getProgress(),true)
            holder.progressBar.visibility = View.VISIBLE
        }
        holder.delete.setOnClickListener {
            val callback = onDelCallback ?: return@setOnClickListener
                print("in delete: "+position+"\n")
                callback(holder.adapterPosition)
        }
    }
}