package by.alexandr7035.gitstat.view.repositories.repo_details

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.getClickableSpannable
import by.alexandr7035.gitstat.core.extensions.getStringDateFromLong
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.databinding.FragmentRepositoryPageBinding
import by.alexandr7035.gitstat.view.repositories.RepositoriesViewModel
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoryPageFragment : Fragment(R.layout.fragment_repository_page) {

    private val binding by viewBinding(FragmentRepositoryPageBinding::bind)
    private val viewModel by viewModels<RepositoriesViewModel>()
    private val safeArgs by navArgs<RepositoryPageFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getRepositoryLiveData(safeArgs.repositoryId).observeNullSafe(viewLifecycleOwner) { repoData ->
            val clickableRepoText = repoData.nameWithOwner.getClickableSpannable(
                clickListener = {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        data = "https://github.com/${repoData.nameWithOwner}".toUri()
                    })
                },
                clickableText = repoData.nameWithOwner,
                isBold = true,
                spannableColor = ContextCompat.getColor(requireContext(), R.color.gray_500)
            )

            binding.repoName.apply {
                text = clickableRepoText
                movementMethod = LinkMovementMethod.getInstance()
                highlightColor = Color.TRANSPARENT
            }

            binding.toolbar.title = repoData.name
            binding.language.text = repoData.primaryLanguage
            binding.stars.text = repoData.stars.toString()

            binding.repoDescription.text = repoData.description

            // Set parent (if fork)
            if (repoData.isFork) {
                binding.parentName.text = HtmlCompat.fromHtml(
                    getString(R.string.forked_from_template, repoData.parentNameWithOwner),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                binding.parentName.visibility = View.GONE
            }

            // Set website link if exists
            if (repoData.websiteUrl != "") {
                // Make link clickable
                val urlText = repoData.websiteUrl

                val clickListener = View.OnClickListener {
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

                binding.websiteUrl.text = spannableUrl

                binding.websiteUrl.apply {
                    movementMethod = LinkMovementMethod.getInstance()
                    highlightColor = Color.TRANSPARENT
                }

            } else {
                binding.websiteUrl.visibility = View.GONE
            }

            // Change mark color depending on repo private/public
            when (repoData.isPrivate) {
                true -> {
                    binding.repoVisibility.text = getString(R.string.private_start_capital)
                    binding.repoVisibility.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )

                    binding.repoVisibility.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_repo_visibilily_private
                    )
                }

                else -> {
                    binding.repoVisibility.text = getString(R.string.public_start_capital)
                    binding.repoVisibility.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding.repoVisibility.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.background_repo_visibilily
                    )
                }
            }

            (binding.languageColorView.background as GradientDrawable?)?.setColor(Color.parseColor(repoData.primaryLanguageColor))

            // Setup topics recycler
            val topicsAdapter = RepoTopicsAdapter()
            binding.topicsRecycler.layoutManager = FlexboxLayoutManager(requireContext())
            binding.topicsRecycler.adapter = topicsAdapter
            topicsAdapter.setItems(repoData.topics)

            binding.createdAt.text = repoData.created_at.getStringDateFromLong("dd.MM.yyyy HH:mm")
            binding.updatedAt.text = repoData.updated_at.getStringDateFromLong("dd.MM.yyyy HH:mm")
            binding.repoSize.text = getString(R.string.disk_usage_template, repoData.diskUsageKB)


            // Setup languages bar
            val langValues = ArrayList<Float>()
            val langColors = ArrayList<Int>()

            repoData.languages.forEach { repoLanguage ->
                langValues.add(repoLanguage.size.toFloat())
                langColors.add(Color.parseColor(repoLanguage.color))
            }

            binding.languagesBar.setValues(langValues, langColors)
            binding.languagesBar.invalidate()

            // Setup legend for languages bar (recycler)
            val languagesAdapter = RepoLanguagesAdapter()
            binding.languagesRecycler.layoutManager = FlexboxLayoutManager(requireContext())
            binding.languagesRecycler.adapter = languagesAdapter
            languagesAdapter.setItems(repoData.languages)
        }
    }
}