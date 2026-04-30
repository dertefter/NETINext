package com.dertefter.data.datasource.remote

import android.util.Log
import com.dertefter.data.common.AppError
import com.dertefter.data.common.AppException
import com.dertefter.data.datasource.remote.api.BaseApiService
import com.dertefter.data.datasource.remote.api.CiuApiService
import com.dertefter.data.datasource.remote.api.Login2ApiService
import com.dertefter.data.datasource.remote.api.parsers.parseFormParams
import com.dertefter.data.datasource.remote.api.parsers.parseMessages
import com.dertefter.data.datasource.remote.api.parsers.parseNews
import com.dertefter.data.datasource.remote.api.parsers.parseNewsDetail
import com.dertefter.data.datasource.remote.api.parsers.parsePersonDetail
import com.dertefter.data.datasource.remote.api.parsers.parsePersonShorts
import com.dertefter.data.datasource.remote.api.parsers.parseSchedule
import com.dertefter.data.datasource.remote.api.parsers.parseSearchGroupResults
import com.dertefter.data.datasource.remote.api.parsers.parseContactInfo
import com.dertefter.data.datasource.remote.api.parsers.parseDocCancelable
import com.dertefter.data.datasource.remote.api.parsers.parseDocsList
import com.dertefter.data.datasource.remote.api.parsers.parseDocsOptionsList
import com.dertefter.data.datasource.remote.api.parsers.parseEvents
import com.dertefter.data.datasource.remote.api.parsers.parseLksList
import com.dertefter.data.datasource.remote.api.parsers.parseMoneyItems
import com.dertefter.data.datasource.remote.api.parsers.parseMoneyYearList
import com.dertefter.data.datasource.remote.api.parsers.parsePromo
import com.dertefter.data.datasource.remote.api.parsers.parseSessiaResults
import com.dertefter.data.datasource.remote.api.parsers.parseSessiaSchedule
import com.dertefter.data.datasource.remote.api.parsers.parseShareScoreLink
import com.dertefter.data.datasource.remote.api.parsers.parseWeekHeader
import com.dertefter.data.datasource.remote.api.parsers.verifyAuth
import com.dertefter.data.dto.auth.Login2FormParams
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
import com.dertefter.data.dto.user.toUserInfoDto
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Named

class CiuRemoteDataSourceImpl @Inject constructor(
    private val login2ApiService: Login2ApiService,
    private val ciuApiService: CiuApiService,
    private val baseApiService: BaseApiService,
    @param:Named("ciu") private val ciuSessionCookieJar: SessionCookieJar,
) : CiuRemoteDataSource {

    companion object {
        const val TAG = "CiuRemoteDataSourceImpl"
    }


    override suspend fun authorizeCiu(login: String, password: String): Result<Unit> {
        return runCatching {
            ciuSessionCookieJar.cleanUp()
            getLogin2FormParams().onSuccess { params ->
                return requestAuthenticate(params, login, password)
            }.onFailure {
                throw AppException(AppError.Network.Unauthorized)
            }
        }

    }


    override fun logout() {
        ciuSessionCookieJar.cleanUp()
    }


    override suspend fun getUserInfo(): Result<UserInfoDto> {
        return runCatching {
            val html = ciuApiService.getContactInfo().string()
            parseContactInfo(html).toUserInfoDto()
        }
    }

    override suspend fun getContactInfo(): Result<ContactInfoDto> {
        return runCatching {
            val html = ciuApiService.getContactInfo().string()
            parseContactInfo(html)
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
        return runCatching {
            val html = ciuApiService.saveContactInfo(
                email = email,
                address = address,
                phone = phone,
                snils = snils,
                oms = oms,
                vk = vk,
                tg = tg,
                leader = leader
            ).string()
            parseContactInfo(html)
        }
    }

    override suspend fun checkAuth(): Result<Unit> {
        return runCatching {
            val response = ciuApiService.fetchBasePage()
            if (!verifyAuth(response.string())) {
                throw AppException(AppError.Network.Unauthorized)
            }
        }
    }

    override suspend fun getNewsForPage(page: Int): Result<List<NewsItem>> {
        return runCatching {
            val responseBody = baseApiService.getNews(page.toString())
            parseNews(responseBody)
        }
    }

    override suspend fun getNewsDetail(id: String): Result<NewsDetailDto> {
        return runCatching {
            val responseBody = baseApiService.getNewsDetail(id)
            parseNewsDetail(responseBody.string())
        }
    }

    private suspend fun loadSchedule(group: GroupDto): ResponseBody {
        return if (group.isIndividual){
            ciuApiService.getSchedule()
        } else {
            baseApiService.getSchedule(group.name)
        }
    }

    override suspend fun getSchedule(group: GroupDto): Result<ScheduleDto> {
        return runCatching {
            val firstDayDateResponseHtml = baseApiService.getScheduleForFirstDay(group.name).string()
            val basicSchedulePrintResponseHtml = baseApiService.getSchedule(group.name).string()
            val schedulePrintResponseHtml = loadSchedule(group).string()
            val schedule =
                parseSchedule(
                    schedulePrintResponseHtml = schedulePrintResponseHtml,
                    basicSchedulePrintResponseHtml = basicSchedulePrintResponseHtml,
                    scheduleFirstWeekRespHtml = firstDayDateResponseHtml,
                )

            val scheduleFallback =
                parseSchedule(
                    schedulePrintResponseHtml = basicSchedulePrintResponseHtml,
                    basicSchedulePrintResponseHtml = basicSchedulePrintResponseHtml,
                    scheduleFirstWeekRespHtml = firstDayDateResponseHtml,
                )
            if (schedule.timeSlots.isEmpty()){
                scheduleFallback
            }else {
               schedule
            }
        }.onFailure { e ->
            Log.e(TAG, e.stackTraceToString())
        }
    }

    override suspend fun getSessiaSchedule(group: GroupDto): Result<List<TimeSlotDto>> {
        return runCatching {
            val sessiaScheduleHtml = baseApiService.getSessiaSchedule(group = group.name)
            parseSessiaSchedule(sessiaScheduleHtml.string())
        }
    }

    override suspend fun getEvents(
        year: String,
        month: String
    ): Result<List<EventDto>> {
        return runCatching {
            val html = baseApiService.getEvents(year = year, month = month).string()
            parseEvents(html)

        }
    }

    override suspend fun getSearchGroupResults(query: String): Result<List<GroupDto>> {
        return runCatching {
            val response = baseApiService.getSearchGroupResults(query)
            parseSearchGroupResults(response.items)
        }
    }

    override suspend fun getWeekHeader(): Result<String> {
        return runCatching {
            val response = baseApiService.getBasePage()
            parseWeekHeader(response.string())
        }
    }

    override suspend fun getMessages(): Result<List<MessageDto>> {
        return runCatching {
            val undeletedMessagesUndeleted = ciuApiService.getUndeletedMessages()
            val deletedMessagesBody = ciuApiService.getDeletedMessages()
            parseMessages(undeletedMessagesUndeleted.string(), deletedMessagesBody.string())
        }

    }

    override suspend fun getSessiaResults(): Result<List<SessiaResultDto>> {
        return runCatching {
            val response = ciuApiService.getSessiaResults()
            parseSessiaResults(response.string())
        }

    }

    override suspend fun updateShareScoreLink(generateNew: Boolean): Result<String> {
        return runCatching {
            if (generateNew){
                ciuApiService.generateShareScoreLink(true).accessUrl
            } else {
                parseShareScoreLink(ciuApiService.getShareScoreLink().string())
            }

        }
    }

    override suspend fun getPromo(): Result<List<PromoItem>> {
        return runCatching {
            val response = baseApiService.getBasePage()
            parsePromo(response.string())
        }
    }

    override suspend fun getMoneyForYear(year: String): Result<List<MoneyItemDto>> {
        return runCatching {
            val response = ciuApiService.getMoneyForYear(year = year)
            parseMoneyItems(response.string())
        }
    }

    override suspend fun getYears(): Result<List<String>> {
        return runCatching {
            val response = ciuApiService.getMoneyYears()
            parseMoneyYearList(response.string())
        }
    }

    override suspend fun getLksList(): Result<List<LksDto>> {
        return runCatching {
            val response = ciuApiService.getBasePage()
            parseLksList(response.string())
        }
    }

    override suspend fun setSelectedLks(lksId: Int): Result<Unit> {
        return runCatching {
            ciuApiService.setSelectedLks(lksId)
        }
    }

    override suspend fun getDocsList(): Result<List<DocsItemDto>> {
        return runCatching {
            val response = ciuApiService.getDocuments()
            parseDocsList(response.string())
        }
    }

    override suspend fun getOptionsList(): Result<List<DocumentOptionItem>> {
        return runCatching {
            val response = ciuApiService.getDocuments()
            parseDocsOptionsList(response.string())
        }
    }

    override suspend fun getDocumentRequestItem(typeDoc: String): Result<DocumentRequestItem> {
        return runCatching {
            ciuApiService.getDocumentRequestItem(typeDoc = typeDoc)
        }
    }

    override suspend fun claimNewDocument(typeClaim: String, comment: String): Result<Unit> {
        return runCatching {
            ciuApiService.claimNewDocument(typeClaim = typeClaim, comment = comment)
        }
    }

    override suspend fun checkCancelable(docId: String): Result<Boolean> {
        return runCatching {
            val response = ciuApiService.checkDocCancelable(docId)
            parseDocCancelable(response.string())
        }
    }

    override suspend fun cancelDocument(docId: String): Result<Unit> {
        return runCatching {
            ciuApiService.cancelDocument(docId)
            Unit
        }
    }

    override suspend fun readMessage(messageId: Long): Result<Unit> {
        return runCatching {
             ciuApiService.getMessageById(messageId)
        }

    }

    override suspend fun moveMessageToTrash(idMessage: Long): Result<Unit> {
        return runCatching {
            ciuApiService.moveMessageToTrash(idMessage)
        }
    }

    override suspend fun removeMessageToTrash(idMessage: Long): Result<Unit> {
        return runCatching {
            ciuApiService.removeMessageFromTrash(idMessage)
        }
    }

    override suspend fun deleteMessageForever(idMessage: Long): Result<Unit> {
        return runCatching {
            ciuApiService.deleteMessageForever(idMessage)
        }
    }

    override suspend fun getPersonById(id: Long): Result<PersonDetailDto> {
        return runCatching {
            parsePersonDetail(ciuApiService.getPersonById(id).string(), id)
        }
    }

    override suspend fun getSearchPersonResults(q: String): Result<List<PersonShortDto>> {
        return runCatching {
            val response = baseApiService.getSearchPersonResults(q)
            parsePersonShorts(response.string())
        }
    }



    private suspend fun getLogin2FormParams(): Result<Login2FormParams> {
        return runCatching {
            val response = login2ApiService.getLogin2FromParams()
            val bodyString = response.string()
            val formParams = parseFormParams(bodyString)
            formParams
        }

    }

    private suspend fun requestAuthenticate(
        login2FormParams: Login2FormParams, login: String, password: String
    ): Result<Unit> {

        return runCatching {
            val paramLoginPassword = HashMap<String, String?>()
            paramLoginPassword["username"] = login
            paramLoginPassword["selected_subset"] = ""
            paramLoginPassword["username-visible"] = login
            paramLoginPassword["password"] = password
            paramLoginPassword["credentialId"] = ""

            val response = login2ApiService.authenticate(
                sessionCode = login2FormParams.sessionCode,
                execution = login2FormParams.execution,
                clientId = login2FormParams.clientId,
                tabId = login2FormParams.tabId,
                clientData = login2FormParams.clientData,
                username = login,
                password = password,
                usernameVisible = login,
                credentialId = "",
            )
            if (!verifyAuth(response.body()?.string().toString())) {
                throw AppException(AppError.Network.Unauthorized)
            }
        }


    }

}
