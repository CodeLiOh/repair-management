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

    <div v-if="detail.status === 'PENDING_CHECK'" style="margin-top:20px">
      <h4>评价维修服务</h4>
      <el-form :model="reviewForm" label-width="80px" style="max-width:400px">
        <el-form-item label="评分">
          <el-rate v-model="reviewForm.rating" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="reviewForm.comment" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitReview" :loading="submitting">提交评价</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { repairApi, reviewApi } from '../../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const detail = ref({})
const loading = ref(false)
const submitting = ref(false)
const reviewForm = reactive({ repairOrderId: null, rating: 5, comment: '' })

const loadDetail = async () => {
  loading.value = true
  try {
    const res = await repairApi.getDetail(route.params.id)
    detail.value = res.data
    reviewForm.repairOrderId = res.data.id
  } catch (e) {} finally { loading.value = false }
}

const submitReview = async () => {
  if (reviewForm.rating === 0) {
    ElMessage.warning('请选择评分')
    return
  }
  submitting.value = true
  try {
    await reviewApi.submit(reviewForm)
    ElMessage.success('评价提交成功')
    loadDetail()
  } catch (e) {} finally { submitting.value = false }
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
