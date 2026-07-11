package com.example.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.game.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

    @Query(value = """
        SELECT * FROM friend WHERE user_id = :userId AND status = 1
        UNION
        SELECT * FROM friend WHERE friend_id = :userId AND status = 1
        """, nativeQuery = true)
    List<Friend> findMyFriends(@Param("userId") Integer userId);

    List<Friend> findByFriendIdAndStatus(Integer friendId, Integer status);

    List<Friend> findByUserIdAndStatus(Integer userId, Integer status);

    @Query("SELECT f FROM Friend f WHERE " +
           "(f.userId = :a AND f.friendId = :b) OR (f.userId = :b AND f.friendId = :a)")
    Friend findRelation(@Param("a") Integer a, @Param("b") Integer b);
}