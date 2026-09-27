import { useEffect, useState } from 'react'
import { Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import Home from './pages/Home'
import Auth from './pages/Auth'
import Dashboard from './pages/Dashboard'
import Notes from './pages/Notes'
import Calendar from './pages/Calendar'
import Layout from './components/Layout'
import { getAccessToken } from './services/api.js'

const THEME_KEY = 'taskflow:theme'

function getInitialTheme() {
  try {
    const savedTheme = localStorage.getItem(THEME_KEY)
    return savedTheme === 'light' || savedTheme === 'dark' ? savedTheme : 'dark'
  } catch {
    return 'dark'
  }
}

// Helper to protect routes
function ProtectedRoute({ children }) {
  if (!getAccessToken()) {
    return <Navigate to="/login" replace />
  }
  return children
}

function App() {
  const [theme, setTheme] = useState(getInitialTheme)
  const navigate = useNavigate()

  useEffect(() => {
    function handleUnauthorized() {
      navigate('/login')
    }

    window.addEventListener('taskflow:unauthorized', handleUnauthorized)

    return () => {
      window.removeEventListener('taskflow:unauthorized', handleUnauthorized)
    }
  }, [navigate])

  useEffect(() => {
    document.documentElement.dataset.theme = theme

    try {
      localStorage.setItem(THEME_KEY, theme)
    } catch {
      return
    }
  }, [theme])

  const themeSwitch = (
    <div className="themeSwitch" aria-label="Theme">
      <button
        type="button"
        className={theme === 'dark' ? 'active' : ''}
        onClick={() => setTheme('dark')}
      >
        Dark
      </button>
      <button
        type="button"
        className={theme === 'light' ? 'active' : ''}
        onClick={() => setTheme('light')}
      >
        Light
      </button>
    </div>
  )

  return (
    <Routes>
      <Route path="/login" element={
        <div className="appFrame authFrame">
          {themeSwitch}
          <Auth mode="login" onAuthenticated={() => navigate('/')} onNavigate={(mode) => navigate(`/${mode}`)} />
        </div>
      } />
      
      <Route path="/register" element={
        <div className="appFrame authFrame">
          {themeSwitch}
          <Auth mode="register" onAuthenticated={() => navigate('/')} onNavigate={(mode) => navigate(`/${mode}`)} />
        </div>
      } />

      <Route element={<ProtectedRoute><Layout onLogout={() => navigate('/login')} themeSwitch={themeSwitch} /></ProtectedRoute>}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/tasks" element={<Home />} />
        <Route path="/notes" element={<Notes />} />
        <Route path="/calendar" element={<Calendar />} />
      </Route>
      
      {/* Fallback to handle unknown routes */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default App
