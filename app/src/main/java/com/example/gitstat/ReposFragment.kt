package com.example.gitstat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gitstat.databinding.FragmentReposBinding
import com.example.gitstat.model.RepositoryModel
import com.example.gitstat.model.UserModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.components.Legend








class ReposFragment : Fragment() {

    private val LOG_TAG = "DEBUG_TAG"
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var reposList: ArrayList<RepositoryModel>
    private lateinit var userModel: UserModel

    private lateinit var binding: FragmentReposBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentReposBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Shared pref
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        val user = sharedPreferences.getString(getString(R.string.shared_pref_login), "NONE")
        val token = sharedPreferences.getString(getString(R.string.shared_pref_token), "NONE")
        Log.d(LOG_TAG, "Auth '$user' with token '$token'")

        val viewModel = MainViewModel(requireActivity().application, "$user", "$token")


        viewModel.userLiveData.observe(viewLifecycleOwner, {
            if (it != null) {

                val reposCount = it.public_repos + it.total_private_repos

                binding.publicReposCountView.text = it.public_repos.toString()
                binding.privateReposCountView.text = it.total_private_repos.toString()

            }
        })


        viewModel.reposLiveData.observe(viewLifecycleOwner, {

            if (it != null) {
                var forksCount = 0

                it.forEachIndexed { i, repo ->
                    if (repo.fork) {
                        forksCount += 1
                    }
                }

                binding.forksCountView.text = forksCount.toString()
            }

        })


        viewModel.updateUserData()
        viewModel.updateRepositoriesData()

        testChart()
    }


    fun testChart() {

        val entries = ArrayList<PieEntry>()

        entries.add(PieEntry(100f, "Java"))
        entries.add(PieEntry(200f, "Python"))
        entries.add(PieEntry(200f, "C"))
        entries.add(PieEntry(100f, "Kotlin"))
        entries.add(PieEntry(100f, "Unknown"))


        val dataSet = PieDataSet(entries,   "")

        // Values
        dataSet.valueTextSize = 20f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD);

        val colors: MutableList<Int> = ArrayList<Int>()
        colors.add(Color.parseColor("#b07219"))
        colors.add(Color.parseColor("#3572A5"))
        colors.add(Color.parseColor("#555555"))
        colors.add(Color.parseColor("#F18E33"))
        colors.add(Color.parseColor("#C3C3C3"))
        dataSet.colors = colors


        val data: PieData = PieData(dataSet)
        

        binding.languagesChart.data = data

        binding.languagesChart.setEntryLabelTextSize(16f)
        //binding.languagesChart.setUsePercentValues(true)

        binding.languagesChart.setDrawSliceText(false)

        val l: Legend = binding.languagesChart.getLegend() // get legend of pie

        l.verticalAlignment =
            Legend.LegendVerticalAlignment.BOTTOM// set vertical alignment for legend

        l.horizontalAlignment =
            Legend.LegendHorizontalAlignment.LEFT // set horizontal alignment for legend

        l.orientation = Legend.LegendOrientation.HORIZONTAL// set orientation for legend

        l.setDrawInside(false) // set if legend should be drawn inside or not
        l.setWordWrapEnabled(true);
        l.textSize = 20f
        l.xEntrySpace = 20f

        binding.languagesChart.description.isEnabled = false;




    }
}