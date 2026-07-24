package com.example.ottapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ottapp.data.model.VideoItem
import com.example.ottapp.data.repository.VideoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Explore screen (MVVM). Holds and exposes the list of
 * videos as LiveData and survives configuration changes independently of
 * the Fragment lifecycle.
 *
 * Uses AndroidViewModel (rather than a plain ViewModel) purely so the
 * repository has an application Context available to read the bundled
 * dummy_response.json asset.
 */
class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VideoRepository(application)

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
