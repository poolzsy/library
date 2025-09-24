<template>
    <div class="board">
        <div class="search">
            <el-input v-model="params.name" placeholder="请输入用户名或姓名" class="search-input" clearable>
                <template #prefix>
                    <el-icon>
                        <Search />
                    </el-icon>
                </template>
            </el-input>

            <el-button type="primary" :icon="Search" class="search-btn">搜索</el-button>
            <el-button @click="clear" class="clearn-btn">重置</el-button>

            <el-button @click="addUser" type="success" :icon="Plus" class="add-btn">新增用户</el-button>
        </div>

        <!-- 表格区域 -->
        <div class="table">
            <el-table :data="data.userList" style="width: 100%">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
                <el-table-column prop="username" label="用户名" width="180"></el-table-column>
                <el-table-column prop="nickname" label="昵称" width="180"></el-table-column>
                <el-table-column prop="phone" label="手机号" width="180"></el-table-column>
                <el-table-column prop="email" label="邮箱"></el-table-column>
                <el-table-column label="操作" width="200" align="center">
                    <template #default>
                        <el-button type="primary" size="small" link>编辑</el-button>
                        <el-button type="danger" size="small" link>删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 分页组件 -->
        <div class="pagination-container">
            <el-pagination background :current-page="params.pageNum" :page-size="params.pageSize"
                :page-sizes="[5, 10, 15, 20]" layout="total, sizes, prev, pager, next, jumper" :total="params.total"
                @size-change="handleSizeChange" @current-change="handlePageChange" />
        </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="40%" @close="resetForm">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="账号" prop="username">
                <el-input v-model="form.username" :disabled="!!form.id" placeholder="请输入账号" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
            <!-- <el-form-item label="头像" prop="avatar">
                <el-upload action="http://localhost:9090/file/upload" :headers="{
                    token: data.user.token
                }" :on-success="handleFileSuccess" list-type="picture">
                    <el-button type="primary">上传头像</el-button>
                </el-upload>
            </el-form-item> -->
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialog.visible = false">取消</el-button>
                <el-button type="primary" @click="submitForm">确定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'
import { getUserList } from '@/api/user'

// 表单验证规则
const formRef = ref(null)
const form = reactive({
    username: '',
    name: '',
    phone: '',
    email: '',
    // avatar: ''
})

const rules = {
    username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ],
    name: [
        { required: true, message: '请输入姓名', trigger: 'blur' },
        { min: 2, max: 10, message: '长度在 2 到 10'}
    ]
}

const dialog = reactive({
    visible: false,
    title: ''
})


const data = reactive({
    userList: [],
})

const params = reactive({
    pageNum: 1,
    pageSize: 5,
    name: '',
    total: 0
});


const clear = () => {
    params.name = ''
}

// 表单处理
const resetForm = () => {
    formRef.value.resetFields()
}

const submitForm = () => {
    formRef.value.validate((valid) => {
        if (valid) {
            dialog.visible = false
            resetForm()
            ElMessage.success('提交成功')
            getUserListData()
        }
    })
}

// 新增用户
const addUser = () => {
    dialog.title = '新增用户'
    dialog.visible = true
}

// 获取用户列表数据
const getUserListData = async () => {
    const res = await getUserList({
        params: params
    })
    userList.splice(0, userList.length, ...res.data)
}
getUserListData();

// 分页插件处理
const handleSizeChange = (newSize) => {
    params.pageSize = newSize;
    getUserListData();
};

const handlePageChange = (newPage) => {
    params.pageNum = newPage;
    getUserListData();
};
</script>

<style scoped>
@import '@/assets/css/User.css';
</style>
