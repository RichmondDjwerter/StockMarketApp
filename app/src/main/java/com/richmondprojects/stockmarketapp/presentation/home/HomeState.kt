package com.richmondprojects.stockmarketapp.presentation.home

import com.richmondprojects.stockmarketapp.domain.model.CompanyListing

data class HomeState(
    val company: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
