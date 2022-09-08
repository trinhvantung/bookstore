import axiosClient from './axiosClient'

const url = '/author'
const authorApi = {
    getAll: () => axiosClient.get(url),
    getAllByPage: (params) => axiosClient.get(url, { params }),
    get: id => axiosClient.get(`${url}?id=${id}`),
    add: author => {
        return axiosClient.post(url, author)
    },
    update: (id, author) => {
        return axiosClient.put(`${url}/${id}`, author)
    },
    delete: (id) => {
        return axiosClient.delete(`${url}/${id}`)
    }
}
export default authorApi