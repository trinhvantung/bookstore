import axios from 'axios'
import queryString from 'query-string';

const axiosClient = axios.create({
    // baseURL: process.env.REACT_APP_API_URL,
    baseURL: "http://localhost:8080/api",
    headers: {
        'content-type': 'application/json',
        "Access-Control-Allow-Origin": "*"
    },
    paramsSerializer: params => queryString.stringify(params),
});

axiosClient.interceptors.request.use(async (config) => {
    const token = await localStorage.getItem("accessToken")
    const type = await localStorage.getItem("type")
    if (token && type) {
        config.headers.Authorization = `${type} ${token}`;
    }

    return config;
})

axiosClient.interceptors.response.use((response) => {
    if (response && response.data) {
        return response.data;
    }
    return response;
}, (error) => {
    throw error;
});


export default axiosClient;