package com.dertefter.neticlient

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.repository.AuthRepository
import com.dertefter.data.repository.GroupsRepository
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DataLayerListenerService : WearableListenerService() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var groupRepository: GroupsRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == com.google.android.gms.wearable.DataEvent.TYPE_CHANGED &&
                event.dataItem.uri.path == "/shared_data"
            ) {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap

                val xLogin = dataMap.getString("xLogin")
                val xPassword = dataMap.getString("xPassword")
                val groupName = dataMap.getString("group")?.split("=")?.get(0)
                val isGroupIndividual = dataMap.getString("group")?.split("=")?.get(1) == "true"

                serviceScope.launch {
                    if (xLogin != null && xPassword != null) {
                        authRepository.saveAuthCreds(xLogin, xPassword)
                    }

                    if (groupName != null) {
                        groupRepository.setCurrentGroup(GroupDto(groupName, isGroupIndividual))
                    }
                }

            }
        }
    }
}
