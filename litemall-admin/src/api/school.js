import request from '@/utils/request'

export function publishSchool(data) {
  return request({
    url: '/school/create',
    method: 'post',
    data
  })
}

export function listSchool(query) {
  return request({
    url: '/school/list',
    method: 'get',
    params: query
  })
}

export function getSchool(id) {
  return request({
    url: '/school/get/'+id,
    method: 'get'
  })
}

export function deleteSchool(id) {
  return request({
    url: '/school/delete/'+id,
    method: 'post'
  })
}

export function updateSchool(data) {
  return request({
    url: '/school/update',
    method: 'post',
		data
  })
}

export function queryAllSchool() {
  return request({
    url: '/school/queryAll',
    method: 'get'
  })
}

