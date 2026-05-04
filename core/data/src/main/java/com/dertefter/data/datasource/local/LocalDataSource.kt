package com.dertefter.data.datasource.local

import com.dertefter.data.datasource.local.room.entity.NewsEntity
import com.dertefter.data.datasource.local.room.entity.NewsRemoteKey
import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.dto.auth.AuthCreditions
import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.dto.money.MoneyItemDto
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.data.dto.news.PromoItem
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingSource

interface LocalDataSource {

    fun getNewsPagingSource(): PagingSource<Int, NewsEntity>

    suspend fun getRemoteKeyForNewsId(newsId: String): NewsRemoteKey?

    suspend fun saveNewsPage(
        news: List<NewsItem>,
        keys: List<NewsRemoteKey>,
        isRefresh: Boolean,
        page: Int,
        pageSize: Int
    )

    fun getContactInfo(): Flow<ContactInfoDto?>

    suspend fun saveContactInfo(contactInfoDto: ContactInfoDto?)

    fun getUserInfo(): Flow<UserInfoDto?>

    suspend fun saveUserInfo(userInfoDto: UserInfoDto?)

    suspend fun switchToLogin(login: String)

    suspend fun logout()

    fun getCurrentLogin(): Flow<String?>

    suspend fun saveCurrentGroup(groupDto: GroupDto?)

    fun getCurrentGroup(): Flow<GroupDto?>

    suspend fun saveTimeSlotsForGroup(groupDto: GroupDto, timeSlots: List<TimeSlotDto>)

    fun getTimeSlotsForGroup(groupDto: GroupDto): Flow<List<TimeSlotDto>?>

    suspend fun saveSessiaSchedule(groupDto: GroupDto, timeSlots: List<TimeSlotDto>)

    fun getSessiaSchedule(groupDto: GroupDto): Flow<List<TimeSlotDto>?>


    suspend fun saveWeekBounds(weekBounds: List<WeekBoundsDto>)

    fun getWeekBounds(): Flow<List<WeekBoundsDto>?>

    fun getWeekHeader(): Flow<String?>

    suspend fun saveWeekHeader(header: String)

    fun getAuthCreds(): Flow<AuthCreditions?>

    suspend fun saveAuthCreds(authCreds: AuthCreditions?)

    fun getGroupHistory(): Flow<List<GroupDto>>

    suspend fun saveGroupHistory(history: List<GroupDto>)

    suspend fun removeFromAccountHistory(login: String)

    suspend fun addToAccountHistory(login: String)

    fun getLoginHistory(): Flow<List<String>>

    suspend fun saveMessages(messages: List<MessageDto>)

    suspend fun updateMessage(message: MessageDto)

    fun getMessages(): Flow<List<MessageDto>>

    fun getMessageById(idMessage: Long): Flow<MessageDto?>

    suspend fun deleteMessage(idMessage: Long)

    fun getPersonDetailById(personId: Long): Flow<PersonDetailDto?>

    fun getPersonDetailsByIds(personIds: List<Long>): Flow<List<PersonDetailDto>>

    suspend fun savePersonDetail(personDetailDto: PersonDetailDto)

    fun getSessiaResults(): Flow<List<SessiaResultDto>?>

    suspend fun saveSessiaResults(sessiaResults: List<SessiaResultDto>)

    fun getShareScoreLink(): Flow<String?>

    suspend fun saveShareScoreLink(link: String?)

    fun getSelectedRemoteDataSource(): Flow<PreferredRemoteSource>

    suspend fun setSelectedRemoteDataSource(p: PreferredRemoteSource)

    fun getThemeColor(): Flow<Long?>

    suspend fun saveThemeColor(color: Long?)

    fun getIsShapeCut(): Flow<Boolean?>

    suspend fun saveIsShapeCut(isCut: Boolean)

    fun getIsNotificationEnabled(): Flow<Boolean?>

    suspend fun saveIsNotificationEnabled(isEnabled: Boolean)

    fun getIsMessagesAlertSkipped(): Flow<Boolean?>

    suspend fun saveIsMessagesAlertSkipped(isSkipped: Boolean)

    fun getPromo(): Flow<List<PromoItem>?>

    suspend fun savePromo(promoList: List<PromoItem>)

    fun getEvents(): Flow<List<EventDto>?>

    suspend fun saveEvents(eventList: List<EventDto>)

    fun getMoneyForYear(year: String): Flow<List<MoneyItemDto>?>

    fun getMoneyYears(): Flow<List<String>?>

    suspend fun saveMoneyForYear(year: String, money: List<MoneyItemDto>)

    suspend fun saveMoneyYears(years: List<String>)

    fun getLksList(): Flow<List<LksDto>?>

    suspend fun saveLksList(lksList: List<LksDto>?)

    fun getDocsList(): Flow<List<DocsItemDto>?>

    suspend fun saveDocsList(docsList: List<DocsItemDto>?)

}
