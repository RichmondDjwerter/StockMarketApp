package com.richmondprojects.stockmarketapp.di

import com.richmondprojects.stockmarketapp.data.csv.CSVParser
import com.richmondprojects.stockmarketapp.data.csv.CompanyListingsParser
import com.richmondprojects.stockmarketapp.data.csv.IntradayInfoParser
import com.richmondprojects.stockmarketapp.data.repository.RepositoryImpl
import com.richmondprojects.stockmarketapp.domain.model.CompanyListing
import com.richmondprojects.stockmarketapp.domain.model.IntradayInfo
import com.richmondprojects.stockmarketapp.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsRepository(
        repositoryImpl: RepositoryImpl
    ): Repository

    @Binds
    @Singleton
    abstract fun bindsCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindsIntradayInfoParser(
        intradayInfo: IntradayInfoParser
    ): CSVParser<IntradayInfo>

}