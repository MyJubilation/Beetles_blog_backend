package com.beetles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beetles.DTO.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    int register(User user);

    boolean getUserName(String userName);

    boolean getNickName(String nickName);

    boolean getUserId(Long id);


    @Select("SELECT \n" +
            " id, nick_name nickName, user_name userName\n" +
            "FROM\n" +
            "  sys_user\n" +
            "WHERE\n" +
            "  nick_name LIKE CONCAT('%', #{nickName}, '%')" +
            "   AND NOT id = #{userId}")
    List<Map<String, String>> selectFriendByNickName(String nickName, int userId);

    @Select("SELECT \n" +
            " id, nick_name nickName, user_name userName\n" +
            "FROM\n" +
            "  sys_user\n" +
            "WHERE\n" +
            "  user_name LIKE CONCAT('%', #{userName}, '%') AND NOT id = #{userId}")
    List<Map<String, String>> selectFriendByUserName(String userName, int userId);

    @Select("SELECT \n" +
            " id, nick_name nickName, user_name userName\n" +
            "FROM\n" +
            "  sys_user\n" +
            "WHERE\n" +
            "  id LIKE CONCAT('%', #{id}, '%') AND NOT id = #{userId}")
    List<Map<String, String>> selectFriendById(int id, int userId);

    @Select("SELECT \n" +
            " s.id, s.nick_name nickName, s.user_name userName\n" +
            "FROM\n" +
            "  add_friend_tb a JOIN sys_user s\n" +
            "WHERE\n" +
            "  a.friend_id = #{id} AND s.id = a.user_id AND a.status = '申请中' ")
    List<Map<String, String>> selectAcceptFriendsList(int id);

    @Select("SELECT \n" +
            " s.id, s.nick_name nickName, s.user_name userName, a.status\n" +
            "FROM\n" +
            "  add_friend_tb a JOIN sys_user s\n" +
            "WHERE\n" +
            "  a.user_id = #{id} AND s.id = a.friend_id")
    List<Map<String, String>> selectAddFriendsHistoryList(int id);

    @Select("SELECT \n" +
            " COUNT(*)\n" +
            "FROM\n" +
            "  add_friend_tb\n" +
            "WHERE\n" +
            "  user_id = #{userId} AND friend_id = #{friendId} AND status = #{status}")
    int selectFriendRequestHistoryStatus(int userId, int friendId, String status);

    @Insert("INSERT INTO add_friend_tb (user_id, friend_id, status, create_time, update_time)\n" +
            "VALUES (#{userId}, #{friendId}, #{alreadyHaveAddFriendRequest}, #{createTime}, #{updateTime})")
    Integer addFriendRequest(int userId, int friendId, String alreadyHaveAddFriendRequest, Date createTime, Date updateTime);

    @Insert("UPDATE add_friend_tb\n" +
            "SET status = #{status}, update_time = #{now}\n" +
            "WHERE user_id = #{friendId} AND friend_id = #{userId} AND status = '申请中'")
    Integer resolveFriendRequest(int userId, int friendId, String status, Date now);

    @Insert("INSERT INTO friend_list_role (user_id, friend_id, create_time, update_time)\n" +
            "VALUES (#{userId}, #{friendId}, #{now}, #{now})")
    Integer addFriend(int userId, int friendId, Date now);

    @Select("SELECT \n" +
            " COUNT(*)\n" +
            "FROM\n" +
            "  friend_list_role\n" +
            "WHERE\n" +
            "  user_id = #{userId} AND friend_id = #{friendId}")
    int selectFriendship(int userId, int friendId);

    @Select("SELECT id FROM sys_user WHERE user_name = #{userName}")
    int getUserIdByName(String userName);

    @Select("SELECT user_type FROM sys_user WHERE id = #{userId}")
    int getUserTypeByUserId(String userId);
}