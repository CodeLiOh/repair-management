<template>
  <el-card v-loading="loading">
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <h3>报修单详情 #{{ detail.id }}</h3>
        <el-button @click="$router.back()">返回</el-button>
      </div>
    </template>
    <el-descriptions :column="2" border v-if="detail.id">
      <el-descriptions-item label="报修类型">{{ detail.categoryName }}</el-descriptions-item>
      <el-descriptions-item label="紧急程度">
        <el-tag :type="urgencyType(detail.urgency)" size="small">{{ urgencyLabel(detail.urgency) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="当前状态">
        <el-tag :type="statusType(detail.status)">{{ statusLabel(detail.status) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="提交时间">{{ detail.createTime }}</el-descriptions-item>
      <el-descriptions-item label="故障描述" :span="2">{{ detail.description }}</el-descriptions-item>
      <el-descriptions-item label="预约时间">{{ detail.appointTime || '未指定' }}</el-descriptions-item>
      <el-descriptions-item label="维修工">{{ detail.repairerName || '待分配' }}</el-descriptions-item>
    </el-descriptions>

    <!-- Sprint 2 将在此处添加评价功能 -->
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { repairApi } from '../../api'

const route = useRoute()
const detail = ref({})
const loading = ref(false)

const loadDetail = async () => {
  loading.value = true
  try {
    const res = await repairApi.getDetail(route.params.id)
    detail.value = res.data
  } catch (e) {} finally { loading.value = false }
}

const urgencyType = (v) => ({ NORMAL: '', URGENT: 'warning', CRITICAL: 'danger' }[v] || '')
const urgencyLabel = (v) => ({ NORMAL: '普通', URGENT: '紧急', CRITICAL: '特急' }[v] || v)
const statusType = (v) => {
  const m = { PENDING_REVIEW: '', PENDING_DISPATCH: 'info', PENDING_ACCEPT: 'info',
    ACCEPTED: 'warning', REPAIRING: 'warning', PENDING_CHECK: '', COMPLETED: 'success', CANCELLED: 'danger' }
  return m[v] || ''
}
const statusLabel = (v) => {
  const m = { PENDING_REVIEW: '待审核', PENDING_DISPATCH: '待派单', PENDING_ACCEPT: '待接单',
    ACCEPTED: '已接单', REPAIRING: '维修中', PENDING_CHECK: '待验收', COMPLETED: '已完成', CANCELLED: '已撤销' }
  return m[v] || v
}

onMounted(loadDetail)
</script>
