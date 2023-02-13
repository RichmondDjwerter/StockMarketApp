package com.richmondprojects.stockmarketapp.presentation.home


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmondprojects.stockmarketapp.domain.repository.Repository
import com.richmondprojects.stockmarketapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var state by mutableStateOf(HomeState())

    init {
        getCompanyListing()
    }

    var searchJob: Job? = null
    fun onEvents(events: HomeEvents) {
        when (events) {
            is HomeEvents.Refresh -> {
                getCompanyListing(fetchFromRemote = true)
            }
            is HomeEvents.OnSearchQueryChanged -> {
                state = state.copy(searchQuery = events.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListing()
                }
            }
        }
    }

    private fun getCompanyListing(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyListings(query, fetchFromRemote)
                .collect { list ->
                    when (list) {
                        is Resource.Success -> {
                            list.data?.let {
                                state = state.copy(
                                    company = it
                                )
                            }
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = list.isLoading)
                        }
                        is Resource.Error -> Unit
                    }
                }
        }
    }
}