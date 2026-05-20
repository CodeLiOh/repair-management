<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <h3>我的任务</h3>
        <el-select v-model="statusFilter" placeholder="筛选状态" clearable style="width:150px" @change="loadTasks">
          <el-option label="待接单" value="PENDING_ACCEPT" />
          <el-option label="已接单" value="ACCEPTED" />
          <el-option label="维修中" value="REPAIRING" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="已转单" value="TRANSFERRED" />
        </el-select>
      </div>
    </template>
    <el-table :data="tasks" v-loading="loading" stripe>
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column prop="categoryName" label="类型" width="100" />
      <el-table-column prop="repairOrderDesc" label="故障描述" show-overflow-tooltip />
      <el-table-column prop="urgency" label="紧急程度" width="90">
        <template #default="{ row }">
          <el-tag :type="urgencyType(row.urgency)" size="small">{{ urgencyLabel(row.urgency) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="住户" width="80" />
      <el-table-column prop="building" label="地址" width="150">
        <template #default="{ row }">{{ row.building || '' }}{{ row.unitNum || '' }}{{ row.room || '' }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="$router.push(`/repairer/task/${row.id}`)">详情</el-button>
          <el-button v-if="row.status === 'PENDING_ACCEPT'" size="small" type="success" @click="accept(row.id)">接单</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { dispatchApi } from '../../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const tasks = ref([])
const loading = ref(false)
const statusFilter = ref('')

const loadTasks = async () => {
  loading.value = true
  try {
    const res = await dispatchApi.listMyTasks(statusFilter.value || undefined)
    tasks.value = res.data
  } catch (e) {} finally { loading.value = false }
}

const accept = async (id) => {
  try {
    await dispatchApi.accept(id)
    ElMessage.success('已接单')
    loadTasks()
  } catch (e) {}
}

const urgencyType = (v) => ({ NORMAL: '', URGENT: 'warning', CRITICAL: 'danger' }[v] || '')
const urgencyLabel = (v) => ({ NORMAL: '普通', URGENT: '紧急', CRITICAL: '特急' }[v] || v)
const statusType = (v) => {
  const m = { PENDING_ACCEPT: 'info', ACCEPTED: 'warning', REPAIRING: 'warning', COMPLETED: 'success', TRANSFERRED: 'danger' }
  return m[v] || ''
}
const statusLabel = (v) => {
  const m = { PENDING_ACCEPT: '待接单', ACCEPTED: '已接单', REPAIRING: '维修中', COMPLETED: '已完成', TRANSFERRED: '已转单' }
  return m[v] || v
}

onMounted(loadTasks)
</script>
