<template>
  <div>
    <el-card v-loading="loading" style="margin-bottom:20px">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <h3>任务详情 #{{ detail.id }}</h3>
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
        <el-descriptions-item label="派单时间">{{ detail.dispatchTime }}</el-descriptions-item>
        <el-descriptions-item label="住户姓名">{{ detail.userName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detail.building }}{{ detail.unitNum }}{{ detail.room }}</el-descriptions-item>
        <el-descriptions-item label="故障描述" :span="2">{{ detail.repairOrderDesc }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-if="detail.status !== 'COMPLETED' && detail.status !== 'TRANSFERRED'" style="margin-bottom:20px">
      <template #header><h4>更新状态</h4></template>
      <div>
        <el-button v-if="detail.status === 'ACCEPTED'" type="warning" @click="updateStatus('REPAIRING')">开始维修</el-button>
        <el-button v-if="detail.status === 'REPAIRING'" type="success" @click="updateStatus('COMPLETED')">维修完成</el-button>
        <el-button v-if="detail.status === 'ACCEPTED' || detail.status === 'REPAIRING'" type="danger" @click="showTransfer = true">申请转单</el-button>
      </div>
      <div v-if="showTransfer" style="margin-top:15px">
        <el-input v-model="transferReason" placeholder="请输入转单原因" style="width:300px;margin-right:10px" />
        <el-button type="primary" @click="transfer">确认转单</el-button>
        <el-button @click="showTransfer = false">取消</el-button>
      </div>
    </el-card>

    <el-card style="margin-bottom:20px">
      <template #header><h4>维修记录</h4></template>
      <el-form :model="logForm" label-width="100px">
        <el-form-item label="维修说明">
          <el-input v-model="logForm.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="使用材料">
          <el-input v-model="logForm.materialsUsed" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addLog" :loading="logSubmitting">添加维修记录</el-button>
        </el-form-item>
      </el-form>
      <el-timeline>
        <el-timeline-item v-for="log in logs" :key="log.id" :timestamp="log.createTime">
          {{ log.description || '无描述' }}
          <div v-if="log.materialsUsed" style="color:#909399;font-size:12px">材料：{{ log.materialsUsed }}</div>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { dispatchApi } from '../../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const detail = ref({})
const loading = ref(false)
const showTransfer = ref(false)
const transferReason = ref('')
const logs = ref([])
const logSubmitting = ref(false)
const logForm = reactive({ description: '', materialsUsed: '' })

const loadDetail = async () => {
  loading.value = true
  try {
    const res = await dispatchApi.getDetail(route.params.id)
    detail.value = res.data
    await loadLogs()
  } catch (e) {} finally { loading.value = false }
}

const loadLogs = async () => {
  try {
    const res = await dispatchApi.getLogs(route.params.id)
    logs.value = res.data
  } catch (e) {}
}

const updateStatus = async (status) => {
  try {
    await dispatchApi.updateStatus(route.params.id, status)
    ElMessage.success('状态更新成功')
    loadDetail()
  } catch (e) {}
}

const transfer = async () => {
  if (!transferReason.value) { ElMessage.warning('请输入转单原因'); return }
  try {
    await dispatchApi.transfer(route.params.id, transferReason.value)
    ElMessage.success('已申请转单')
    loadDetail()
  } catch (e) {}
}

const addLog = async () => {
  logSubmitting.value = true
  try {
    await dispatchApi.addLog(route.params.id, logForm)
    ElMessage.success('添加成功')
    logForm.description = ''
    logForm.materialsUsed = ''
    loadLogs()
  } catch (e) {} finally { logSubmitting.value = false }
}

const urgencyType = (v) => ({ NORMAL: '', URGENT: 'warning', CRITICAL: 'danger' }[v] || '')
const urgencyLabel = (v) => ({ NORMAL: '普通', URGENT: '紧急', CRITICAL: '特急' }[v] || v)
const statusType = (v) => {
  const m = { PENDING_ACCEPT: 'info', ACCEPTED: 'warning', REPAIRING: 'warning', COMPLETED: 'success' }
  return m[v] || ''
}
const statusLabel = (v) => {
  const m = { PENDING_ACCEPT: '待接单', ACCEPTED: '已接单', REPAIRING: '维修中', COMPLETED: '已完成', TRANSFERRED: '已转单' }
  return m[v] || v
}

onMounted(loadDetail)
</script>
