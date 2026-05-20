<template>
  <div>
    <h2>欢迎，{{ userStore.info?.realName || '用户' }}</h2>
    <p style="color:#909399">角色：{{ roleLabel }}</p>

    <el-row :gutter="20" style="margin-top:20px" v-if="userStore.role === 'RESIDENT'">
      <el-col :span="8">
        <el-card shadow="hover" @click="$router.push('/repair/submit')" class="clickable-card">
          <div class="card-inner">
            <el-icon :size="40" color="#409EFF"><Edit /></el-icon>
            <h3>提交报修</h3>
            <p>在线提交报修申请</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" @click="$router.push('/repair/my-orders')" class="clickable-card">
          <div class="card-inner">
            <el-icon :size="40" color="#67C23A"><List /></el-icon>
            <h3>我的报修</h3>
            <p>查看报修进度</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Sprint 2 将在此处添加 REPAIRER 和 ADMIN 卡片 -->
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '../store/user'

const userStore = useUserStore()
const roleLabel = computed(() => {
  const map = { RESIDENT: '住户', REPAIRER: '维修工', ADMIN: '管理员' }
  return map[userStore.role] || ''
})
</script>

<style scoped>
.clickable-card { cursor: pointer; transition: transform 0.2s; }
.clickable-card:hover { transform: translateY(-3px); }
.card-inner { text-align: center; padding: 20px 0; }
.card-inner h3 { margin: 12px 0 8px; color: #303133; }
.card-inner p { color: #909399; font-size: 13px; margin: 0; }
</style>
