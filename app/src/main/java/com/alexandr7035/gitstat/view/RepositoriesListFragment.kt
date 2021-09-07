package com.alexandr7035.gitstat.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.databinding.FragmentReposBinding
import com.alexandr7035.gitstat.databinding.FragmentRepositoriesListBinding


class RepositoriesListFragment : Fragment() {

    private var binding: FragmentRepositoriesListBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}