<template>
  <el-card>
    <template #header><h3>个人中心</h3></template>
    <el-form :model="form" ref="formRef" label-width="100px" v-loading="loading">
      <el-form-item label="用户名">
        <el-input v-model="form.username" disabled />
      </el-form-item>
      <el-form-item label="角色">
        <el-tag>{{ roleLabel }}</el-tag>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.realName" />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="楼栋号" v-if="userStore.role === 'RESIDENT'">
        <el-input v-model="form.building" />
      </el-form-item>
      <el-form-item label="单元号" v-if="userStore.role === 'RESIDENT'">
        <el-input v-model="form.unitNum" />
      </el-form-item>
      <el-form-item label="房间号" v-if="userStore.role === 'RESIDENT'">
        <el-input v-model="form.room" />
      </el-form-item>
      <el-form-item label="技能标签" v-if="userStore.role === 'REPAIRER'">
        <el-input v-model="form.skillTag" />
      </el-form-item>
      <el-form-item label="服务年限" v-if="userStore.role === 'REPAIRER'">
        <el-input-number v-model="form.serviceYears" :min="0" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save" :loading="saving">保存修改</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted, computed } from 'vue'
import { useUserStore } from '../store/user'
import { userApi } from '../api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const loading = ref(false)
const saving = ref(false)
const form = reactive({
  username: '', realName: '', phone: '', email: '',
  building: '', unitNum: '', room: '', skillTag: '', serviceYears: 0
})

const roleLabel = computed(() => {
  const map = { RESIDENT: '住户', REPAIRER: '维修工', ADMIN: '管理员' }
  return map[userStore.role] || ''
})

const loadInfo = async () => {
  loading.value = true
  try {
    const res = await userApi.getInfo()
    Object.assign(form, res.data)
  } catch (e) {} finally { loading.value = false }
}

const save = async () => {
  saving.value = true
  try {
    await userApi.updateInfo(form)
    ElMessage.success('保存成功')
  } catch (e) {} finally { saving.value = false }
}

onMounted(loadInfo)
</script>
