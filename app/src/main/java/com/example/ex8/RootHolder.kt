package com.example.ex8

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RootHolder(view: View): RecyclerView.ViewHolder(view){
    val description: TextView = view.findViewById(R.id.roots)
    val delete:ImageButton = view.findViewById(R.id.delete)
    val progressBar:ProgressBar = view.findViewById(R.id.progressBar)
}