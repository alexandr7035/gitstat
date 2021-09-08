package com.alexandr7035.gitstat.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.databinding.FragmentReposBinding
import com.alexandr7035.gitstat.databinding.FragmentRepositoriesListBinding


class RepositoriesListFragment : Fragment() {

    private var binding: FragmentRepositoriesListBinding? = null
    private var sharedPreferences: SharedPreferences? = null
    private var viewModel: MainViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val user = sharedPreferences!!.getString(getString(R.string.shared_pref_login), "NONE")!!
        val token = sharedPreferences!!.getString(getString(R.string.shared_pref_token), "NONE")

        viewModel = MainViewModel(requireActivity().application, "$user", "$token")

        binding!!.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = RepositoriesAdapter()
        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel!!.getRepositoriesData().observe(viewLifecycleOwner, {
            adapter.setItems(it)
        })

        viewModel!!.updateRepositoriesLiveData()
    }

}