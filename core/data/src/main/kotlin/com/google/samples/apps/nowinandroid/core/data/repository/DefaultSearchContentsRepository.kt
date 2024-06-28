

package com.google.samples.apps.nowinandroid.core.data.repository

import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceDao
import com.google.samples.apps.nowinandroid.core.database.dao.NewsResourceFtsDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicFtsDao
import com.google.samples.apps.nowinandroid.core.database.model.PopulatedNewsResource
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.database.model.asFtsEntity
import com.google.samples.apps.nowinandroid.core.model.data.SearchResult
import com.google.samples.apps.nowinandroid.core.network.Dispatcher
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers.IO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultSearchContentsRepository @Inject constructor(
    private val newsResourceDao: NewsResourceDao,
    private val newsResourceFtsDao: NewsResourceFtsDao,
    private val topicDao: TopicDao,
    private val topicFtsDao: TopicFtsDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : SearchContentsRepository {

    override suspend fun populateFtsData() {
        withContext(ioDispatcher) {
            newsResourceFtsDao.insertAll(
                newsResourceDao.getNewsResources(
                    useFilterTopicIds = false,
                    useFilterNewsIds = false,
                )
                    .first()
                    .map(PopulatedNewsResource::asFtsEntity),
            )
            topicFtsDao.insertAll(topicDao.getOneOffTopicEntities().map { it.asFtsEntity() })
        }
    }

    override fun searchContents(searchQuery: String): Flow<SearchResult> {
        // Surround the query by asterisks to match the query when it's in the middle of
        // a word
        val newsResourceIds = newsResourceFtsDao.searchAllNewsResources("*$searchQuery*")
        val topicIds = topicFtsDao.searchAllTopics("*$searchQuery*")

        val newsResourcesFlow = newsResourceIds
            .mapLatest { it.toSet() }
            .distinctUntilChanged()
            .flatMapLatest {
                newsResourceDao.getNewsResources(useFilterNewsIds = true, filterNewsIds = it)
            }
        val topicsFlow = topicIds
            .mapLatest { it.toSet() }
            .distinctUntilChanged()
            .flatMapLatest(topicDao::getTopicEntities)
        return combine(newsResourcesFlow, topicsFlow) { newsResources, topics ->
            SearchResult(
                topics = topics.map { it.asExternalModel() },
                newsResources = newsResources.map { it.asExternalModel() },
            )
        }
    }

    override fun getSearchContentsCount(): Flow<Int> =
        combine(
            newsResourceFtsDao.getCount(),
            topicFtsDao.getCount(),
        ) { newsResourceCount, topicsCount ->
            newsResourceCount + topicsCount
        }
}
