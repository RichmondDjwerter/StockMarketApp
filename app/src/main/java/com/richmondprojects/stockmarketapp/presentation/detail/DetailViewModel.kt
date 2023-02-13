package com.richmondprojects.stockmarketapp.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmondprojects.stockmarketapp.domain.repository.Repository
import com.richmondprojects.stockmarketapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf((DetailState()))

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol = symbol) }
            val companyIntradayResult = async { repository.getIntradayInfo(symbol = symbol) }

            when (val result = companyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        company = null,
                        error = result.message,
                        isLoading = false
                    )
                }
                else -> Unit
            }
            when (val result = companyIntradayResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockInfo = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
                else -> Unit
            }
        }

    }
}