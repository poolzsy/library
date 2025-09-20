<template>
    <div class="login-container">
        <el-form :model="form" :rules="rules" ref="formRef" class="login-form" label-position="top">
            <div class="title-container">
                <h3 class="title">{{ isLoginMode ? '图书管理系统 - 登录' : '图书管理系统 - 注册' }}</h3>
            </div>

            <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" size="large"></el-input>
            </el-form-item>

            <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password
                    size="large"></el-input>
            </el-form-item>

            <el-form-item v-if="!isLoginMode" label="确认密码" prop="confirmPassword">
                <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" :prefix-icon="Lock"
                    show-password size="large"></el-input>
            </el-form-item>

            <el-form-item label="验证码" prop="code">
                <el-row :gutter="20" justify="space-between" style="width: 100%;">
                    <el-col :span="14">
                        <el-input v-model="form.code" placeholder="请输入验证码" :prefix-icon="Key" size="large"
                            @keyup.enter="handleSubmit"></el-input>
                    </el-col>
                    <el-col :span="10" class="captcha-col">
                        <img :src="captchaUrl" @click="refreshCaptcha" alt="验证码" class="captcha-img" title="点击刷新" />
                    </el-col>
                </el-row>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" style="width: 100%;" size="large" @click="handleSubmit" :loading="loading">
                    {{ isLoginMode ? '登 录' : '注 册' }}
                </el-button>
            </el-form-item>

            <div class="switch-mode-container">
                <span @click="toggleMode" class="switch-mode-link">
                    {{ isLoginMode ? '没有账户？立即注册' : '已有账户？前往登录' }}
                </span>
            </div>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import { User, Lock, Key } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { login, register } from '@/api/user';

const router = useRouter();
const formRef = ref(null);
const loading = ref(false);
const formMode = ref('login');

const isLoginMode = computed(() => formMode.value === 'login');

// 模拟验证码，实际项目中应替换为后端接口
const captchaUrl = ref('http://localhost:9090/captcha');
const refreshCaptcha = () => {
    captchaUrl.value = `http://localhost:9090/captcha?t=${new Date().getTime()}`;
};

const form = reactive({
    username: '',
    password: '',
    confirmPassword: '',
    code: ''
});

// 自定义确认密码校验规则
const validateConfirmPassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'));
    } else if (value !== form.password) {
        callback(new Error("两次输入的密码不一致!"));
    } else {
        callback();
    }
};

// 表单校验规则
const rules = reactive({
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }],
    code: [{ required: false, message: '请输入验证码', trigger: 'blur' }]
});

// 统一的提交处理
const handleSubmit = () => {
    formRef.value.validate(async (valid) => {
        if (valid) {
            loading.value = true;
            try {
                if (isLoginMode.value) {
                    await handleLogin();
                } else {
                    await handleRegister();
                }
            } catch (error) {
                // refreshCaptcha();
            } finally {
                loading.value = false;
            }
        }
    });
};

// 登录逻辑
const handleLogin = async () => {
    const loginData = { username: form.username, password: form.password, code: form.code };
    const res = await login(loginData);
    console.log('登录成功，后端返回:', res);

    localStorage.setItem('token', res.data.token);
    ElMessage.success('登录成功！');
    router.push('/');
};

// 注册逻辑
const handleRegister = async () => {
    await register(form);
    ElMessage.success('注册成功！正在为您切换到登录界面。');
    toggleMode();
};

const toggleMode = () => {
    formMode.value = isLoginMode.value ? 'register' : 'login';
    if (formRef.value) formRef.value.resetFields();
    loading.value = false;

    rules.confirmPassword[0].required = !isLoginMode.value;
};
</script>

<style>
@import '@/assets/css/Login.css';
</style>
