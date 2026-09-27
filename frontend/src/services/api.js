import axios from 'axios'

const ACCESS_TOKEN_KEY = 'taskflow:accessToken'
const EXPIRES_IN_KEY = 'taskflow:expiresIn'
const USER_KEY = 'taskflow:user'
const API_URL = 'https://eaeacjdoyg.execute-api.us-east-1.amazonaws.com'
const DEMO_MODE = import.meta.env.VITE_DEMO_MODE === 'true' || (import.meta.env.DEV && import.meta.env.VITE_DEMO_MODE !== 'false')

let demoTasks = [
    {
        id: 'demo-1',
        title: 'Review frontend palette',
        description: 'Check contrast, spacing, and the new dark motion background.',
        completed: false,
        createdAt: new Date(Date.now() - 1000 * 60 * 45).toISOString()
    },
    {
        id: 'demo-2',
        title: 'Validate Railway environment',
        description: 'Confirm JWT and CORS variables before the next deploy.',
        completed: true,
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 5).toISOString()
    },
    {
        id: 'demo-3',
        title: 'Polish task interactions',
        description: 'Try create, edit, complete, reopen, and delete states.',
        completed: false,
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 22).toISOString()
    }
]

const api = axios.create({
    baseURL: API_URL
})

export function getAccessToken() {
    if (DEMO_MODE) {
        return 'demo-token'
    }

    try {
        return localStorage.getItem(ACCESS_TOKEN_KEY)
    } catch {
        return null
    }
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
    if (DEMO_MODE) {
        return {
            name: 'Demo User',
            email: 'demo@taskflow.local'
        }
    }

    let savedUser

    try {
        savedUser = localStorage.getItem(USER_KEY)
    } catch {
        return null
    }

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
    try {
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
    } catch {
        return
    }
}

export function clearSession() {
    try {
        localStorage.removeItem(ACCESS_TOKEN_KEY)
        localStorage.removeItem(EXPIRES_IN_KEY)
        localStorage.removeItem(USER_KEY)
    } catch {
        return
    }
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

function createDemoResponse(data) {
    return Promise.resolve({
        data
    })
}

function createDemoTask(task) {
    return {
        id: crypto.randomUUID(),
        title: task.title,
        description: task.description,
        completed: false,
        createdAt: new Date().toISOString()
    }
}

const demoApi = {
    get(url) {
        if (url === '/task') {
            return createDemoResponse([...demoTasks])
        }

        return createDemoResponse(null)
    },

    post(url, data) {
        if (url === '/task') {
            const task = createDemoTask(data)
            demoTasks = [task, ...demoTasks]
            return createDemoResponse(task)
        }

        return createDemoResponse({
            accessToken: 'demo-token',
            expiresIn: 3600
        })
    },

    patch(url, data) {
        const taskId = url.replace('/task/', '')
        demoTasks = demoTasks.map(task => (
            task.id === taskId ? { ...task, ...data } : task
        ))

        return createDemoResponse(demoTasks.find(task => task.id === taskId) || null)
    },

    delete(url) {
        const taskId = url.replace('/task/', '')
        demoTasks = demoTasks.filter(task => task.id !== taskId)

        return createDemoResponse(null)
    }
}

export default DEMO_MODE ? demoApi : api
