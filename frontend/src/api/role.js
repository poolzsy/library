import request from '@/utils/request';

export function getRoleList(params) {
    return request({
        url: '/role/list',
        method: 'get',
        params
    });
}

export function addRole(data) {
    return request({
        url: '/role/save',
        method: 'post',
        data
    });
}

export function updateRole(data) {
    return request({
        url: '/role/update',
        method: 'put',
        data
    });
}

export function updateRoleStatus(data) {
    return request({
        url: '/role/update',
        method: 'put',
        data
    });
}

export function deleteRole(id) {
    return request({
        url: `/role/delete/${id}`,
        method: 'delete'
    });
}

export function getAllRoleList() {
    return request({
        url: `/role/listAll`,
        method: 'get'
    });
}
