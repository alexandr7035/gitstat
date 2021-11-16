package by.alexandr7035.gitstat.view.contributions_grid

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentContributionDayDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class ContributionDayDialogFragment : BottomSheetDialogFragment() {

    private var binding: FragmentContributionDayDialogBinding? = null
    private val safeArgs by navArgs<ContributionDayDialogFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentContributionDayDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.contributionsCount?.text = getString(
            R.string.daily_contributions_template,
            safeArgs.contributionsCount.toString()
        )

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        binding?.contributionDate?.text = dateFormat.format(safeArgs.contributionDate)

        (binding?.contributionMark?.background as GradientDrawable).setColor(safeArgs.contributionsColor)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}