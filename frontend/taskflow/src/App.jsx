import { useEffect, useState } from 'react'
import Home from './pages/Home'
import Auth from './pages/Auth'
import { getAccessToken } from './services/api.js'

function App() {
  const [view, setView] = useState(() => getAccessToken() ? 'tasks' : 'login')

  useEffect(() => {
    function handleUnauthorized() {
      setView('login')
    }

    window.addEventListener('taskflow:unauthorized', handleUnauthorized)

    return () => {
      window.removeEventListener('taskflow:unauthorized', handleUnauthorized)
    }
  }, [])

  if (view === 'tasks' && getAccessToken()) {
    return <Home onLogout={() => setView('login')} />
  }

  return (
    <Auth
      mode={view === 'register' ? 'register' : 'login'}
      onAuthenticated={() => setView('tasks')}
      onNavigate={setView}
    />
  )
}

export default App
