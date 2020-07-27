package com.appyvet.rangebarsample

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = Adapter()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }

    class Adapter : RecyclerView.Adapter<VH>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): VH {
            return VH(LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.recyclerview_item, viewGroup, false))
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun onBindViewHolder(vh: VH, i: Int) {}
        override fun getItemCount(): Int {
            return 50
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
}