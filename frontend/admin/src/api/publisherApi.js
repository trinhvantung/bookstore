import axiosClient from './axiosClient'

const url = '/publisher'
const publisherApi = {
    getAll: () => axiosClient.get(url),
    getAllByPage: (params) => axiosClient.get(url, { params }),
    get: id => axiosClient.get(`${url}?id=${id}`),
    add: publisher => {
        return axiosClient.post(url, publisher)
    },
    update: (id, publisher) => {
        return axiosClient.put(`${url}/${id}`, publisher)
    },
    delete: (id) => {
        return axiosClient.delete(`${url}/${id}`)
    }
}
export default publisherApi