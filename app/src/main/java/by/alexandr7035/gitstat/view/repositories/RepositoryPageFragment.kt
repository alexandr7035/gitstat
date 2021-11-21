package by.alexandr7035.gitstat.view.repositories

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.databinding.FragmentRepositoryPageBinding
import by.alexandr7035.gitstat.extensions.debug
import by.alexandr7035.gitstat.extensions.getClickableSpannable
import by.alexandr7035.gitstat.extensions.getStringDateFromLong
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.reflect.Array

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

            if (repoData != null) {

                Timber.debug("repo page ${repoData.description}")

                binding?.toolbar?.title = repoData.name
                binding?.repoName?.text = repoData.nameWithOwner
                binding?.language?.text = repoData.primaryLanguage
                binding?.stars?.text = repoData.stars.toString()

                binding?.repoDescription?.text = repoData.description

                // Set parent (if fork)
                if (repoData.isFork) {
                    binding?.parentName?.text = HtmlCompat.fromHtml(
                        getString(R.string.forked_from_template, repoData.parentNameWithOwner),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    binding?.parentName?.visibility = View.GONE
                }

                // Set website link if exists
                if (repoData.websiteUrl != "") {

                    // Make link clickable
                    val urlText = repoData.websiteUrl

                    val clickListener = View.OnClickListener {
                        Timber.debug("click link")
                        val webpage: Uri = Uri.parse(urlText)
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        startActivity(intent)
                    }

                    val spannableUrl = urlText.getClickableSpannable(
                        clickListener = clickListener,
                        clickableText = urlText,
                        isBold = false,
                        spannableColor = ContextCompat.getColor(requireContext(), R.color.blue_500)
                    )

                    binding?.websiteUrl?.text = spannableUrl

                    binding?.websiteUrl?.apply {
                        movementMethod = LinkMovementMethod.getInstance()
                        highlightColor = Color.TRANSPARENT
                    }

                } else {
                    binding?.websiteUrl?.visibility = View.GONE
                }

                // Change mark color depending on repo private/public
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

                (binding?.languageColorView?.background as GradientDrawable?)?.setColor(Color.parseColor(repoData.primaryLanguageColor))

                // Setup topics recycler
                val adapter = RepoTopicsAdapter()
                binding?.topicsRecycler?.layoutManager = FlexboxLayoutManager(requireContext())
                binding?.topicsRecycler?.adapter = adapter
                adapter.setItems(repoData.topics)

                binding?.createdAt?.text = repoData.created_at.getStringDateFromLong("dd.MM.yyyy HH:mm")
                binding?.updatedAt?.text = repoData.updated_at.getStringDateFromLong("dd.MM.yyyy HH:mm")
                binding?.repoSize?.text = getString(R.string.disk_usage_template, repoData.diskUsageKB)

                val langValues = ArrayList<Float>()
                val langColors = ArrayList<Int>()

                repoData.languages.forEach { repoLanguage ->
                    langValues.add(repoLanguage.size.toFloat())
                    langColors.add(Color.parseColor(repoLanguage.color))
                }

                binding?.languagesBar?.setValues(langValues, langColors)
                binding?.languagesBar?.invalidate()

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}