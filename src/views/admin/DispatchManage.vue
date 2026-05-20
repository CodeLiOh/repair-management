<template>
  <el-card>
    <template #header><h3>派单管理 - 待派单报修</h3></template>
    <el-table :data="orders" v-loading="loading" stripe>
      <el-table-column prop="id" label="编号" width="80" />
      <el-table-column prop="categoryName" label="类型" width="100" />
      <el-table-column prop="description" label="故障描述" show-overflow-tooltip />
      <el-table-column prop="urgency" label="紧急程度" width="90">
        <template #default="{ row }">
          <el-tag :type="urgencyType(row.urgency)" size="small">{{ urgencyLabel(row.urgency) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="userName" label="住户" width="80" />
      <el-table-column prop="building" label="地址" width="150">
        <template #default="{ row }">{{ row.building }}{{ row.unitNum }}{{ row.room }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="提交时间" width="170" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <template v-if="!row._dispatching">
            <el-button size="small" type="primary" @click="row._dispatching = true">派单</el-button>
          </template>
          <template v-else>
            <el-select v-model="row._selectedRepairer" size="small" style="width:120px;margin-right:5px" placeholder="选择维修工">
              <el-option v-for="r in repairers" :key="r.id" :label="r.realName" :value="r.id" />
            </el-select>
            <el-button size="small" type="success" @click="doDispatch(row)">确认</el-button>
            <el-button size="small" @click="row._dispatching = false">取消</el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { repairApi, userApi, dispatchApi } from '../../api'
import { ElMessage } from 'element-plus'

const orders = ref([])
const repairers = ref([])
const loading = ref(false)

const loadOrders = async () => {
  loading.value = true
  try {
    const res = await repairApi.listPending()
    orders.value = res.data.map(o => ({ ...o, _dispatching: false, _selectedRepairer: null }))
  } catch (e) {} finally { loading.value = false }
}

const loadRepairers = async () => {
  try {
    const res = await userApi.listRepairers()
    repairers.value = res.data
  } catch (e) {}
}

const doDispatch = async (row) => {
  if (!row._selectedRepairer) { ElMessage.warning('请选择维修工'); return }
  try {
    await dispatchApi.dispatch({ repairOrderId: row.id, repairerId: row._selectedRepairer })
    ElMessage.success('派单成功')
    loadOrders()
  } catch (e) {}
}

const urgencyType = (v) => ({ NORMAL: '', URGENT: 'warning', CRITICAL: 'danger' }[v] || '')
const urgencyLabel = (v) => ({ NORMAL: '普通', URGENT: '紧急', CRITICAL: '特急' }[v] || v)

onMounted(() => { loadOrders(); loadRepairers() })
</script>
