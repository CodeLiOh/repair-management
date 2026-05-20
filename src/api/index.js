import request from './request'

// ========== Sprint 1: 用户 + 报修 API ==========

export const userApi = {
  login: (data) => request.post('/user/login', data),
  register: (data) => request.post('/user/register', data),
  getInfo: () => request.get('/user/info'),
  updateInfo: (data) => request.put('/user/info', data)
}

export const repairApi = {
  submit: (data) => request.post('/repair', data),
  listMyOrders: (status) => request.get('/repair/my', { params: { status } }),
  getDetail: (id) => request.get(`/repair/${id}`),
  cancel: (id) => request.put(`/repair/${id}/cancel`),
  listCategories: () => request.get('/repair/categories')
}
