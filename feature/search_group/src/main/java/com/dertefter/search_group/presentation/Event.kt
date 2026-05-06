package com.dertefter.search_group.presentation

import com.dertefter.data.dto.schedule.GroupDto

sealed class Event {

    data class OnSearchQueryChanged(val query: String) : Event()

    data class OnSelectGroup(val groupDto: GroupDto) : Event()

    data class OnRemoveGroupFromHistory(val groupDto: GroupDto) : Event()

    object OnClearGroupHistory : Event()

}
