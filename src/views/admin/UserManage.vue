<template>
  <el-card>
    <template #header><h3>用户管理</h3></template>
    <el-table :data="users" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="role" label="角色" width="90">
        <template #default="{ row }">
          <el-tag :type="roleType(row.role)" size="small">{{ roleLabel(row.role) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="170" />
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
          <el-select v-model="row._newRole" size="small" style="width:100px;margin-right:5px"
            @change="changeRole(row)" placeholder="角色">
            <el-option label="住户" value="RESIDENT" />
            <el-option label="维修工" value="REPAIRER" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
          <el-button size="small" :type="row.status === 1 ? 'danger' : 'success'"
            @click="toggleStatus(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '../../api'
import { ElMessage, ElMessageBox } from 'element-plus'

const users = ref([])
const loading = ref(false)

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await userApi.listAll()
    users.value = res.data.map(u => ({ ...u, _newRole: u.role }))
  } catch (e) {} finally { loading.value = false }
}

const toggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  try {
    await userApi.updateStatus(row.id, newStatus)
    ElMessage.success('状态更新成功')
    loadUsers()
  } catch (e) {}
}

const changeRole = async (row) => {
  await ElMessageBox.confirm(`确定修改 ${row.realName} 的角色吗？`, '确认', { type: 'warning' })
  try {
    await userApi.updateRole(row.id, row._newRole)
    ElMessage.success('角色修改成功')
    loadUsers()
  } catch (e) {}
}

const roleType = (r) => ({ ADMIN: 'danger', REPAIRER: 'warning', RESIDENT: '' }[r] || '')
const roleLabel = (r) => ({ ADMIN: '管理员', REPAIRER: '维修工', RESIDENT: '住户' }[r] || r)

onMounted(loadUsers)
</script>
