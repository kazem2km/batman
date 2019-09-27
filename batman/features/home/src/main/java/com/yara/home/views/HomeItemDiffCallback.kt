package com.yara.home.views

import androidx.recyclerview.widget.DiffUtil
import com.yara.model.Search

class HomeItemDiffCallback(private val oldList: List<Search>,
                           private val newList: List<Search>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].imdbID == newList[newItemPosition].imdbID
                && oldList[oldItemPosition].Poster == newList[newItemPosition].Poster
                && oldList[oldItemPosition].Type == newList[newItemPosition].Type
    }
}