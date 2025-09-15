<template>
    <el-row type="flex" class="row-bg" justify="center">
        <el-col :span="6">
            <h2>欢迎来到管理系统</h2>
            <p>这是一个基于Vue.js和Element UI构建的管理系统。</p>
            <p>请登录以继续。</p>
        </el-col>

        <el-col :span="1">
            <el-divider direction="vertical" style="height: 150px;"></el-divider>
        </el-col>

        <el-col :span="6">
            <el-form :model="loginForm" :rules="rules" ref="formRef" label-width="70px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
                </el-form-item>

                <el-form-item label="密码" prop="password">
                    <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
                </el-form-item>

                <el-form-item label="验证码" prop="code">
                    <el-row :gutter="20">
                        <el-col :span="14">
                            <el-input v-model="loginForm.code" placeholder="请输入验证码"></el-input>
                        </el-col>
                        <el-col :span="10">
                            <img src="https://via.placeholder.com/100x40?text=Captcha" alt="验证码" style="cursor: pointer;">
                        </el-col>
                    </el-row>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary">登录</el-button>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<script setup>
import { ref, reactive } from 'vue';

const formRef = ref(null);

const loginForm = reactive({
    username: '',
    password: '',
    code: ''
});

const rules = reactive({
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在3到20个字符之间', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在6到20个字符之间', trigger: 'blur' }
    ],
    code: [
        { required: true, message: '请输入验证码', trigger: 'blur' }
    ]
});
</script>

<style scoped>
.row-bg {
    padding-top: 50px;
}
</style>
