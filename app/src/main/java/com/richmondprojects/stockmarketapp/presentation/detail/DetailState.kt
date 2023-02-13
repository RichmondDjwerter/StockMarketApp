package com.richmondprojects.stockmarketapp.presentation.detail

import com.richmondprojects.stockmarketapp.domain.model.CompanyInfo
import com.richmondprojects.stockmarketapp.domain.model.IntradayInfo

data class DetailState(
    val company: CompanyInfo? = null,
    val stockInfo: List<IntradayInfo>? = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
