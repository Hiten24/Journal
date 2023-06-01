package com.hcapps.journal.data.repository

import com.hcapps.journal.model.Journal
import com.hcapps.journal.model.RequestState
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Journals = RequestState<Map<LocalDate, List<Journal>>>

interface MongoRepository {

    fun configureTheRealm()

    fun getAllJournals(): Flow<Journals>

    fun getSelectedJournal(journalId: ObjectId): Flow<RequestState<Journal>>

    suspend fun insertJournal(journal: Journal): RequestState<Journal>

    suspend fun updateJournal(journal: Journal): RequestState<Journal>

    suspend fun deleteJournal(id: ObjectId): RequestState<Journal>

    suspend fun deleteAllJournals(): RequestState<Boolean>

}