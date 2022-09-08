import axiosClient from './axiosClient'

const url = '/publisher'
const publisherApi = {
    getAll: () => axiosClient.get(url)
}
export default publisherApi