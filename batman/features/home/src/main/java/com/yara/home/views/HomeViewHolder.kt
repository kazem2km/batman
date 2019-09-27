package com.yara.home.views

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yara.home.HomeViewModel
import com.yara.home.databinding.ItemHomeBinding
import com.yara.model.Search

class HomeViewHolder(parent: View): RecyclerView.ViewHolder(parent) {

    private val binding = ItemHomeBinding.bind(parent)

    fun bindTo(search: Search, viewModel: HomeViewModel) {
        binding.search = search
        binding.viewmodel = viewModel
    }
}