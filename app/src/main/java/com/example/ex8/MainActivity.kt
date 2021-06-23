package com.example.ex8

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    @JvmField
    var holder: RootHolderImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val workManager = WorkManager.getInstance(this)
        if (holder == null) {
            holder = RootHolderImpl(this)
        }
        val adapter = Adapter(holder!!)
        val recycler = findViewById<RecyclerView>(R.id.recyclerTodoItemsList)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false /*reverseLayout*/)

//        //my code starts here:
        val editTextUserInput = findViewById<EditText>(R.id.editTextInsertTask)
        val buttonAdd = findViewById<FloatingActionButton>(R.id.buttonCreateTodoItem)
        buttonAdd.setOnClickListener {
            val number = editTextUserInput.text.toString().toLong()
            val request = OneTimeWorkRequest.Builder(countToNumWorker::class.java)
                    .setInputData(Data.Builder().putLong("number", number)
                            .build())
                    .addTag("calc_roots").build()
            workManager.enqueueUniqueWork(number.toString(),ExistingWorkPolicy.REPLACE,request)
            val new_root = RootCalc(number, request.id)
            val pos=holder!!.addNewInProgressItem(new_root)
            adapter.notifyItemInserted(pos)
            editTextUserInput.setText("")
        }
        adapter.onDelCallback = { position: Int ->
            val rootItem = holder!!.getCurrentItems().get(position)
            holder!!.deleteItem(rootItem)
            adapter.notifyItemRemoved(position)
            workManager.cancelWorkById(rootItem.id)
        }
        val workInfosByTagLiveData = workManager.getWorkInfosByTagLiveData("calc_roots")
        workInfosByTagLiveData.observe(this, Observer { workInfos ->
            for (work in workInfos) {
                if (work.state == WorkInfo.State.SUCCEEDED) {
                    val curr_pos = holder!!.getItemPosById(work.id)
                    if (curr_pos == -1) {
                        continue
                    }
                    val root1 = work.outputData.getLong("root1", 0)
                    val root2 = work.outputData.getLong("root2", 0)
                    val item = holder!!.getCurrentItems().get(curr_pos)
                    item.set_answer(root1.toString() + "x" + root2.toString())
                    val new_pos = holder!!.markItemDone(work.id)
                    workManager.cancelWorkById(work.id)
                    adapter.notifyItemMoved(curr_pos, new_pos)
                    adapter.notifyItemChanged(new_pos)
                }
                else {
                    val curr_pos = holder!!.getItemPosById(work.id)
                    if (curr_pos == -1) {
                        continue
                    }
                    val progress = work.progress
                    val value = progress.getInt("PROGRESS",0)
                    val item = holder!!.getCurrentItems().get(curr_pos)
                    item.progress = value
                    adapter.notifyItemChanged(curr_pos)
                }
            }
        })

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("state", holder!!.saveState())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //calculator.loadState(savedInstanceState);
        val prevState = savedInstanceState.getSerializable("state")
        holder!!.loadState(prevState)
    }
}