import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import router from '@/router';

// 创建一个 axios 实例
const service = axios.create({
    baseURL: 'http://localhost:9090',
    timeout: 5000
});

// 请求拦截器
service.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    response => {
        const res = response.data;

        if (res.code !== 200) {
            ElMessage({
                message: res.msg || 'Error',
                type: 'error',
                duration: 5 * 1000
            });
            return Promise.reject(new Error(res.msg || 'Error'));
        } else {
            return res;
        }
    },
    error => {
        let message = error.message;

        if (error.response) {
            if (error.response.status === 401) {
                // 使用 ElMessageBox 提示用户
                ElMessageBox.confirm('您的登录已过期，请重新登录', '认证失败', {
                    confirmButtonText: '重新登录',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    localStorage.removeItem('token');
                    router.push('/login');
                });

                // 返回一个被拒绝的 Promise，中断后续操作
                return Promise.reject(new Error('登录已过期'));
            }

            // 使用后端返回的错误信息
            if (error.response.data && error.response.data.msg) {
                message = error.response.data.msg;
            }
        }

        ElMessage({
            message: message,
            type: 'error',
            duration: 5 * 1000
        });

        return Promise.reject(error);
    }
);

export default service;
