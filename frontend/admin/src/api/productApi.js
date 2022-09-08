import axiosClient from './axiosClient'

const url = '/book'
const productApi = {
    getAllByPage: (params) => axiosClient.get(url, { params }),
    get: id => axiosClient.get(`${url}?id=${id}`),
    add: product => {
        return axiosClient.post(url, product)
    },
    update: (id, product) => {
        return axiosClient.put(`${url}/${id}`, product)
    },
    delete: (id) => {
        return axiosClient.delete(`${url}/${id}`)
    }
}
export default productApi