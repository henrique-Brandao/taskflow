import axios from 'axios'

const ACCESS_TOKEN_KEY = 'taskflow:accessToken'
const EXPIRES_IN_KEY = 'taskflow:expiresIn'

const api = axios.create({
    baseURL: 'http://localhost:8080'
})

export function getAccessToken() {
    return localStorage.getItem(ACCESS_TOKEN_KEY)
}

export function saveSession({ accessToken, expiresIn }) {
    localStorage.setItem(ACCESS_TOKEN_KEY, accessToken)

    if (expiresIn) {
        localStorage.setItem(EXPIRES_IN_KEY, String(expiresIn))
    }
}

export function clearSession() {
    localStorage.removeItem(ACCESS_TOKEN_KEY)
    localStorage.removeItem(EXPIRES_IN_KEY)
}

api.interceptors.request.use(config => {
    const token = getAccessToken()

    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }

    return config
})

api.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401) {
            clearSession()
            window.dispatchEvent(new Event('taskflow:unauthorized'))
        }

        return Promise.reject(error)
    }
)

export default api
