<template>
  <el-container style="min-height:100vh">
    <el-aside width="220px" style="background:#304156">
      <div class="logo">
        <span>🏠 报修管理系统</span>
      </div>
      <el-menu
        :default-active="route.path"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <template v-if="userStore.role === 'RESIDENT'">
          <el-menu-item index="/repair/submit">
            <el-icon><Edit /></el-icon>
            <span>提交报修</span>
          </el-menu-item>
          <el-menu-item index="/repair/my-orders">
            <el-icon><List /></el-icon>
            <span>我的报修</span>
          </el-menu-item>
        </template>

        <template v-if="userStore.role === 'REPAIRER'">
          <el-menu-item index="/repairer/tasks">
            <el-icon><List /></el-icon>
            <span>我的任务</span>
          </el-menu-item>
        </template>

        <template v-if="userStore.role === 'ADMIN'">
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/dispatch">
            <el-icon><Connection /></el-icon>
            <span>派单管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/statistics">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </el-menu-item>
        </template>

        <el-menu-item index="/profile">
          <el-icon><UserFilled /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;border-bottom:1px solid #dcdfe6;display:flex;align-items:center;justify-content:space-between">
        <div>
          <span style="font-size:16px;color:#303133">{{ pageTitle }}</span>
        </div>
        <div>
          <span style="margin-right:20px;color:#606266">
            {{ userStore.info?.realName || '用户' }}
            <el-tag size="small" style="margin-left:8px">{{ roleLabel }}</el-tag>
          </span>
          <el-button type="danger" size="small" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const pageTitle = computed(() => {
  const map = {
    '/dashboard': '工作台',
    '/repair/submit': '提交报修',
    '/repair/my-orders': '我的报修',
    '/repairer/tasks': '我的任务',
    '/admin/users': '用户管理',
    '/admin/dispatch': '派单管理',
    '/admin/statistics': '数据统计',
    '/profile': '个人中心'
  }
  return map[route.path] || '社区物业报修管理系统'
})

const roleLabel = computed(() => {
  const map = { RESIDENT: '住户', REPAIRER: '维修工', ADMIN: '管理员' }
  return map[userStore.role] || ''
})

const logout = async () => {
  await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  userStore.fetchInfo()
})
</script>

<style scoped>
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}
.el-main {
  background: #f5f7fa;
  padding: 20px;
}
</style>
