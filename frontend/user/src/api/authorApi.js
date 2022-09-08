import axiosClient from './axiosClient'

const url = '/author'
const authorApi = {
    getAll: () => axiosClient.get(url)
}
export default authorApi