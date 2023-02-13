package com.richmondprojects.stockmarketapp.data.repository

import com.richmondprojects.stockmarketapp.data.csv.CSVParser
import com.richmondprojects.stockmarketapp.data.local.ListingsDatabase
import com.richmondprojects.stockmarketapp.data.mappers.toCompanyEntity
import com.richmondprojects.stockmarketapp.data.mappers.toCompanyInfo
import com.richmondprojects.stockmarketapp.data.mappers.toCompanyListing
import com.richmondprojects.stockmarketapp.data.remote.StockApi
import com.richmondprojects.stockmarketapp.domain.model.CompanyInfo
import com.richmondprojects.stockmarketapp.domain.model.CompanyListing
import com.richmondprojects.stockmarketapp.domain.model.IntradayInfo
import com.richmondprojects.stockmarketapp.domain.repository.Repository
import com.richmondprojects.stockmarketapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val api: StockApi,
    db: ListingsDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : Repository {

    private val dao = db.dao
    override suspend fun getCompanyListings(
        query: String,
        fetchFromRemote: Boolean
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localCache = dao.searchCompanyListing(query)
            emit(Resource.Success(localCache.map { it.toCompanyListing() }))

            val isDbEmpty = localCache.isEmpty() && query.isBlank()
            val shouldJustLoadFromDatabase = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromDatabase) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = e.localizedMessage))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message()))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyEntity() })

                emit(
                    Resource.Success(
                        data = dao
                            .searchCompanyListing("")
                            .map { it.toCompanyListing() })
                )
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val response = api.getCompanyInfo(symbol)
            Resource.Success(response.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage)

        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.message())
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val result = intradayInfoParser.parse(response.byteStream())
            Resource.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage)
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.message())
        }
    }

}