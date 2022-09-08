import axiosClient from './axiosClient'

const url = '/user'
const userApi = {
    getAllParent: () => axiosClient.get(`${url}/parent`),
    getAllByPage: (params) => axiosClient.get(url, { params }),
    get: id => axiosClient.get(`${url}?id=${id}`),
    add: user => {
        return axiosClient.post(url, user)
    },
    update: (id, user) => {
        return axiosClient.put(`${url}/${id}`, user)
    },
    delete: (id) => {
        return axiosClient.delete(`${url}/${id}`)
    }
}
export default userApi