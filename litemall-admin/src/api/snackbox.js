import request from '@/utils/request'

export function createSnackbox(data) {
  return request({
    url: '/snackbox/create',
    method: 'post',
    data
  })
}
export function listSnackbox(data) {
  return request({
    url: '/snackbox/list',
    method: 'get'
  })
}
export function deleteSnackbox(id) {
  return request({
    url: '/snackbox/delete/'+id,
    method: 'post'
  })
}
export function updateSnackbox(data) {
  return request({
    url: '/snackbox/update',
    method: 'post',
		data
  })
}
export function getSnackbox(id) {
  return request({
    url: '/snackbox/get/'+id,
    method: 'get'
  })
}

export function queryGoodsByName(name){
	return request({
	  url: '/snackbox/queryGoodsByName',
	  method: 'get',
		params: {'name':name}
	})
}
export function downLoadQRCode(id){
	return request({
	  url: '/snackbox/downQRCode',
		responseType:'blob',
	  method: 'get',
		params: {'id':id}
	})
}


