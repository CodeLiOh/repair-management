import request from './request'

export const userApi = {
  login: (data) => request.post('/user/login', data),
  register: (data) => request.post('/user/register', data),
  getInfo: () => request.get('/user/info'),
  updateInfo: (data) => request.put('/user/info', data),
  listAll: () => request.get('/user/list'),
  updateStatus: (id, status) => request.put(`/user/${id}/status?status=${status}`),
  updateRole: (id, role) => request.put(`/user/${id}/role?role=${role}`),
  listRepairers: () => request.get('/user/repairers')
}

export const repairApi = {
  submit: (data) => request.post('/repair', data),
  listMyOrders: (status) => request.get('/repair/my', { params: { status } }),
  getDetail: (id) => request.get(`/repair/${id}`),
  cancel: (id) => request.put(`/repair/${id}/cancel`),
  listPending: () => request.get('/repair/pending'),
  listCategories: () => request.get('/repair/categories'),
  addCategory: (data) => request.post('/repair/categories', data)
}

export const dispatchApi = {
  dispatch: (data) => request.post('/dispatch', data),
  listMyTasks: (status) => request.get('/dispatch/my-tasks', { params: { status } }),
  getDetail: (id) => request.get(`/dispatch/${id}`),
  accept: (id) => request.put(`/dispatch/${id}/accept`),
  updateStatus: (id, status) => request.put(`/dispatch/${id}/status?status=${status}`),
  addLog: (id, data) => request.post(`/dispatch/${id}/log`, data),
  getLogs: (id) => request.get(`/dispatch/${id}/logs`),
  transfer: (id, reason) => request.put(`/dispatch/${id}/transfer?reason=${reason}`),
  reDispatch: (id, newRepairerId) => request.put(`/dispatch/${id}/re-dispatch?newRepairerId=${newRepairerId}`)
}

export const reviewApi = {
  submit: (data) => request.post('/review', data),
  getRepairerReviews: (id) => request.get(`/review/repairer/${id}`),
  getMyStats: () => request.get('/review/my-stats')
}

export const statisticsApi = {
  get: () => request.get('/statistics')
}
