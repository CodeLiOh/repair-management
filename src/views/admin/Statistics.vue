<template>
  <div>
    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align:center">
            <h2 style="color:#409EFF;margin:0">{{ stats.totalOrders || 0 }}</h2>
            <p style="color:#909399">总报修单</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align:center">
            <h2 style="color:#E6A23C;margin:0">{{ stats.pendingOrders || 0 }}</h2>
            <p style="color:#909399">处理中</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align:center">
            <h2 style="color:#67C23A;margin:0">{{ stats.completedOrders || 0 }}</h2>
            <p style="color:#909399">已完成</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align:center">
            <h2 style="color:#909399;margin:0">{{ stats.totalRepairers || 0 }}</h2>
            <p style="color:#909399">维修工人数</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-bottom:20px">
      <template #header><h4>维修工评分排行</h4></template>
      <el-table :data="stats.repairerRanking || []" stripe>
        <el-table-column type="index" label="排名" width="60" />
        <el-table-column prop="repairerName" label="维修工" />
        <el-table-column prop="avgRating" label="平均评分" width="100">
          <template #default="{ row }">
            <el-rate :model-value="Number(row.avgRating || 0)" disabled show-score />
          </template>
        </el-table-column>
        <el-table-column prop="reviewCount" label="评价数" width="80" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { statisticsApi } from '../../api'

const stats = ref({})

const loadStats = async () => {
  try {
    const res = await statisticsApi.get()
    stats.value = res.data
  } catch (e) {}
}

onMounted(loadStats)
</script>
