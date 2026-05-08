import request from '@/utils/request'

export function getMemoList(params) {
  return request.get('/memos', { params })
}

export function getMemoDetail(id) {
  return request.get(`/memos/${id}`)
}

export function createMemo(data) {
  return request.post('/memos', data)
}

export function updateMemo(id, data) {
  return request.put(`/memos/${id}`, data)
}

export function deleteMemo(id) {
  return request.delete(`/memos/${id}`)
}

export function getTrashList(params) {
  return request.get('/memos/trash', { params })
}

export function restoreMemo(id) {
  return request.post(`/memos/${id}/restore`)
}

export function forceDeleteMemo(id) {
  return request.delete(`/memos/${id}/force`)
}
