import request from '@/utils/request';

export function login(data) {
    return request({
        url: '/user/login',
        method: 'post',
        data
    });
}

export function register(data) {
    return request({
        url: '/user/register',
        method: 'post',
        data
    });
}

export function getUserList(params) {
    return request({
        url: '/user/list',
        method: 'get',
        params
    });
}

export function addUser(data) {
    return request({
        url: '/user/save',
        method: 'post',
        data
    });
}

export function updateUser(data) {
    return request({
        url: '/user/update',
        method: 'put',
        data
    });
}

export function deleteUser(id) {
    return request({
        url: `/user/delete/${id}`,
        method: 'delete'
    });
}
