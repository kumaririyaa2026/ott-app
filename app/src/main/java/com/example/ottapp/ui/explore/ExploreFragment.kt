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

// Main screen being evaluated. Shows a scrolling feed of video cards, each
// with title/description/watch now button and like/save/share/volume icons.
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
        binding.recyclerView.itemAnimator = null // avoids flicker on the player views

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                playMostVisibleItem()
            }
        })

        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            adapter.submitList(videos) {
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

    // figures out which row is closest to the center of the screen and
    // plays that one, so only one video plays at a time as you scroll
    private fun playMostVisibleItem() {
        val recyclerView = binding.recyclerView
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        if (recyclerView.childCount == 0) return

        val centerY = recyclerView.height / 2
        var closestPosition = RecyclerView.NO_POSITION
        var minDistance = Int.MAX_VALUE

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val childCenterY = (child.top + child.bottom) / 2
            val distance = abs(childCenterY - centerY)
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
