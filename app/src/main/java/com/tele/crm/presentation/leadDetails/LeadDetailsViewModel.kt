package com.tele.crm.presentation.leadDetails

import androidx.lifecycle.ViewModel
import com.tele.crm.data.network.model.LeadsEntry
import com.tele.crm.data.network.model.leadDetails.History
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LeadDetailsViewModel : ViewModel() {
    private val _leadEntries = MutableStateFlow<List<History>>(emptyList())
    val leadEntries: StateFlow<List<History>> get() = _leadEntries.asStateFlow()

    // Mock data for testing
    fun loadMockData() {
        _leadEntries.update {
            listOf(
                History("25-01", "1m 9sec", "LOST"),
                History("26-01", "1m 78sec", "LOST"),
                History("26-01", "10m 9sec", "LOST"),
                History("26-01", "58m 9sec", "LOST"),

            )
        }
    }
}