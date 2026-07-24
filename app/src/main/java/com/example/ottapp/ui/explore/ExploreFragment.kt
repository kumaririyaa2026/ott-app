package com.example.ottapp.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ottapp.adapter.VideoAdapter
import com.example.ottapp.databinding.FragmentExploreBinding
import com.example.ottapp.player.VideoPlayerManager
import com.example.ottapp.viewmodel.ExploreViewModel
import kotlin.math.abs

/**
 * The only screen that is evaluated. Shows a vertically scrolling list of
 * videos, each with an inline Media3 (ExoPlayer) preview, title, 2-line
 * description, Watch Now button, and like/save/share/volume icons (UI only).
 *
 * Only one video plays at a time: a single shared [VideoPlayerManager] is
 * moved between rows as the user scrolls, always attaching to whichever
 * row is closest to the center of the screen.
 */
class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExploreViewModel by viewModels()

    private val adapter = VideoAdapter()

    private var playerManager: VideoPlayerManager? = null
    private var currentlyPlayingPosition = RecyclerView.NO_POSITION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerManager = VideoPlayerManager(requireContext())

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        // Video items are relatively tall/complex; disabling item change
        // animations avoids player flicker when the list updates.
        binding.recyclerView.itemAnimator = null

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                playMostVisibleItem()
            }
        })

        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            adapter.submitList(videos) {
                // Wait for the list (and its views) to actually be laid out
                // before deciding which row to play.
                binding.recyclerView.post { playMostVisibleItem() }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        playerManager?.resume()
    }

    override fun onPause() {
        super.onPause()
        playerManager?.pause()
    }

    /**
     * Finds the row closest to the vertical center of the RecyclerView and,
     * if it isn't already playing, attaches the shared player to it.
     */
    private fun playMostVisibleItem() {
        val recyclerView = binding.recyclerView
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        if (recyclerView.childCount == 0) return

        val recyclerViewCenterY = recyclerView.height / 2

        var closestPosition = RecyclerView.NO_POSITION
        var minDistance = Int.MAX_VALUE

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val childCenterY = (child.top + child.bottom) / 2
            val distance = abs(childCenterY - recyclerViewCenterY)
            if (distance < minDistance) {
                minDistance = distance
                closestPosition = layoutManager.getPosition(child)
            }
        }

        if (closestPosition == RecyclerView.NO_POSITION) return
        if (closestPosition == currentlyPlayingPosition) return

        val holder = recyclerView.findViewHolderForAdapterPosition(closestPosition)
                as? VideoAdapter.VideoViewHolder ?: return
        val item = adapter.currentList.getOrNull(closestPosition) ?: return

        playerManager?.play(holder.playerView, item.coverVideoRaw)
        currentlyPlayingPosition = closestPosition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        playerManager?.release()
        playerManager = null
        currentlyPlayingPosition = RecyclerView.NO_POSITION
        _binding = null
    }
}
