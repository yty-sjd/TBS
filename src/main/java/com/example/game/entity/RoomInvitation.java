package com.example.game.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_invitation")
public class RoomInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @Column(name = "from_user", nullable = false)
    private Integer fromUser;

    @Column(name = "to_user", nullable = false)
    private Integer toUser;

    @Column(name = "status", nullable = false)
    private Integer status = 0;

    @Column(name = "host_ready", nullable = false)
    private Integer hostReady = 0;

    @Column(name = "guest_ready", nullable = false)
    private Integer guestReady = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public RoomInvitation() {}

    public RoomInvitation(Integer roomId, Integer fromUser, Integer toUser, Integer status) {
        this.roomId = roomId;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }
    public Integer getFromUser() { return fromUser; }
    public void setFromUser(Integer fromUser) { this.fromUser = fromUser; }
    public Integer getToUser() { return toUser; }
    public void setToUser(Integer toUser) { this.toUser = toUser; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getHostReady() { return hostReady; }
    public void setHostReady(Integer hostReady) { this.hostReady = hostReady; }
    public Integer getGuestReady() { return guestReady; }
    public void setGuestReady(Integer guestReady) { this.guestReady = guestReady; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}