import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/user/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/user/Register.vue')
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'repair/submit',
        name: 'RepairSubmit',
        component: () => import('../views/resident/RepairSubmit.vue')
      },
      {
        path: 'repair/my-orders',
        name: 'MyOrders',
        component: () => import('../views/resident/MyOrders.vue')
      },
      {
        path: 'repair/detail/:id',
        name: 'RepairDetail',
        component: () => import('../views/resident/RepairDetail.vue')
      },
      {
        path: 'repairer/tasks',
        name: 'MyTasks',
        component: () => import('../views/repairer/MyTasks.vue')
      },
      {
        path: 'repairer/task/:id',
        name: 'TaskDetail',
        component: () => import('../views/repairer/TaskDetail.vue')
      },
      {
        path: 'admin/users',
        name: 'UserManage',
        component: () => import('../views/admin/UserManage.vue')
      },
      {
        path: 'admin/dispatch',
        name: 'DispatchManage',
        component: () => import('../views/admin/DispatchManage.vue')
      },
      {
        path: 'admin/statistics',
        name: 'Statistics',
        component: () => import('../views/admin/Statistics.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && to.path !== '/register' && !token) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && token) {
    next('/')
  } else {
    next()
  }
})

export default router
