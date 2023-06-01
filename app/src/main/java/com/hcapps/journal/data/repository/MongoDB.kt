package com.hcapps.journal.data.repository

import com.hcapps.journal.model.Journal
import com.hcapps.journal.util.Constants.APP_ID
import com.hcapps.journal.model.RequestState
import com.hcapps.journal.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId
import java.time.ZonedDateTime

object MongoDB: MongoRepository {

    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Journal::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Journal>("ownerId == $0", user.id),
                        name = "User's Journals"
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }
    }

    override fun getAllJournals(): Flow<Journals> {
        return if (user != null) {
            try {
                realm.query<Journal>(query =  "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getFilteredJournals(zonedDateTime: ZonedDateTime): Flow<Journals> {
        return if (user != null) {
            try {
                realm.query<Journal>(
                    "ownerId == $0 AND date < $1 AND date > $2",
                    user.id,
                    RealmInstant.from(zonedDateTime.plusDays(1).toInstant().epochSecond, 0),
                    RealmInstant.from(zonedDateTime.minusDays(1).toInstant().epochSecond, 0)
                ).asFlow().map { result ->
                    RequestState.Success(
                        data = result.list.groupBy { it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() }
                    )
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedJournal(journalId: ObjectId): Flow<RequestState<Journal>> {
        return if (user != null) {
            try {
                realm.query<Journal>(query = "_id == $0", journalId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override suspend fun insertJournal(journal: Journal): RequestState<Journal> {
        return if (user != null) {
            realm.write {
                try {
                    val addedJournal = copyToRealm(journal.apply { ownerId = user.id })
                    RequestState.Success(data = addedJournal)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun updateJournal(journal: Journal): RequestState<Journal> {
        return if (user != null) {
            realm.write {
                val queriedJournal = query<Journal>(query = "_id == $0", journal._id).first().find()
                if (queriedJournal != null) {
                    queriedJournal.title = journal.title
                    queriedJournal.description = journal.description
                    queriedJournal.mood = journal.mood
                    queriedJournal.images = journal.images
                    queriedJournal.date = journal.date
                    RequestState.Success(data = queriedJournal)
                } else {
                    RequestState.Error(error = Exception("Queried Journal does not exist."))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteJournal(id: ObjectId): RequestState<Journal> {
        return if (user != null) {
            realm.write {
                val journal = query<Journal>(query = "_id == $0 AND ownerId == $1", id, user.id)
                    .first().find()
                if (journal != null) {
                    try {
                        delete(journal)
                        RequestState.Success(data = journal)
                    } catch (e: Exception) {
                        RequestState.Error(e)
                    }
                } else {
                    RequestState.Error(Exception("Journal does not exist."))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun deleteAllJournals(): RequestState<Boolean> {
        return if (user != null) {
            realm.write {
               val journals = this.query<Journal>("ownerId == $0", user.id).find()
                try {
                    delete(journals)
                    RequestState.Success(true)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }
}

private class UserNotAuthenticatedException: Exception("User is not Logged in.")