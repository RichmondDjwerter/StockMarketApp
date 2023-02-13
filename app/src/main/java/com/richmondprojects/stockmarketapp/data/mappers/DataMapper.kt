package com.richmondprojects.stockmarketapp.data.mappers

import com.richmondprojects.stockmarketapp.data.local.CompanyListingEntity
import com.richmondprojects.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.richmondprojects.stockmarketapp.domain.model.CompanyInfo
import com.richmondprojects.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        symbol = symbol,
        name = name,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        symbol = symbol,
        name = name,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        name = name ?: "",
        symbol = symbol ?: "",
        country = country ?: "",
        industry = industry ?: "",
        description = description ?: ""
    )
}