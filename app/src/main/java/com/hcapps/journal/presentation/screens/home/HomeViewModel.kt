package com.hcapps.journal.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.hcapps.journal.connectivity.ConnectivityObserver
import com.hcapps.journal.connectivity.NetworkConnectivityObserver
import com.hcapps.journal.data.repository.Journals
import com.hcapps.journal.data.repository.MongoDB
import com.hcapps.journal.model.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver
): ViewModel() {

    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    var journals: MutableState<Journals> = mutableStateOf(RequestState.Idle)

    init {
        observeAllJournals()
        viewModelScope.launch {
            connectivity.observer().collect {
                network = it
            }
        }
    }

    private fun observeAllJournals() {
        viewModelScope.launch {
            MongoDB.getAllJournals().collect { result ->
                journals.value = result
            }
        }
    }

    fun deleteAllJournals(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (network == ConnectivityObserver.Status.Available) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val imagesDirectory = "images/$userId"
            val storage = FirebaseStorage.getInstance().reference
            storage.child(imagesDirectory)
                .listAll()
                .addOnSuccessListener {
                    it.items.forEach { ref ->
                        val imagePath = "images/$userId/${ref.name}"
                        storage.child(imagePath).delete()
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        val result = MongoDB.deleteAllJournals()
                        if (result is RequestState.Success) {
                            withContext(Dispatchers.Main) {
                                onSuccess()
                            }
                        } else if (result is RequestState.Error) {
                            withContext(Dispatchers.Main) {
                                onError(result.error)
                            }
                        }
                    }
                }
                .addOnFailureListener { onError(it) }
        } else {
            onError(Exception("No Internet Connection."))
        }
    }

}