package by.alexandr7035.gitstat.view.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentReposOverviewBinding
import by.alexandr7035.gitstat.extensions.navigateSafe
import by.alexandr7035.gitstat.view.MainActivity
import by.alexandr7035.gitstat.view.repositories.plots.languages_plot.LanguagesPlot
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ReposOverviewFragment : Fragment() {

    private var binding: FragmentReposOverviewBinding? = null
    private val viewModel by viewModels<RepositoriesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentReposOverviewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plot = LanguagesPlot()
        // Setup chart configuration
        if (binding?.languagesChart != null) {
            plot.setupPlot(binding!!.languagesChart)
        }

        viewModel.getRepositoriesLiveData().observe(viewLifecycleOwner, { repos ->

            if (repos != null) {

                Timber.tag("DEBUG_TAG").d("repos $repos")

                binding!!.totalReposCountView.text = repos.size.toString()
                binding!!.privateReposCountView.text = repos.filter { it.isPrivate }.size.toString()
                binding!!.publicReposCountView.text = repos.filter { !it.isPrivate }.size.toString()

                // Show stup instead of plot if list is empty
                if (repos.isNullOrEmpty()) {
                    binding?.languagesChart?.visibility = View.GONE
                    binding?.noReposStub?.visibility = View.VISIBLE
                } else {
                    binding?.languagesChart?.visibility = View.VISIBLE
                    binding?.noReposStub?.visibility = View.GONE

                    // Populate chart with data
                    plot.setLanguagesData(
                        chart = binding!!.languagesChart,
                        languages = viewModel.getLanguagesForReposList(repos),
                        totalReposCount = repos.size
                    )

                }
            }
        })


        binding!!.toReposListBtn.setOnClickListener {
            findNavController().navigateSafe(ReposOverviewFragmentDirections.actionReposFragmentToRepositoriesListHostFragment())
        }

        binding?.drawerBtn?.setOnClickListener {
            (requireActivity() as MainActivity).openDrawerMenu()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}