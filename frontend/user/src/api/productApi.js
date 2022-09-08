import axiosClient from './axiosClient'

const url = '/book'
const productApi = {
    getAllByPage: (params) => axiosClient.get(url, { params }),
    get: id => axiosClient.get(`${url}?id=${id}`),
    getBySlug: slug => axiosClient.get(`${url}/${slug}`),
    getLatest: () => axiosClient.get(`${url}/latest`),
    getByCategory: (slug, params) => axiosClient.get(`${url}/category/${slug}`, { params }),
    search: (search, params) => axiosClient.get(`${url}/search/?q=${search}`, { params })

}
export default productApi