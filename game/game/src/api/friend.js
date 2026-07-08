import request from './request'

export function searchUser(keyword, userId) {
  return request({
    url: '/friend/search',
    method: 'get',
    params: { keyword, userId }
  })
}

export function addFriend(data) {
  return request({
    url: '/friend/add',
    method: 'post',
    data
  })
}

export function acceptFriend(data) {
  return request({
    url: '/friend/accept',
    method: 'post',
    data
  })
}

export function rejectFriend(data) {
  return request({
    url: '/friend/reject',
    method: 'post',
    data
  })
}

export function deleteFriend(data) {
  return request({
    url: '/friend/delete',
    method: 'post',
    data
  })
}

export function getFriendList(userId) {
  return request({
    url: '/friend/list',
    method: 'get',
    params: { userId }
  })
}

export function getFriendRequests(userId) {
  return request({
    url: '/friend/requests',
    method: 'get',
    params: { userId }
  })
}