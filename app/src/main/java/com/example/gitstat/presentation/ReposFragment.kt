package com.example.gitstat

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
import com.example.gitstat.databinding.FragmentReposBinding
import com.example.gitstat.presentation.MainViewModel
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

    private lateinit var languagesList: TreeMap<String, Int>

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

        val viewModel = MainViewModel(requireActivity().application, "$user", "$token")


        viewModel.languagesLiveData.observe(viewLifecycleOwner, {

            binding.languagesChart.invalidate()

            // Read colors jsom from resources
            val inputStream = resources.openRawResource(R.raw.language_colors)
            val reader = InputStreamReader(inputStream)

            // Convert to map
            val builder = GsonBuilder()
            val itemsMapType = object : TypeToken<Map<String, Map<String, String>>>() {}.type
            val languagesColorsList: Map<String, Map<String, String>> = builder.create().fromJson(reader, itemsMapType)


            // Diagram data
            val entries = ArrayList<PieEntry>()
            val colors: MutableList<Int> = ArrayList()

            it.forEach() {
                // Add entry
                entries.add(PieEntry(it.reposCount.toFloat(), it.name))

                // Add corresponding color from json
                if (it.name == "Unknown") {
                    colors.add(Color.parseColor("#C3C3C3"))
                }
                else {
                    val color = languagesColorsList[it.name]!!["color"]
                    if (color != null) {
                        colors.add(Color.parseColor(color))
                    }
                    else {
                        // FIXME
                    }
                }

            }


            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors
            dataSet.valueTextSize = 20f
            dataSet.valueTextColor = Color.WHITE
            dataSet.valueTypeface = Typeface.defaultFromStyle(Typeface.BOLD);

            val data = PieData(dataSet)
            // Remove decimal part from value
            data.setValueFormatter(CustomValueFormatter())

            binding.languagesChart.setEntryLabelTextSize(16f)
            //binding.languagesChart.setUsePercentValues(true)
            binding.languagesChart.setDrawSliceText(false)
            binding.languagesChart.description.isEnabled = false;

            // Legend settings
            val l: Legend = binding.languagesChart.getLegend() // get legend of pie
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM// set vertical alignment for legend
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT // set horizontal alignment for legend
            l.orientation = Legend.LegendOrientation.HORIZONTAL// set orientation for legend
            l.setDrawInside(false)
            l.isWordWrapEnabled = true;
            l.textSize = 20f
            l.xEntrySpace = 20f


            // Should be in the end to display legend correctly
            binding.languagesChart.data = data

            binding.languagesChart.centerText = it.size.toString()
            binding.languagesChart.setCenterTextSize(30f)

        })

    }

}


class CustomValueFormatter(): ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "" + value.toInt()
    }
}