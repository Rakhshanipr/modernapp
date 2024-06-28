

package com.google.samples.apps.nowinandroid.sync.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.samples.apps.nowinandroid.core.data.util.SyncManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val SYNC_TOPIC_SENDER = "/topics/sync"

@AndroidEntryPoint
internal class SyncNotificationsService : FirebaseMessagingService() {

    @Inject
    lateinit var syncManager: SyncManager

    override fun onMessageReceived(message: RemoteMessage) {
        if (SYNC_TOPIC_SENDER == message.from) {
            syncManager.requestSync()
        }
    }
}
