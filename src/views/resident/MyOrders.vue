<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <h3>我的报修</h3>
        <el-select v-model="statusFilter" placeholder="筛选状态" clearable style="width:150px" @change="loadOrders">
          <el-option label="待审核" value="PENDING_REVIEW" />
          <el-option label="待派单" value="PENDING_DISPATCH" />
          <el-option label="待接单" value="PENDING_ACCEPT" />
          <el-option label="已接单" value="ACCEPTED" />
          <el-option label="维修中" value="REPAIRING" />
          <el-option label="待验收" value="PENDING_CHECK" />
          <el-option label="已完成" value="COMPLETED" />
          <el-option label="已撤销" value="CANCELLED" />
        </el-select>
      </div>
    </template>
    <el-table :data="orders" v-loading="loading" stripe>
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column prop="categoryName" label="报修类型" width="120" />
      <el-table-column prop="description" label="故障描述" show-overflow-tooltip />
      <el-table-column prop="urgency" label="紧急程度" width="100">
        <template #default="{ row }">
          <el-tag :type="urgencyType(row.urgency)" size="small">{{ urgencyLabel(row.urgency) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="提交时间" width="170" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="$router.push(`/repair/detail/${row.id}`)">详情</el-button>
          <el-button v-if="row.status === 'PENDING_REVIEW' || row.status === 'PENDING_DISPATCH'"
            size="small" type="danger" @click="cancelOrder(row.id)">撤销</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { repairApi } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const orders = ref([])
const loading = ref(false)
const statusFilter = ref('')

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await repairApi.listMyOrders(statusFilter.value || undefined)
    orders.value = res.data
  } catch (e) {} finally { loading.value = false }
}

const cancelOrder = async (id) => {
  await ElMessageBox.confirm('确定要撤销该报修单吗？', '确认', { type: 'warning' })
  try {
    await repairApi.cancel(id)
    ElMessage.success('已撤销')
    loadOrders()
  } catch (e) {}
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

onMounted(loadOrders)
</script>
