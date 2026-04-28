package com.dertefter.data.datasource.remote

import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.dto.money.MoneyItemDto
import com.dertefter.data.dto.news.NewsDetailDto
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.data.dto.news.PromoItem
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.dto.person.PersonShortDto
import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.ScheduleDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto


interface RemoteDataSource  {

    var preferredRemoteSource: PreferredRemoteSource


    suspend fun authorizeCiu(login: String, password: String): Result<Unit>

    suspend fun authorizeYourNeti(login: String, password: String): Result<Unit>

    fun logout()

    suspend fun getUserInfo(): Result<UserInfoDto?>

    suspend fun getContactInfo(): Result<ContactInfoDto>

    suspend fun saveContactInfo(
        email: String,
        address: String,
        phone: String,
        snils: String,
        oms: String,
        vk: String,
        tg: String,
        leader: String,
    ): Result<ContactInfoDto>

    suspend fun checkAuth(): Result<Unit>

    suspend fun getNewsForPage(page: Int): Result<List<NewsItem>>

    suspend fun getNewsDetail(id: String): Result<NewsDetailDto>

    suspend fun getSchedule(group: GroupDto): Result<ScheduleDto>

    suspend fun getEvents(year: String, month: String): Result<List<EventDto>>

    suspend fun getWeekHeader(): Result<String>

    suspend fun getSessiaSchedule(group: GroupDto): Result<List<TimeSlotDto>>

    suspend fun getSearchGroupResults(query: String): Result<List<GroupDto>>

    suspend fun getMessages(): Result<List<MessageDto>>

    suspend fun readMessage(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun unreadMessage(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun moveMessageToTrash(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun removeMessageFromTrash(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun deleteMessageForever(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun getPersonById(id: Long): Result<PersonDetailDto>


    suspend fun getSearchPersonResults(q: String): Result<List<PersonShortDto>>

    suspend fun getSessiaResults(): Result<List<SessiaResultDto>>

    suspend fun updateShareScoreLink(generateNew: Boolean): Result<String>


    suspend fun getPromo(): Result<List<PromoItem>>

    suspend fun getMoneyForYear(year: String): Result<List<MoneyItemDto>>

    suspend fun getYears(): Result<List<String>>

    suspend fun getLksList(): Result<List<LksDto>>

    suspend fun setSelectedLks(lksId: Int): Result<Unit>

}
