<template>
  <div class="login-container">
    <div class="login-card" style="width:480px">
      <h1>住户注册</h1>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱（选填）" />
        </el-form-item>
        <el-form-item label="楼栋号">
          <el-input v-model="form.building" placeholder="如：3栋" />
        </el-form-item>
        <el-form-item label="单元号">
          <el-input v-model="form.unitNum" placeholder="如：2单元" />
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="form.room" placeholder="如：501" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" :loading="loading" style="width:100%">注册</el-button>
        </el-form-item>
      </el-form>
      <p class="register-link">已有账号？<router-link to="/login">返回登录</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '../../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({
  username: '', password: '', realName: '', phone: '',
  email: '', building: '', unitNum: '', room: ''
})
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
}

const register = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await userApi.register(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (e) {} finally { loading.value = false }
}
</script>
