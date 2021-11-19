package by.alexandr7035.gitstat.view.repositories

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentRepositoryPageBinding
import by.alexandr7035.gitstat.extensions.debug
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
            Timber.debug("repo page ${repoData.description}")

            binding?.toolbar?.title = repoData.name
            binding?.repoName?.text = repoData.nameWithOwner
            binding?.language?.text = repoData.language
            binding?.stars?.text = repoData.stars.toString()

            binding?.repoDescription?.text = repoData.description

            when (repoData.isPrivate) {
                true -> {
                    binding?.repoVisibility?.text = getString(R.string.private_start_capital)
                    binding?.repoVisibility?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )

                    binding?.repoVisibility?.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_repo_visibilily_private
                    )
                }
                else -> {
                    binding?.repoVisibility?.text = getString(R.string.public_start_capital)
                    binding?.repoVisibility?.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding?.repoVisibility?.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_repo_visibilily
                    )
                }
            }

            (binding?.languageColorView?.background as GradientDrawable?)?.setColor(Color.parseColor(repoData.languageColor))
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}