package com.richmondprojects.stockmarketapp.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.richmondprojects.stockmarketapp.presentation.destinations.DetailScreenDestination

@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = homeViewModel.state.isRefreshing)

    val state = homeViewModel.state

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                homeViewModel.onEvents(HomeEvents.OnSearchQueryChanged(it))
            },
            placeholder = { Text(text = "Search...") },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { homeViewModel.onEvents(HomeEvents.Refresh) }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.company.size) { i ->
                    val result = state.company[i]
                    ListContent(
                        company = result, modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { navigator.navigate(DetailScreenDestination(result.symbol)) }
                    )
                    if (i < state.company.size) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }

}