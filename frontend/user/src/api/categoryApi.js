import axiosClient from './axiosClient'

const url = '/category'
const categoryApi = {
    getAll: () => axiosClient.get(`${url}`)
}
export default categoryApi