import axiosClient from './axiosClient'

const url = '/cart'
const cartApi = {
    add: (id, quantity) => axiosClient.post(`${url}/${id}/${quantity}`),
    getAll: () => axiosClient.get(`${url}`),
    delete: (id) => axiosClient.delete(`${url}/${id}`),
    update: (id, quantity) => axiosClient.put(`${url}/${id}/${quantity}`)
}
export default cartApi