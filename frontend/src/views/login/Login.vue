<template>
    <div class="login-container">
        <el-form :model="loginForm" :rules="rules" ref="formRef" class="login-form" label-position="top">
            <div class="title-container">
                <h3 class="title">图书管理系统</h3>
            </div>

            <el-form-item label="用户名" prop="username">
                <el-input v-model="loginForm.username" placeholder="请输入用户名" :prefix-icon="User" size="large"></el-input>
            </el-form-item>

            <el-form-item label="密码" prop="password">
                <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock"
                    show-password size="large"></el-input>
            </el-form-item>

            <el-form-item label="验证码" prop="code">
                <el-row :gutter="20" justify="space-between" style="width: 100%;">
                    <el-col :span="14">
                        <el-input v-model="loginForm.code" placeholder="请输入验证码" :prefix-icon="Key" size="large"
                            @keyup.enter="handleLogin"></el-input>
                    </el-col>
                    <el-col :span="10" class="captcha-col">
                        <img :src="captchaUrl" @click="refreshCaptcha" alt="验证码" class="captcha-img" title="点击刷新" />
                    </el-col>
                </el-row>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" style="width: 100%;" size="large" @click="handleLogin" :loading="loading">
                    登 录
                </el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { User, Lock, Key } from '@element-plus/icons-vue';

const formRef = ref(null);
const loading = ref(false);

// 模拟验证码
const captchaUrl = ref('https://via.placeholder.com/120x40?text=Captcha');
const refreshCaptcha = () => {
    // 实际项目中，这里会请求新的验证码URL
    captchaUrl.value = `https://via.placeholder.com/120x40?text=Captcha&t=${new Date().getTime()}`;
    console.log('验证码已刷新');
};

const loginForm = reactive({
    username: '',
    password: '',
    code: ''
});

const rules = reactive({
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
    ],
    code: [
        { required: true, message: '请输入验证码', trigger: 'blur' }
    ]
});

// 登录逻辑
const handleLogin = () => {
    formRef.value.validate((valid) => {
        if (valid) {
            loading.value = true;
            console.log('表单验证通过，准备提交:', loginForm);
            setTimeout(() => {
                console.log('登录成功！');
                loading.value = false;
                // 此处可以进行路由跳转等操作
            }, 1500);
        } else {
            console.log('表单验证失败');
            return false;
        }
    });
};
</script>

<style>
@import '@/assets/css/Login.css';
</style>