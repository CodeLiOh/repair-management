<template>
  <el-card>
    <template #header><h3>提交报修</h3></template>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" style="max-width:600px">
      <el-form-item label="报修类型" prop="categoryId">
        <el-select v-model="form.categoryId" placeholder="请选择" style="width:100%">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="紧急程度" prop="urgency">
        <el-radio-group v-model="form.urgency">
          <el-radio label="NORMAL">普通</el-radio>
          <el-radio label="URGENT">紧急</el-radio>
          <el-radio label="CRITICAL">特急</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="故障描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请详细描述故障情况" />
      </el-form-item>
      <el-form-item label="预约时间">
        <el-date-picker v-model="form.appointTime" type="datetime" placeholder="选择预约上门时间"
          value-format="YYYY-MM-DDTHH:mm:ss" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit" :loading="submitting">提交报修</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { repairApi } from '../../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)
const categories = ref([])
const form = reactive({
  categoryId: null, urgency: 'NORMAL', description: '', appointTime: ''
})
const rules = {
  categoryId: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
  urgency: [{ required: true, message: '请选择紧急程度', trigger: 'change' }],
  description: [{ required: true, message: '请填写故障描述', trigger: 'blur' }]
}

const loadCategories = async () => {
  const res = await repairApi.listCategories()
  categories.value = res.data
}

const submit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    await repairApi.submit(form)
    ElMessage.success('报修提交成功')
    router.push('/repair/my-orders')
  } catch (e) {} finally { submitting.value = false }
}

onMounted(loadCategories)
</script>
