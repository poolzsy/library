<template>
    <div class="common-layout">
        <el-container class="layout-container">
            <!-- 头部区域 -->
            <el-header>
                <div class="logo-title">
                    <img src="@/assets/logo.svg" alt="logo" class="logo-img" />
                    <span>图书管理系统</span>
                </div>
                <!-- 右侧用户菜单 -->
                <div class="user-menu">
                    <el-dropdown>
                        <span class="el-dropdown-link">
                            <el-avatar size="small"
                                src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
                            <span>Admin</span>
                            <el-icon class="el-icon--right"><arrow-down /></el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item>个人中心</el-dropdown-item>
                                <el-dropdown-item>修改密码</el-dropdown-item>
                                <el-dropdown-item divided>退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>

            <el-container>
                <!-- 侧边栏区域 -->
                <el-aside width="200px">
                    <el-menu :default-active="$route.path" :router="true" class="el-menu-vertical">
                        <el-menu-item index="/home">
                            <el-icon><home-filled /></el-icon>
                            <span>首页</span>
                        </el-menu-item>
                        <el-sub-menu index="user-management">
                            <template #title>
                                <el-icon>
                                    <user />
                                </el-icon>
                                <span>用户管理</span>
                            </template>
                            <el-menu-item index="/user">用户列表</el-menu-item>
                            <el-menu-item index="/role">角色列表</el-menu-item>
                        </el-sub-menu>
                        <el-sub-menu index="book-management">
                            <template #title>
                                <el-icon>
                                    <collection />
                                </el-icon>
                                <span>图书管理</span>
                            </template>
                            <el-menu-item index="/book-list">图书列表</el-menu-item>
                        </el-sub-menu>
                    </el-menu>
                </el-aside>

                <!-- 主体内容区域 -->
                <el-main>
                    <RouterView />
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script setup>
import { ArrowDown, HomeFilled, User, Collection } from '@element-plus/icons-vue'
</script>

<style>
@import '@/assets/css/Layout.css';
</style>
