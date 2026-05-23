import axios from 'axios'

const ACCESS_TOKEN_KEY = 'taskflow:accessToken'
const EXPIRES_IN_KEY = 'taskflow:expiresIn'
const USER_KEY = 'taskflow:user'
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const api = axios.create({
    baseURL: API_URL
})

export function getAccessToken() {
    return localStorage.getItem(ACCESS_TOKEN_KEY)
}

function getDisplayNameFromEmail(email) {
    if (!email) {
        return 'Signed in user'
    }

    return email
        .split('@')[0]
        .split(/[._-]/)
        .filter(Boolean)
        .map(part => part.charAt(0).toUpperCase() + part.slice(1))
        .join(' ') || 'Signed in user'
}

export function getSavedUser() {
    const savedUser = localStorage.getItem(USER_KEY)

    if (!savedUser) {
        return null
    }

    try {
        return JSON.parse(savedUser)
    } catch {
        localStorage.removeItem(USER_KEY)
        return null
    }
}

export function saveSession({ accessToken, expiresIn, user }) {
    localStorage.setItem(ACCESS_TOKEN_KEY, accessToken)

    if (expiresIn) {
        localStorage.setItem(EXPIRES_IN_KEY, String(expiresIn))
    }

    if (user?.email) {
        localStorage.setItem(USER_KEY, JSON.stringify({
            name: user.name || getDisplayNameFromEmail(user.email),
            email: user.email
        }))
    }
}

export function clearSession() {
    localStorage.removeItem(ACCESS_TOKEN_KEY)
    localStorage.removeItem(EXPIRES_IN_KEY)
    localStorage.removeItem(USER_KEY)
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
