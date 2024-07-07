package ru.skillbox.userservice.service;

import ru.skillbox.userservice.model.dto.AllFriendsDto;
import ru.skillbox.userservice.model.dto.FriendSearchDto;
import ru.skillbox.userservice.model.dto.RecommendationFriendsDto;

/**
 * Сервис для работы с "друзьями"
 */
public interface FriendService {
    /**
     * Удаление друга
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     * @param accountId         идентификатор аккаунта
     */
    void deleteFriend(Long currentAuthUserId, Long accountId);

    /**
     * Подтверждение дружбы
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     * @param accountId идентификатор аккаунта
     */
    void approveFriend(Long currentAuthUserId, Long accountId);

    /**
     * Блокировка друга
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     * @param accountId идентификатор аккаунта
     */
    void blockFriend(Long currentAuthUserId, Long accountId);

    /**
     * Добавление друга
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     * @param accountId         идентификатор аккаунта
     */
    void addFriend(Long currentAuthUserId, Long accountId);

    /**
     * Подписка
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     * @param accountId идентификатор аккаунта
     */
    void subscribe(Long currentAuthUserId, Long accountId);

    /**
     * Получение всех друзей
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     * @param friendSearchDto модель для поиска друзей
     * @return модель найденных друзей
     */
    AllFriendsDto getFriends(Long currentAuthUserId, FriendSearchDto friendSearchDto);

    /**
     * Получение друга по id
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     * @param accountId идентификатор аккаунта
     */
    void getFriendByAccountDto(Long currentAuthUserId, Long accountId);

    /**
     * Рекомендации
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     * @return модель рекомендаций
     */
    RecommendationFriendsDto getByRecommendation(Long currentAuthUserId);

    /**
     * Получение id друзей
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     */
    void getFriendId(Long currentAuthUserId);

    /**
     * Счетчик заявок в друзья
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     */
    void requestCount(Long currentAuthUserId);

    /**
     * Получение id заблокированных друзей
     *
     * @param currentAuthUserId идентификатор текущего авторизованного юзера
     */
    void getBlockFriendId(Long currentAuthUserId);
}
