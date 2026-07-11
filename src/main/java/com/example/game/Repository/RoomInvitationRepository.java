package com.example.game.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.game.entity.RoomInvitation;

@Repository
public interface RoomInvitationRepository extends JpaRepository<RoomInvitation, Integer> {

    List<RoomInvitation> findByToUserAndStatus(Integer toUser, Integer status);

    List<RoomInvitation> findByFromUserAndStatus(Integer fromUser, Integer status);

    @Query("SELECT r FROM RoomInvitation r WHERE " +
           "(r.fromUser = :a AND r.toUser = :b AND r.status = 0) OR " +
           "(r.fromUser = :b AND r.toUser = :a AND r.status = 0)")
    List<RoomInvitation> findPendingInvitations(@Param("a") Integer a, @Param("b") Integer b);

    Optional<RoomInvitation> findByRoomIdAndStatus(Integer roomId, Integer status);

    List<RoomInvitation> findAllByRoomIdAndStatus(Integer roomId, Integer status);

    void deleteByCreatedAtBefore(LocalDateTime before);
}