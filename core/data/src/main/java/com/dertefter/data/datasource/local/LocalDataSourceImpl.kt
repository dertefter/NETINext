package com.dertefter.data.datasource.local


import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.dertefter.data.datasource.local.room.AppDatabase
import com.dertefter.data.datasource.local.room.entity.AccountEntity
import com.dertefter.data.datasource.local.room.entity.GlobalConfigEntity
import com.dertefter.data.datasource.local.room.entity.MessageEntity
import com.dertefter.data.datasource.local.room.entity.MoneyEntity
import com.dertefter.data.datasource.local.room.entity.NewsEntity
import com.dertefter.data.datasource.local.room.entity.NewsRemoteKey
import com.dertefter.data.datasource.local.room.entity.PersonEntity
import com.dertefter.data.datasource.local.room.entity.ScheduleEntity
import com.dertefter.data.datasource.local.room.entity.SessiaScheduleEntity
import com.dertefter.data.datasource.local.room.entity.toEntity
import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.dto.auth.AuthCreditions
import com.dertefter.data.dto.control_weeks.ControlWeekDto
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
import com.dertefter.data.dto.schedule.asLowercase
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.settings.ThemeStyle
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class LocalDataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val encryptedAuthStorage: EncryptedAuthStorage,
    private val datastoreManager: DatastoreManager
) : LocalDataSource {

    private val accountDao = appDatabase.accountDao()
    private val messageDao = appDatabase.messageDao()
    private val scheduleDao = appDatabase.scheduleDao()
    private val sessiaScheduleDao = appDatabase.sessiaScheduleDao()
    private val globalConfigDao = appDatabase.globalConfigDao()
    private val newsDao = appDatabase.newsDao()
    private val newsRemoteKeyDao = appDatabase.newsRemoteKeyDao()
    private val moneyDao = appDatabase.moneyDao()

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

    private suspend fun getCurrentLoginValue(): String = datastoreManager.getCurrentLogin().first() ?: GUEST_LOGIN

    override fun getCurrentLogin(): Flow<String?> = datastoreManager.getCurrentLogin()

    override suspend fun switchToLogin(login: String) {
        datastoreManager.setCurrentLogin(login)
        if (login.isNotEmpty()) {
            val account = accountDao.getAccount(login).first()
            if (account == null) {
                accountDao.insertAccount(AccountEntity(login = login))
            }
        }
    }

    override suspend fun logout() {
        datastoreManager.setCurrentLogin(null)
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

    override suspend fun saveSessiaSchedule(groupDto: GroupDto, timeSlots: List<TimeSlotDto>) {
        val login = getCurrentLoginValue()
        sessiaScheduleDao.insertSessiaSchedule(SessiaScheduleEntity(login, groupDto.asLowercase().name, timeSlots))
    }

    override fun getSessiaSchedule(groupDto: GroupDto): Flow<List<TimeSlotDto>?> {
        return getCurrentLogin().flatMapLatest { login ->
            sessiaScheduleDao.getSessiaSchedule(login ?: GUEST_LOGIN, groupDto.asLowercase().name).map { it?.timeSlots }
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
        return getCurrentLogin().map { login ->
            if (login == null) null
            else encryptedAuthStorage.getAuthCreds(login)
        }
    }

    override suspend fun saveAuthCreds(authCreds: AuthCreditions?) {
        val login = getCurrentLoginValue()
        if (login != GUEST_LOGIN) {
            if (authCreds != null) {
                encryptedAuthStorage.saveAuthCreds(login, authCreds)
            } else {
                encryptedAuthStorage.deleteAuthCreds(login)
            }
        }
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
        encryptedAuthStorage.deleteAuthCreds(login)
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

    override fun getControlWeeks(): Flow<List<ControlWeekDto>?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.controlWeeks }
        }
    }

    override suspend fun saveControlWeeks(sessiaResults: List<ControlWeekDto>) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(controlWeeks = sessiaResults))
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

    override fun getThemeStyle(): Flow<ThemeStyle> {
        return globalConfigDao.getThemeStyle().map { it ?: ThemeStyle.TonalSpot }
    }

    override suspend fun saveThemeStyle(themeStyle: ThemeStyle) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(themeStyle = themeStyle))
    }

    override fun getNewColorSpecVersion(): Flow<Boolean> {
        return globalConfigDao.getNewColorSpecVersion().map { it ?: true }
    }

    override suspend fun saveNewColorSpecVersion(newColorSpecVersion: Boolean) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(newColorSpecVersion = newColorSpecVersion))
    }

    override fun getIsShapeCut(): Flow<Boolean> {
        return globalConfigDao.getIsShapeCut().map { it ?: false }
    }

    override suspend fun saveIsShapeCut(isCut: Boolean) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(isShapeCut = isCut))
    }

    override fun getIsNotificationEnabled(): Flow<Boolean> {
        return globalConfigDao.getIsNotificationEnabled().map { it ?: false }
    }

    override suspend fun saveIsNotificationEnabled(isEnabled: Boolean) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(isNotificationEnabled = isEnabled))
    }

    override fun getIsMessagesAlertSkipped(): Flow<Boolean> {
        return globalConfigDao.getIsMessagesAlertSkipped().map { it ?: false }
    }

    override suspend fun saveIsMessagesAlertSkipped(isSkipped: Boolean) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(isMessagesAlertSkipped = isSkipped))
    }

    override fun getIsTgLinkShow(): Flow<Boolean> {
        return globalConfigDao.getIsTgLinkShow().map { it ?: true }
    }

    override suspend fun saveIsTgLinkShow(isTgLinkShow: Boolean) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(isTgLinkShow = isTgLinkShow))
    }

    override fun getPromo(): Flow<List<PromoItem>?> {
        return globalConfigDao.getPromoList().map { it?.promoList }
    }

    override suspend fun savePromo(promoList: List<PromoItem>) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(promoList = promoList))
    }

    override fun getEvents(): Flow<List<EventDto>?> {
        return globalConfigDao.getEvents().map { it?.eventList }
    }

    override suspend fun saveEvents(eventList: List<EventDto>) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(eventList = eventList))
    }

    override fun getWeekHeader(): Flow<String?> {
        return globalConfigDao.getWeekHeader()
    }

    override suspend fun saveWeekHeader(header: String) {
        val currentConfig = globalConfigDao.getConfig().first() ?: GlobalConfigEntity()
        globalConfigDao.insertConfig(currentConfig.copy(weekHeader = header))
    }

    override fun getMoneyForYear(year: String): Flow<List<MoneyItemDto>?> {
        return getCurrentLogin().flatMapLatest { login ->
            moneyDao.getMoney(login ?: GUEST_LOGIN, year).map { it?.moneyItems }
        }
    }

    override fun getMoneyYears(): Flow<List<String>?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.moneyYears }
        }
    }

    override suspend fun saveMoneyForYear(year: String, money: List<MoneyItemDto>) {
        val login = getCurrentLoginValue()
        moneyDao.insertMoney(MoneyEntity(login, year, money))
    }

    override suspend fun saveMoneyYears(years: List<String>) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(moneyYears = years))
    }

    override fun getLksList(): Flow<List<LksDto>?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.lksList }
        }
    }

    override suspend fun saveLksList(lksList: List<LksDto>?) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(lksList = lksList))
    }

    override fun getDocsList(): Flow<List<DocsItemDto>?> {
        return getCurrentLogin().flatMapLatest { login ->
            accountDao.getAccount(login ?: GUEST_LOGIN).map { it?.docsList }
        }
    }

    override suspend fun saveDocsList(docsList: List<DocsItemDto>?) {
        val login = getCurrentLoginValue()
        val account = accountDao.getAccount(login).first() ?: AccountEntity(login = login)
        accountDao.insertAccount(account.copy(docsList = docsList))
    }
}
