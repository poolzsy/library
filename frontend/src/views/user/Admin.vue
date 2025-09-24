<template>
    <div class="board">
        <div class="search">
            <el-input v-model="searchQuery" placeholder="请输入用户名或姓名" class="search-input" clearable>
                <template #prefix>
                    <el-icon>
                        <Search />
                    </el-icon>
                </template>
            </el-input>

            <el-button type="primary" :icon="Search" class="search-btn">搜索</el-button>
            <el-button @click="resetQuery" class="clearn-btn">重置</el-button>

            <el-button type="success" :icon="Plus" class="add-btn">新增管理员</el-button>
        </div>

        <!-- 表格区域 -->
        <div class="table">
            <el-table :data="tableData" style="width: 100%">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
                <el-table-column prop="username" label="用户名" width="180"></el-table-column>
                <el-table-column prop="name" label="姓名" width="180"></el-table-column>
                <el-table-column prop="email" label="邮箱"></el-table-column>
                <el-table-column label="操作" width="200" align="center">
                    <template #default>
                        <el-button type="primary" size="small" link>编辑</el-button>
                        <el-button type="danger" size="small" link>删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'

const searchQuery = ref('')

const resetQuery = () => {
    searchQuery.value = ''
}

const tableData = reactive([])


</script>

<style scoped>
@import '@/assets/css/User.css';

.add-btn {
    margin-left: auto;
}
</style>
