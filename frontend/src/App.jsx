import { useEffect, useState } from 'react'
import Home from './pages/Home'
import Auth from './pages/Auth'
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

function App() {
  const [view, setView] = useState(() => getAccessToken() ? 'tasks' : 'login')
  const [theme, setTheme] = useState(getInitialTheme)

  useEffect(() => {
    function handleUnauthorized() {
      setView('login')
    }

    window.addEventListener('taskflow:unauthorized', handleUnauthorized)

    return () => {
      window.removeEventListener('taskflow:unauthorized', handleUnauthorized)
    }
  }, [])

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

  if (view === 'tasks' && getAccessToken()) {
    return (
      <div className="appFrame tasksFrame">
        {themeSwitch}
        <Home onLogout={() => setView('login')} />
      </div>
    )
  }

  return (
    <div className="appFrame authFrame">
      {themeSwitch}
      <Auth
        mode={view === 'register' ? 'register' : 'login'}
        onAuthenticated={() => setView('tasks')}
        onNavigate={setView}
      />
    </div>
  )
}

export default App
