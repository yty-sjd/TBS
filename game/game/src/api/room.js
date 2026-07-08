import request from './request'

export function inviteFriend(fromUserId, toUserId) {
  return request({
    url: '/room/invite',
    method: 'post',
    data: { fromUserId, toUserId }
  })
}

export function acceptInvitation(invitationId, userId) {
  return request({
    url: '/room/accept',
    method: 'post',
    data: { invitationId, userId }
  })
}

export function rejectInvitation(invitationId, userId) {
  return request({
    url: '/room/reject',
    method: 'post',
    data: { invitationId, userId }
  })
}

export function getPendingInvitations(userId) {
  return request({
    url: '/room/invitations',
    method: 'get',
    params: { userId }
  })
}

export function checkRoomStatus(roomId) {
  return request({
    url: '/room/status',
    method: 'get',
    params: { roomId }
  })
}

export function setReady(roomId, userId, isHost, ready) {
  return request({
    url: '/room/ready',
    method: 'post',
    data: { roomId, userId, isHost, ready }
  })
}

export function leaveRoom(roomId, userId) {
  return request({
    url: '/room/leave',
    method: 'post',
    data: { roomId, userId }
  })
}

export function startGame(roomId, userId) {
  return request({
    url: '/room/start',
    method: 'post',
    data: { roomId, userId }
  })
}