package com.richmondprojects.stockmarketapp.domain.repository

import com.richmondprojects.stockmarketapp.domain.model.CompanyInfo
import com.richmondprojects.stockmarketapp.domain.model.CompanyListing
import com.richmondprojects.stockmarketapp.domain.model.IntradayInfo
import com.richmondprojects.stockmarketapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getCompanyListings(
        query: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>

    suspend fun getIntradayInfo(
        symbol: String
    ): Resource<List<IntradayInfo >>
}