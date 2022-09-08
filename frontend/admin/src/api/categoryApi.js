import axiosClient from './axiosClient'

const url = '/category'
const categoryApi = {
    getAllParent: () => axiosClient.get(`${url}/parent`),
    getAll: () => axiosClient.get(`${url}`),
    getAllByPage: (params) => axiosClient.get(url, { params }),
    get: id => axiosClient.get(`${url}?id=${id}`),
    add: category => {
        return axiosClient.post(url, category)
    },
    update: (id, category) => {
        return axiosClient.put(`${url}/${id}`, category)
    },
    delete: (id) => {
        return axiosClient.delete(`${url}/${id}`)
    }
}
export default categoryApi