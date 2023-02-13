package com.richmondprojects.stockmarketapp.presentation.home

sealed class HomeEvents {
    object Refresh : HomeEvents()

    data class OnSearchQueryChanged(val query: String) : HomeEvents()
}
