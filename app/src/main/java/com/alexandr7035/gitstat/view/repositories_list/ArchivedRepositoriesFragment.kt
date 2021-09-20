package com.alexandr7035.gitstat.view.repositories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.App
import com.alexandr7035.gitstat.databinding.FragmentArchivedRepositoriesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArchivedRepositoriesFragment : Fragment() {

    private val viewModel by navGraphViewModels<RepositoriesListViewModel>(R.id.repositoriesListGraph) { defaultViewModelProviderFactory }
    private var binding: FragmentArchivedRepositoriesBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentArchivedRepositoriesBinding.inflate(inflater, container, false)

        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup adapter
        val adapter = RepositoriesAdapter((requireActivity().application as App).progLangManager)
        binding!!.root.adapter = adapter
        binding!!.root.layoutManager = LinearLayoutManager(context)

        viewModel.getArchivedRepositoriesLiveData().observe(viewLifecycleOwner, { repos ->
            adapter.setItems(repos)
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}