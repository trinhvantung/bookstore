import axiosClient from './axiosClient'

const url = '/order'
const orderApi = {
    getAll: () => axiosClient.get(`${url}`),
    getAllByPage: (params) => axiosClient.get(`${url}/all`, { params }),
    get: id => axiosClient.get(`${url}?id=${id}`),
    add: order => {
        return axiosClient.post(url, order)
    },
    update: (id, orderTrack) => {
        return axiosClient.put(`${url}/${id}`, orderTrack)
    },
    delete: (id) => {
        return axiosClient.delete(`${url}/${id}`)
    }
}
export default orderApi