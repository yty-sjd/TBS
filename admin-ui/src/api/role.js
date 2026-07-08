import request from './request'

// 获取所有角色
export function getRoleList() {
  return request({
    url: '/admin/roles',
    method: 'get'
  })
}

// 获取单个角色详情
export function getRoleById(id) {
  return request({
    url: `/admin/roles/${id}`,
    method: 'get'
  })
}

// 创建角色
export function createRole(data) {
  return request({
    url: '/admin/roles',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(id, data) {
  return request({
    url: `/admin/roles/${id}`,
    method: 'put',
    data
  })
}

// 删除角色
export function deleteRole(id) {
  return request({
    url: `/admin/roles/${id}`,
    method: 'delete'
  })
}