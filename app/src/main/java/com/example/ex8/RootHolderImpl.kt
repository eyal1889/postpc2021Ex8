package com.example.ex8
import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


class RootHolderImpl {
    val sp: SharedPreferences;
    private var todos = ArrayList<RootCalc>()
    private var work_manager:WorkManager
    constructor(context: Context) {
        sp = context.getSharedPreferences("local_db_todo", Context.MODE_PRIVATE)
        initFromSp()
        work_manager = WorkManager.getInstance(context)
    }

    public fun initFromSp() {
        val storedJson: String? = sp.getString("holder", "nope")
        if (storedJson.equals("nope")) return
        val gson = Gson()
        val type = object : TypeToken<ArrayList<RootCalc>>() {}.type
        //arrayType:Type = TypeToken<>
        todos = gson.fromJson(storedJson, type)
    }

    fun resetSp(){
        sp.edit().clear().apply();
    }

    fun getItemPosById(id:UUID):Int{
        for ((index,item) in todos.withIndex()){
            if (item.id == id){
                return index
            }
        }
        return -1
    }

    fun getCurrentItems(): ArrayList<RootCalc> {
        val copy_arr = ArrayList<RootCalc>()
        copy_arr.addAll(todos)
        return copy_arr
    }

    fun addNewInProgressItem(rootCalc: RootCalc):Int {
        //println(date.time)
        todos.add(0, rootCalc);
        return reorder_Marked_Progress(0)
    }

    fun markItemDone(id: UUID): Int {
        for ((index,item) in todos.withIndex()){
            if (item.id == id){
                item._status=true
                return reorder_Marked_Done(index)
            }
        }
        return -1
    }

    fun markItemInProgress(id:UUID): Int {
        for ((index,item) in todos.withIndex()){
            if (item.id == id){
                item._status=false
                return reorder_Marked_Progress(index)
            }
        }
        return -1
    }
    fun deleteItem(item: RootCalc) {
        //size-=1
        todos.remove(item)
        saveInSp()
    }

//    override fun getSize():Int {
//        return size
//    }

    fun saveState(): Serializable {
        val state = RootState()
        state.todos.addAll(todos)
        //state.size = size
        return state
    }

    fun loadState(prevState: Serializable?) {
        if (prevState !is RootState) {
            return  // ignore
        }
        todos.clear()
        todos.addAll(prevState.todos)
        //size = prevState.size
    }

    fun saveInSp() {
        val gson = Gson()
        val objJson: String = gson.toJson(todos, ArrayList::class.java)
        val editor = sp.edit()
        editor.putString("holder", objJson)
        editor.apply()
    }

    private class RootState : Serializable {
        /*
    all fields must only be from the types:
    - primitives (e.g. int, boolean, etc)
    - String
    - ArrayList<> where the type is a primitive or a String
    - HashMap<> where the types are primitives or a String
     */
        var todos = java.util.ArrayList<RootCalc>()
        //var size=0
    }

    fun reorder_Marked_Done(position: Int): Int {
        var new_pos = position
        for (i in position until todos.size - 1) {
            if (todos.get(i + 1).get_status()) {
                break
            }
            new_pos++
            val temp = todos.get(i)
            todos.set(i, todos.get(i + 1))
            todos.set(i + 1, temp)
        }
        saveInSp()
        return new_pos
    }

    fun reorder_Marked_Progress(position: Int): Int {
        var new_pos = position
        for (i in position until todos.size - 1) {
            if (todos.get(i + 1).get_status()) {
                break
            }
            if (todos.get(i)._number > todos.get(i + 1)._number) {
                new_pos++
                val temp = todos.get(i)
                todos.set(i, todos.get(i + 1))
                todos.set(i + 1, temp)
            }
        }
        saveInSp()
        return new_pos
    }
}