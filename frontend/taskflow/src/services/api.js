import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:5432'
})

export default api