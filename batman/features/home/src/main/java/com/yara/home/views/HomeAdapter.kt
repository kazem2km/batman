package com.yara.home.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yara.home.HomeViewModel
import com.yara.home.R
import com.yara.model.Search

class HomeAdapter(private val viewModel: HomeViewModel): RecyclerView.Adapter<HomeViewHolder>() {

    private val searches: MutableList<Search> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = HomeViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_home,
            parent,
            false
        )
    )

    override fun getItemCount(): Int
            = searches.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int)
            = holder.bindTo(searches[position], viewModel)

    // ---

    fun updateData(items: List<Search>) {
        val diffCallback = HomeItemDiffCallback(searches, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        searches.clear()
        searches.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}