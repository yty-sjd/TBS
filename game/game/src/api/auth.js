import request from './request'

export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function updateBackground(data) {
  return request({
    url: '/auth/updateBackground',
    method: 'post',
    data
  })
}

export function updateAssistant(data) {
  return request({
    url: '/auth/updateAssistant',
    method: 'post',
    data
  })
}

export function updateBackgroundMusic(data) {
  return request({
    url: '/auth/updateBackgroundMusic',
    method: 'post',
    data
  })
}

export function verifyEmail(data) {
  return request({
    url: '/auth/verifyEmail',
    method: 'post',
    data
  })
}

export function updateAccount(data) {
  return request({
    url: '/auth/updateAccount',
    method: 'post',
    data
  })
}

export function resetPassword(data) {
  return request({
    url: '/auth/resetPassword',
    method: 'post',
    data
  })
}