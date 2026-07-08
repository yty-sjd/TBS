import request from './request'

export function startGame(roles) {
  return request.post('/game/start', roles, {
    headers: { 'Content-Type': 'text/plain' }
  })
}

export function doTurn(sessionId) {
  return request.post('/game/turn', null, { params: { sessionId } })
}

export function getRoom(sessionId) {
  return request.get('/game/room', { params: { sessionId } })
}

export function getLog(sessionId) {
  return request.get('/game/log', { params: { sessionId } })
}

export function startAndPlay(roles) {
  return request.post('/game/play', roles, {
    headers: { 'Content-Type': 'text/plain' }
  })
}

export function getRolesByIds(ids) {
  return request.post('/admin/roles/batch', ids)
}

export function getAllRoles() {
  return request.get('/admin/roles')
}