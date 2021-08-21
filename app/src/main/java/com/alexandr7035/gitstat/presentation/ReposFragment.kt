package com.alexandr7035.gitstat

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alexandr7035.gitstat.common.SyncStatus
import com.alexandr7035.gitstat.databinding.FragmentReposBinding
import com.alexandr7035.gitstat.presentation.MainViewModel
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.util.*


class ReposFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentReposBinding

    private lateinit var navController: NavController

    private lateinit var languagesList: TreeMap<String, Int>

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentReposBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(LOG_TAG, "repos fragment onviewcreated")

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        val user = sharedPreferences.getString(getString(R.string.shared_pref_login), "NONE")
        val token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")
        Log.d(LOG_TAG, "Auth '$user' with token '$token'")

        viewModel = MainViewModel(requireActivity().application, "$user", "$token")

        // NavController
        val hf: NavHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = hf.navController

    }


    override fun onResume() {
        super.onResume()

        viewModel.getRepositoriesData().observe(viewLifecycleOwner, {

            if (it != null && it.isNotEmpty()) {
                
                showRepositoriesViews()

                val reposList = it

                // Repos count views
                var totalReposCount = it.size
                var privateReposCount = 0
                var publicReposCount = 0
                it.forEach {
                    if (it.isPrivate){
                        privateReposCount += 1
                    }
                }
                publicReposCount = totalReposCount - privateReposCount

                binding.totalReposCountView.text = totalReposCount.toString()
                binding.privateReposCountView.text = privateReposCount.toString()
                binding.publicReposCountView.text = publicReposCount.toString()

                // Chart
                binding.languagesChart.invalidate()

                // Read colors jsom from resources
                val inputStream = resources.openRawResource(R.raw.language_colors)
                val reader = InputStreamReader(inputStream)
                val builder = GsonBuilder()
                val itemsMapType = object : TypeToken<Map<String, Map<String, String>>>() {}.type
                val languagesColorsList: Map<String, Map<String, String>> = builder.create().fromJson(reader, itemsMapType)

                // Diagram data
                val entries = ArrayList<PieEntry>()
                val diagramColors: MutableList<Int> = ArrayList()

                // Init languages map
                languagesList = TreeMap()
                reposList.forEachIndexed { i, repo ->
                    languagesList[repo.language] = 0
                }

                // Populate languages map
                reposList.forEachIndexed { i, repo ->
                    languagesList[repo.language] = languagesList[repo.language]!! + 1
                }

                for ((lang, count) in languagesList) {
                    // Add entry
                    entries.add(PieEntry(count.toFloat(), lang))

                    // Add corresponding color from json
                    if (lang == "Unknown") {
                        diagramColors.add(Color.parseColor("#C3C3C3"))
                    } else {
                        val color = languagesColorsList[lang]!!["color"]
                        if (color != null) {
                            diagramColors.add(Color.parseColor(color))
                        } else {
                            // FIXME
                        }
                    }

                }


                val dataSet = PieDataSet(entries, "")
                dataSet.apply {
                    colors = diagramColors
                    valueTextSize = 20f
                    valueTextColor = Color.WHITE
                    valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }


                val pieData = PieData(dataSet)
                // Remove decimal part from value
                pieData.setValueFormatter(CustomValueFormatter())


                // Legend settings
                val legend: Legend = binding.languagesChart.getLegend()
                legend.apply {
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    setDrawInside(false)
                    isWordWrapEnabled = true
                    textSize = 20f
                    xEntrySpace = 20f
                }


                // General chart settings
                binding.languagesChart.apply {
                    setEntryLabelTextSize(16f)
                    setDrawSliceText(false)
                    description.isEnabled = false
                    setCenterTextSize(30f)

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
        viewModel.getSyncStatusLData().observe(viewLifecycleOwner, {

            if (it == SyncStatus.PENDING) {
                binding.syncStatusBtn.isClickable = false
                binding.syncStatusBtn.text = getString(R.string.loading)
                binding.syncStatusBtn.setBackgroundResource(R.drawable.background_sync_button_pending)
            }

            else if (it == SyncStatus.SUCCESS) {
                binding.syncStatusBtn.isClickable = true
                binding.syncStatusBtn.text = getString(R.string.synced)
                binding.syncStatusBtn.setBackgroundResource(R.drawable.background_sync_button_synced)
            }

            else if (it == SyncStatus.FAILED) {
                // FIXME currently disabled. Fix when implement refresh
                binding.syncStatusBtn.isClickable = true
                binding.syncStatusBtn.text = getString(R.string.failed)
                binding.syncStatusBtn.setBackgroundResource(R.drawable.background_sync_button_failed)
            }

        })


        // Refresh the data on status btn click
        binding.syncStatusBtn.setOnClickListener {
            // Just renavigate to this fragment
            navController.navigate(R.id.reposFragment)
        }
    }


    private fun hideRepositoriesViews() {
        binding.reposCountCard.visibility = View.GONE
        binding.languagesCard.visibility = View.GONE
    }

    private fun showRepositoriesViews() {
        binding.reposCountCard.visibility = View.VISIBLE
        binding.languagesCard.visibility = View.VISIBLE
    }
}


class CustomValueFormatter(): ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "" + value.toInt()
    }
}