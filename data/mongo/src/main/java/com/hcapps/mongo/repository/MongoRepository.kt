package com.hcapps.mongo.repository

import com.hcapps.util.model.Journal
import com.hcapps.util.model.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Journals = RequestState<Map<LocalDate, List<Journal>>>

interface MongoRepository {

    fun configureTheRealm()

    fun getAllJournals(): Flow<Journals>

    fun getFilteredJournals(zonedDateTime: ZonedDateTime): Flow<Journals>

    fun getSelectedJournal(journalId: ObjectId): Flow<RequestState<Journal>>

    suspend fun insertJournal(journal: Journal): RequestState<Journal>

    suspend fun updateJournal(journal: Journal): RequestState<Journal>

    suspend fun deleteJournal(id: ObjectId): RequestState<Journal>

    suspend fun deleteAllJournals(): RequestState<Boolean>

}