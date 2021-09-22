package com.alexandr7035.gitstat.view.repositories

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.App
import com.alexandr7035.gitstat.core.SyncStatus
import com.alexandr7035.gitstat.databinding.FragmentReposOverviewBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ReposOverviewFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"
    private var binding: FragmentReposOverviewBinding? = null
    private lateinit var navController: NavController
    private val viewModel by viewModels<RepositoriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentReposOverviewBinding.inflate(inflater, container, false)

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NavController
        val hf: NavHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

        // Setup chart configuration
        setupLanguagesChart()

        viewModel.getAllRepositoriesListLiveData().observe(viewLifecycleOwner, {

            if (it != null && it.isNotEmpty()) {

                showRepositoriesViews()

                val reposList = it

                // Repos count views
                val totalReposCount = it.size
                var privateReposCount = 0
                it.forEach { repo ->
                    if (repo.isPrivate){
                        privateReposCount += 1
                    }
                }
                val publicReposCount: Int = totalReposCount - privateReposCount

                binding!!.totalReposCountView.text = totalReposCount.toString()
                binding!!.privateReposCountView.text = privateReposCount.toString()
                binding!!.publicReposCountView.text = publicReposCount.toString()

                // Diagram data. Colors must correspond entries in their order in list
                val languages = ((requireActivity().application) as App).progLangManager.getLanguagesList(reposList)
                val entries = ArrayList<PieEntry>()
                val diagramColors: MutableList<Int> = ArrayList()

                languages.forEach { language ->
                    entries.add(PieEntry(language.count.toFloat(), language.name))
                    diagramColors.add(Color.parseColor(language.color))
                }

                // Chart
                binding!!.languagesChart.invalidate()

                val dataSet = PieDataSet(entries, "")
                dataSet.apply {
                    colors = diagramColors
                    valueTextSize = 20f
                    valueTextColor = Color.WHITE
                    valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }


                val pieData = PieData(dataSet)
                // Remove decimal part from value
                pieData.setValueFormatter(object: ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "" + value.toInt()
                    }
                })

                // Update data
                binding!!.languagesChart.apply {
                    centerText = totalReposCount.toString()
                    // Should be in the end to display legend correctly
                    data = pieData
                }

            }

            // If empty cache
            else {
                hideRepositoriesViews()
            }
        })


        // Update synchronization status view
        viewModel.getSyncStatusLiveData().observe(viewLifecycleOwner, {

            when (it!!) {
                SyncStatus.PENDING -> {
                    binding!!.syncStatusView.setBackgroundResource(R.drawable.background_sync_button_pending)
                }
                SyncStatus.SUCCESS -> {
//                    binding!!.swipeRefreshLayout.isRefreshing = false
                    binding!!.syncStatusView.setBackgroundResource(R.drawable.background_sync_button_synced)
                }
                SyncStatus.FAILED -> {
//                    binding!!.swipeRefreshLayout.isRefreshing = false
                    binding!!.syncStatusView.setBackgroundResource(R.drawable.background_sync_button_failed)
                }
            }
        })

        binding!!.toReposListBtn.setOnClickListener {
            navController.navigate(R.id.action_reposFragment_to_repositoriesListHostFragment)
        }


        viewModel.syncRepositoriesData()
        // TODO find better solution
        viewModel.updateAllRepositoriesLiveData()

    }


    private fun hideRepositoriesViews() {
        binding!!.reposCountCard.visibility = View.GONE
        binding!!.languagesCard.visibility = View.GONE
    }


    private fun showRepositoriesViews() {
        binding!!.reposCountCard.visibility = View.VISIBLE
        binding!!.languagesCard.visibility = View.VISIBLE
    }


    private fun setupLanguagesChart() {
        // Legend settings
        binding!!.languagesChart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)
            isWordWrapEnabled = true
            textSize = 20f
            xEntrySpace = 20f
        }

        // General chart settings
        binding!!.languagesChart.apply {
            setEntryLabelTextSize(16f)
            setDrawEntryLabels(false)
            description.isEnabled = false
            setCenterTextSize(30f)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}