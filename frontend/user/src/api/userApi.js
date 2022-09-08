import axiosClient from './axiosClient'

const url = '/user'
const userApi = {
    login: (login) => axiosClient.post(`${url}/login`, login),
    register: (register) => axiosClient.post(`${url}/register`, register)
}
export default userApi