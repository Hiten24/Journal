package com.hcapps.journal.data.repository

import com.hcapps.journal.model.Journal
import com.hcapps.journal.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Journals = RequestState<Map<LocalDate, List<Journal>>>

interface MongoRepository {

    fun configureTheRealm()

    fun getAllJournals(): Flow<Journals>

}