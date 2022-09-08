import axiosClient from './axiosClient'

const url = '/order'
const cartApi = {
    create: (order) => axiosClient.post(`${url}`, order),
    getAll: (params) => axiosClient.get(`${url}`, { params }),
    get: (id) => axiosClient.get(`${url}/user?id=${id}`)
}
export default cartApi