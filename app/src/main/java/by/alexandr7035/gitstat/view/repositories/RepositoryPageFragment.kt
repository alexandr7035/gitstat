package by.alexandr7035.gitstat.view.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.gitstat.databinding.FragmentRepositoryPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoryPageFragment : Fragment() {

    private var binding: FragmentRepositoryPageBinding? = null
    private val viewModel by viewModels<RepositoriesViewModel>()
    private val safeArgs by navArgs<RepositoryPageFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRepositoryPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getRepositoryLiveData(safeArgs.repositoryId).observe(viewLifecycleOwner, { repoData ->
            binding?.toolbar?.title = repoData.name
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}