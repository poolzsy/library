<template>
    <div class="board">
        <!-- 搜索与操作区域 -->
        <div class="search">
            <el-input v-model="params.roleName" placeholder="请输入角色名称" class="search-input" clearable
                @keyup.enter="searchQuery">
                <template #prefix>
                    <el-icon>
                        <Search />
                    </el-icon>
                </template>
            </el-input>
            <el-button type="primary" :icon="Search" @click="searchQuery">搜索</el-button>
            <el-button @click="resetQuery">重置</el-button>
            <el-button type="success" :icon="Plus" class="add-btn" @click="handleAdd">新增角色</el-button>
        </div>

        <!-- 表格区域 -->
        <div class="table">
            <el-table :data="roleList" style="width: 100%" v-loading="loading">
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column prop="id" label="ID" width="80" align="center" />
                <el-table-column prop="roleName" label="角色名称" min-width="180" show-overflow-tooltip />
                <el-table-column prop="roleKey" label="权限标识" min-width="180" show-overflow-tooltip />
                <el-table-column prop="remark" label="描述" show-overflow-tooltip />
                <el-table-column prop="status" label="状态" width="100" align="center">
                    <template #default="scope">
                        <el-switch v-model="scope.row.status" :active-value="0" :inactive-value="1"
                            :before-change="() => handleBeforeStatusChange(scope.row)" />
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180" align="center" fixed="right">
                    <template #default="scope">
                        <el-button type="primary" size="small" link @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button type="danger" size="small" link @click="handleDelete(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 分页组件 -->
        <div class="pagination-container">
            <el-pagination background :current-page="params.pageNum" :page-size="params.pageSize"
                :page-sizes="[5, 10, 20, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total"
                @size-change="handleSizeChange" @current-change="handlePageChange" />
        </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="40%" :close-on-click-modal="false"
        @close="resetForm">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="角色名称" prop="roleName">
                <el-input v-model="form.roleName" :disabled="!!form.id" placeholder="请输入角色名称" />
            </el-form-item>
            <el-form-item label="权限标识" prop="roleKey">
                <el-input v-model="form.roleKey" :disabled="!!form.id" placeholder="请输入权限标识" />
            </el-form-item>
            <el-form-item label="描述" prop="remark">
                <el-input v-model="form.remark" placeholder="请输入描述" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-radio-group v-model="form.status">
                    <el-radio :label="0">正常</el-radio>
                    <el-radio :label="1">禁用</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialog.visible = false">取消</el-button>
                <el-button type="primary" @click="submitForm" :loading="isSubmitting">确定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { Search, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, addRole, updateRole, deleteRole, updateRoleStatus} from '@/api/role'

const loading = ref(false)
const isSubmitting = ref(false)
const roleList = ref([])
const total = ref(0)
const roleOptions = ref([])

// 搜索和分页参数
const params = reactive({
    pageNum: 1,
    pageSize: 10,
    roleName: '',
});

const dialog = reactive({
    visible: false,
    title: ''
});

// 表单引用和数据
const formRef = ref(null)
const initialForm = {
    id: null,
    roleName: '',
    roleKey: '',
    remark: '',
    status: 0
}
const form = reactive({ ...initialForm })

// 表单验证规则
const rules = {
    roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
    roleKey: [{ required: true, message: '请输入权限标识', trigger: 'blur' }]
}

// 获取角色列表数据
const getRoleListData = async () => {
    loading.value = true;
    try {
        const res = await getRoleList(params);
        
        const processedList = res.data.rows.map(role => ({
            ...role,
            status: parseInt(role.status, 10)
        }));
        
        roleList.value = processedList;
        total.value = res.data.total; 
    } catch (error) {
        console.error("Failed to fetch role list:", error);
    } finally {
        loading.value = false;
    }
}

onMounted(getRoleListData);

// 搜索
const searchQuery = () => {
    params.pageNum = 1;
    getRoleListData();
}

// 重置搜索
const resetQuery = () => {
    params.roleName = '';
    searchQuery();
}

// 处理新增
const handleAdd = () => {
    dialog.title = '新增角色';
    dialog.visible = true;
    Object.assign(form, initialForm);
}

// 处理编辑
const handleEdit = (row) => {
    dialog.title = '编辑角色';
    nextTick(() => {
        Object.assign(form, row);
    });
    dialog.visible = true;
}

// 处理删除
const handleDelete = (id) => {
    ElMessageBox.confirm(
        '您确定要删除该角色吗？此操作不可撤销。',
        '警告',
        {
            confirmButtonText: '确定删除',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(async () => {
        try {
            await deleteRole(id);
            ElMessage.success('删除成功');
            if (roleList.value.length === 1 && params.pageNum > 1) {
                params.pageNum--;
            }
            getRoleListData();
        } catch (error) {
            console.error("Failed to delete role:", error);
        }
    }).catch(() => {
        ElMessage.info('已取消删除');
    });
}

// 处理状态切换
const handleBeforeStatusChange = async (role) => {
    const targetStatus = role.status === 0 ? 1 : 0;
    const actionText = targetStatus === 0 ? '启用' : '禁用';
    // 如果是禁用操作，则弹出确认框
    if (targetStatus === 1) {
        try {
            await ElMessageBox.confirm(
                `您确定要${actionText}该角色吗？`,
                '操作确认', { /* ... options ... */ }
            );
        } catch (error) {
            ElMessage.info('操作已取消');
            return false;
        }
    }
    try {
        await updateRoleStatus({ id: role.id, status: targetStatus });
        ElMessage.success(`${actionText}成功`);
        return true;
    } catch (apiError) {
        ElMessage.error(`${actionText}失败，请稍后重试`);
        console.error("API Error:", apiError);
        return false;
    }
};

// 提交表单 (新增/编辑)
const submitForm = () => {
    formRef.value.validate(async (valid) => {
        if (valid) {
            isSubmitting.value = true;
            try {
                if (form.id) {
                    await updateRole(form);
                    ElMessage.success('更新成功');
                } else {
                    await addRole(form);
                    ElMessage.success('新增成功');
                }
                dialog.visible = false;
                getRoleListData();
            } catch (error) {
                console.error("Form submission failed:", error);
            } finally {
                isSubmitting.value = false;
            }
        }
    });
}

// 重置表单
const resetForm = () => {
    if (formRef.value) {
        formRef.value.clearValidate();
    }
}

// --- 分页处理 ---
const handleSizeChange = (newSize) => {
    params.pageSize = newSize;
    params.pageNum = 1;
    getRoleListData();
};
const handlePageChange = (newPage) => {
    params.pageNum = newPage;
    getRoleListData();
};
</script>

<style scoped>
@import '@/assets/css/View.css';
</style>
