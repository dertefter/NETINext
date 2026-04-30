package com.dertefter.data.datasource.remote

import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.dto.docs.DocumentOptionItem
import com.dertefter.data.dto.docs.DocumentRequestItem
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
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val ciuRemoteDataSource: CiuRemoteDataSource,
    private val yourNetiRemoteDataSource: YourNetiRemoteDataSource
) : RemoteDataSource {

    companion object {
        const val TAG = "RemoteDataSourceImpl"
    }

    override var preferredRemoteSource: PreferredRemoteSource = PreferredRemoteSource.AUTO



    override suspend fun authorizeCiu(login: String, password: String): Result<Unit> {
        return ciuRemoteDataSource.authorizeCiu(login, password)

    }

    override suspend fun authorizeYourNeti(login: String, password: String): Result<Unit> {
        return yourNetiRemoteDataSource.authorizeYourNeti(login, password)

    }

    override fun logout() {
        ciuRemoteDataSource.logout()
        yourNetiRemoteDataSource.logout()
    }


    override suspend fun getUserInfo(): Result<UserInfoDto> {
        return if (preferredRemoteSource == PreferredRemoteSource.CIU){
            ciuRemoteDataSource.getUserInfo()
        } else {
            yourNetiRemoteDataSource.getUserInfo()

        }
    }

    override suspend fun getContactInfo(): Result<ContactInfoDto> {

        return if (preferredRemoteSource == PreferredRemoteSource.YOURNETI){
            yourNetiRemoteDataSource.getContactInfo()
        } else {
            ciuRemoteDataSource.getContactInfo()

        }


    }

    override suspend fun saveContactInfo(
        email: String,
        address: String,
        phone: String,
        snils: String,
        oms: String,
        vk: String,
        tg: String,
        leader: String,
    ): Result<ContactInfoDto> {
        return ciuRemoteDataSource.saveContactInfo(
            email = email,
            address = address,
            phone = phone,
            snils = snils,
            oms = oms,
            vk = vk,
            tg = tg,
            leader = leader
        )
    }

    override suspend fun checkAuth(): Result<Unit> {
        return ciuRemoteDataSource.checkAuth()
    }

    override suspend fun getNewsForPage(page: Int): Result<List<NewsItem>> {
        return ciuRemoteDataSource.getNewsForPage(page)
    }

    override suspend fun getNewsDetail(id: String): Result<NewsDetailDto> {
        return ciuRemoteDataSource.getNewsDetail(id)
    }

    override suspend fun getSchedule(group: GroupDto): Result<ScheduleDto> {
        return ciuRemoteDataSource.getSchedule(group)
    }

    override suspend fun getEvents(
        year: String,
        month: String
    ): Result<List<EventDto>> {
        return ciuRemoteDataSource.getEvents(year, month)
    }

    override suspend fun getWeekHeader(): Result<String> {
        return ciuRemoteDataSource.getWeekHeader()
    }

    override suspend fun getSessiaSchedule(group: GroupDto): Result<List<TimeSlotDto>> {
        return ciuRemoteDataSource.getSessiaSchedule(group)
    }

    override suspend fun getSearchGroupResults(query: String): Result<List<GroupDto>> {
        return ciuRemoteDataSource.getSearchGroupResults(query)
    }

    override suspend fun getMessages(): Result<List<MessageDto>> {
        if (preferredRemoteSource == PreferredRemoteSource.CIU){
            return ciuRemoteDataSource.getMessages()
        } else {
            return yourNetiRemoteDataSource.getMessages()
        }
    }


    override suspend fun readMessage(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        if (idStudent == null || preferredRemoteSource == PreferredRemoteSource.CIU){
            return ciuRemoteDataSource.readMessage(idMessage)
        } else {
            return yourNetiRemoteDataSource.readMessage(idStudent, idMessage)
        }
    }

    override suspend fun unreadMessage(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        if (idStudent == null){
            return Result.failure(Exception())
        }else{
            return yourNetiRemoteDataSource.unreadMessage(idStudent, idMessage)
        }
    }

    override suspend fun moveMessageToTrash(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        if (idStudent == null || preferredRemoteSource == PreferredRemoteSource.CIU){
            return ciuRemoteDataSource.moveMessageToTrash(idMessage)
        }else {
            return yourNetiRemoteDataSource.moveMessageToTrash(idStudent, idMessage)
        }

    }

    override suspend fun removeMessageFromTrash(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        if (idStudent == null || preferredRemoteSource == PreferredRemoteSource.CIU){
            return ciuRemoteDataSource.removeMessageToTrash(idMessage)
        } else {
            return yourNetiRemoteDataSource.removeMessageFromTrash(idStudent, idMessage)
        }
    }

    override suspend fun deleteMessageForever(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        if (idStudent == null || preferredRemoteSource == PreferredRemoteSource.CIU){
            return ciuRemoteDataSource.deleteMessageForever(idMessage)
        } else {
            //fallback до ciu
            return ciuRemoteDataSource.deleteMessageForever(idMessage)
        }
    }

    override suspend fun getPersonById(id: Long): Result<PersonDetailDto> {
        return ciuRemoteDataSource.getPersonById(id)
    }

    override suspend fun getSearchPersonResults(q: String): Result<List<PersonShortDto>> {
        return ciuRemoteDataSource.getSearchPersonResults(q)
    }

    override suspend fun getSessiaResults(): Result<List<SessiaResultDto>> {
        if (preferredRemoteSource == PreferredRemoteSource.YOURNETI){
            return yourNetiRemoteDataSource.getSessiaResults()
        } else {
            return ciuRemoteDataSource.getSessiaResults()
        }

    }

    override suspend fun updateShareScoreLink(generateNew: Boolean): Result<String> {
        return ciuRemoteDataSource.updateShareScoreLink(generateNew)
    }

    override suspend fun getPromo(): Result<List<PromoItem>> {
        return ciuRemoteDataSource.getPromo()
    }

    override suspend fun getMoneyForYear(year: String): Result<List<MoneyItemDto>> {
        return ciuRemoteDataSource.getMoneyForYear(year)
    }

    override suspend fun getYears(): Result<List<String>> {
        return ciuRemoteDataSource.getYears()
    }

    override suspend fun getLksList(): Result<List<LksDto>> {
        return ciuRemoteDataSource.getLksList()
    }

    override suspend fun setSelectedLks(lksId: Int): Result<Unit> {
        return ciuRemoteDataSource.setSelectedLks(lksId)
    }

    override suspend fun getDocsList(): Result<List<DocsItemDto>> {
        return ciuRemoteDataSource.getDocsList()
    }

    override suspend fun getOptionsList(): Result<List<DocumentOptionItem>> {
        return ciuRemoteDataSource.getOptionsList()
    }

    override suspend fun getDocumentRequestItem(typeDoc: String): Result<DocumentRequestItem> {
        return ciuRemoteDataSource.getDocumentRequestItem(typeDoc)
    }

    override suspend fun claimNewDocument(
        typeClaim: String,
        comment: String
    ): Result<Unit> {
        return ciuRemoteDataSource.claimNewDocument(typeClaim, comment)
    }

    override suspend fun checkCancelable(docId: String): Result<Boolean> {
        return ciuRemoteDataSource.checkCancelable(docId)
    }

    override suspend fun cancelDocument(docId: String): Result<Unit> {
        return ciuRemoteDataSource.cancelDocument(docId)
    }


}
