package com.dertefter.data.datasource.local

import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.datasource.local.room.AppDatabase
import com.dertefter.data.datasource.local.room.entity.AccountEntity
import com.dertefter.data.datasource.local.room.entity.GlobalConfigEntity
import com.dertefter.data.datasource.local.room.entity.MessageEntity
import com.dertefter.data.datasource.local.room.entity.PersonEntity
import com.dertefter.data.datasource.local.room.entity.ScheduleEntity
import com.dertefter.data.dto.auth.AuthCreditions
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.dto.news.PromoItem
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.schedule.asLowercase
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject


import com.dertefter.data.datasource.local.room.entity.NewsEntity
import com.dertefter.data.datasource.local.room.entity.NewsRemoteKey
import com.dertefter.data.datasource.local.room.entity.toEntity
import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.data.datasource.local.room.dao.NewsDao
import com.dertefter.data.datasource.local.room.dao.NewsRemoteKeyDao


@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : LocalDataSource {

    private val accountDao = appDatabase.accountDao()
    private val messageDao = appDatabase.messageDao()
    private val scheduleDao = appDatabase.scheduleDao()
    private val globalConfigDao = appDatabase.globalConfigDao()
    private val newsDao = appDatabase.newsDao()
    private val newsRemoteKeyDao = appDatabase.newsRemoteKeyDao()

    override fun getNewsPagingSource(): PagingSource<Int, NewsEntity> {
        return newsDao.getNews()
    }

    override suspend fun getRemoteKeyForNewsId(newsId: String): NewsRemoteKey? {
        return newsRemoteKeyDao.remoteKeysNewsId(newsId)
    }

    override suspend fun saveNewsPage(
        news: List<NewsItem>,
        keys: List<NewsRemoteKey>,
        isRefresh: Boolean,
        page: Int,
        pageSize: Int
    ) {
        appDatabase.withTransaction {
            if (isRefresh) {
                newsRemoteKeyDao.clearRemoteKeys()
                // Мы НЕ вызываем newsDao.clearAll(), чтобы UI не моргал пустым списком.
                // Старые новости будут заменены новыми по ID.
            }
            newsRemoteKeyDao.insertAll(keys)
            newsDao.insertAll(news.mapIndexed { index, newsItem ->
                newsItem.toEntity(page, (page - 1) * pageSize + index)
            })
        }
    }

    private val GUEST_LOGIN = ""

    private suspend fun getCurrentLoginValue(): String = globalConfigDao.getCurrentLogin().first() ?: GUEST_LOGIN

    override fun getCurrentLogin(): Flow<String?> = globalConfigDao.getCurrentLogin()

    override suspend fun switchToLogin(login: String) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(currentLogin = login))
        if (login.isNotEmpty()) {
            val account = accountDao.getAccount(login).first()
            if (account == null) {
                accountDao.insertAccount(AccountEntity(login = login))
            }
        }
    }

    override suspend fun logout() {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(currentLogin = null))
    }

    override fun getContactInfo(): Flow<ContactInfoDto?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.contactInfo }
        }
    }

    override suspend fun saveContactInfo(contactInfoDto: ContactInfoDto?) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(contactInfo = contactInfoDto))
    }

    override fun getUserInfo(): Flow<UserInfoDto?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.userInfo }
        }
    }

    override suspend fun saveUserInfo(userInfoDto: UserInfoDto?) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(userInfo = userInfoDto))
    }

    override suspend fun saveCurrentGroup(groupDto: GroupDto?) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(currentGroup = groupDto?.asLowercase()))
    }

    override fun getCurrentGroup(): Flow<GroupDto?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.currentGroup }
        }
    }

    override suspend fun saveTimeSlotsForGroup(groupDto: GroupDto, timeSlots: List<TimeSlotDto>) {
        val login = getCurrentLoginValue()
        scheduleDao.insertSchedule(ScheduleEntity(login, groupDto.asLowercase().name, timeSlots))
    }

    override fun getTimeSlotsForGroup(groupDto: GroupDto): Flow<List<TimeSlotDto>?> {
        return getCurrentLogin().flatMapLatest { login ->
            scheduleDao.getSchedule(login ?: GUEST_LOGIN, groupDto.asLowercase().name).map { it?.timeSlots }
        }
    }

    override suspend fun saveWeekBounds(weekBounds: List<WeekBoundsDto>) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(weekBounds = weekBounds))
    }

    override fun getWeekBounds(): Flow<List<WeekBoundsDto>?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.weekBounds }
        }
    }

    override fun getAuthCreds(): Flow<AuthCreditions?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.authCreds }
        }
    }

    override suspend fun saveAuthCreds(authCreds: AuthCreditions?) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(authCreds = authCreds))
    }

    override fun getGroupHistory(): Flow<List<GroupDto>> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.groupHistory ?: emptyList() }
        }
    }

    override suspend fun saveGroupHistory(history: List<GroupDto>) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(groupHistory = history))
    }

    override suspend fun removeFromAccountHistory(login: String) {
        accountDao.deleteAccount(login)
    }

    override suspend fun addToAccountHistory(login: String) {
        val account = accountDao.getAccount(login).first()
        if (account == null) {
            accountDao.insertAccount(AccountEntity(login = login))
        }
    }

    override fun getLoginHistory(): Flow<List<String>> {
        return accountDao.getLoginHistory().map { list -> list.filter { it.isNotEmpty() } }
    }

    override suspend fun saveMessages(messages: List<MessageDto>) {
        val login = getCurrentLoginValue()
        val entities = messages.map { MessageEntity(it.id, login, it) }
        messageDao.deleteMessages(login)
        messageDao.insertMessages(entities)
    }

    override suspend fun updateMessage(message: MessageDto) {
        val login = getCurrentLoginValue()
        messageDao.insertMessages(listOf(MessageEntity(message.id, login, message)))
    }

    override fun getMessages(): Flow<List<MessageDto>> {
        return getCurrentLogin().flatMapLatest { login ->
            messageDao.getMessages(login ?: GUEST_LOGIN).map { it.map { entity -> entity.messageDto } }
        }
    }

    override fun getMessageById(idMessage: Long): Flow<MessageDto?> {
        return getCurrentLogin().flatMapLatest { login ->
            messageDao.getMessageById(login ?: GUEST_LOGIN, idMessage).map { entity -> entity?.messageDto  }
        }
    }

    override suspend fun deleteMessage(idMessage: Long) {
        val login = getCurrentLoginValue()
        messageDao.deleteMessage(login, idMessage)
    }

    override fun getPersonDetailById(personId: Long): Flow<PersonDetailDto?> {
        return appDatabase.personDao().getPersonById(personId).map { it?.personDetailDto }
    }

    override fun getPersonDetailsByIds(personIds: List<Long>): Flow<List<PersonDetailDto>> {
        return appDatabase.personDao().getPersonsByIds(personIds).map { list -> list.map { it.personDetailDto } }
    }

    override suspend fun savePersonDetail(personDetailDto: PersonDetailDto) {
        appDatabase.personDao().insertPerson(PersonEntity(personDetailDto.personId, personDetailDto))
    }

    override fun getSessiaResults(): Flow<List<SessiaResultDto>?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.sessiaResults }
        }
    }

    override suspend fun saveSessiaResults(sessiaResults: List<SessiaResultDto>) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(sessiaResults = sessiaResults))
    }

    override fun getShareScoreLink(): Flow<String?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.shareScoreLink }
        }
    }

    override suspend fun saveShareScoreLink(link: String?) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(shareScoreLink = link))
    }

    override fun getSelectedRemoteDataSource(): Flow<PreferredRemoteSource> {
        return globalConfigDao.getPreferredRemoteSource().map { it ?: PreferredRemoteSource.AUTO }
    }

    override suspend fun setSelectedRemoteDataSource(p: PreferredRemoteSource) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(preferredRemoteSource = p))
    }

    override fun getThemeColor(): Flow<Long?> {
        return globalConfigDao.getThemeColor()
    }

    override suspend fun saveThemeColor(color: Long?) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(themeColor = color))
    }

    override fun getIsShapeCut(): Flow<Boolean?> {
        return globalConfigDao.getIsShapeCut()
    }

    override suspend fun saveIsShapeCut(isCut: Boolean) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(isShapeCut = isCut))
    }

    override fun getIsNotificationEnabled(): Flow<Boolean?> {
        return globalConfigDao.getIsNotificationEnabled()
    }

    override suspend fun saveIsNotificationEnabled(isEnabled: Boolean) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(isNotificationEnabled = isEnabled))
    }

    override fun getPromo(): Flow<List<PromoItem>?> {
        return globalConfigDao.getPromoList().map { it?.promoList }
    }

    override suspend fun savePromo(promoList: List<PromoItem>) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(promoList = promoList))
    }
}
