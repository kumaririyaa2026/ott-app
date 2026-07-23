package com.example.ottapp.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ottapp.data.model.VideoItem
import com.example.ottapp.data.repository.VideoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Explore screen (MVVM). Holds and exposes the list of
 * videos as LiveData and survives configuration changes independently of
 * the Fragment lifecycle.
 */
class ExploreViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    private val _videos = MutableLiveData<List<VideoItem>>()
    val videos: LiveData<List<VideoItem>> = _videos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _isLoading.value = true
            _videos.value = repository.getVideos()
            _isLoading.value = false
        }
    }
}
