package com.richmondprojects.stockmarketapp.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.richmondprojects.stockmarketapp.data.remote.dto.IntradayInfoDto
import com.richmondprojects.stockmarketapp.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun IntradayInfoDto.toIntradayDomain(): IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfo(
        close = close,
        date = localDateTime
    )
}