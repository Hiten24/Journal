package com.hcapps.journal.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hcapps.journal.data.repository.Journals
import com.hcapps.journal.data.repository.MongoDB
import com.hcapps.journal.util.RequestState
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    var journals: MutableState<Journals> = mutableStateOf(RequestState.Idle)

    init {
        observeAllJournals()
    }

    private fun observeAllJournals() {
        viewModelScope.launch {
            MongoDB.getAllJournals().collect { result ->
                journals.value = result
            }
        }
    }

}