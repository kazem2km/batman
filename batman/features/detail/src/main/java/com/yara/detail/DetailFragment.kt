package com.yara.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import com.yara.common.base.BaseFragment
import com.yara.common.base.BaseViewModel
import com.yara.detail.databinding.FragmentDetailBinding
import com.yara.model.Search
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [BaseFragment] subclass
 * that will show the [Search] details.
 */
class DetailFragment : BaseFragment() {

    // FOR DATA
    private val viewModel: DetailViewModel by viewModel()
    private lateinit var binding: FragmentDetailBinding


    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadDataWhenActivityStarts(args.login)


    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun getExtras(): FragmentNavigator.Extras =
        FragmentNavigatorExtras(binding.fragmentDetailAvatar to getString(R.string.transition_avatar_dest))
}
