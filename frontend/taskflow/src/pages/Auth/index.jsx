import { useState } from 'react'
import api, { saveSession } from '../../services/api.js'
import '../Home/style.css'

function getErrorMessage(error, fallbackMessage) {
  return error.response?.data?.message || error.response?.data?.error || fallbackMessage
}

function Auth({ mode, onAuthenticated, onNavigate }) {
  const isRegister = mode === 'register'
  const [form, setForm] = useState({
    name: '',
    email: '',
    password: ''
  })
  const [message, setMessage] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  function handleChange(event) {
    const { name, value } = event.target
    setForm(currentForm => ({
      ...currentForm,
      [name]: value
    }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setMessage('')
    setIsSubmitting(true)

    try {
      if (isRegister) {
        await api.post('/auth/register', {
          name: form.name,
          email: form.email,
          password: form.password
        })

        onNavigate('login')
        return
      }

      const response = await api.post('/auth/login', {
        email: form.email,
        password: form.password
      })
      const accessToken = response.data.accessToken || response.data.acessToken

      if (!accessToken) {
        setMessage('Login response did not include an access token.')
        return
      }

      saveSession({
        accessToken,
        expiresIn: response.data.expiresIn
      })
      onAuthenticated()
    } catch (error) {
      setMessage(getErrorMessage(error, isRegister ? 'Could not create your account.' : 'Could not sign in.'))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <main className="appShell authShell">
      <section className="heroPanel authHero">
        <div>
          <p className="eyebrow">Task management</p>
          <h1>TaskFlow</h1>
          <p className="subtitle">
            {isRegister ? 'Create your account to start managing your own tasks.' : 'Sign in to access your task board.'}
          </p>
        </div>
      </section>

      <section className="authCard">
        <form className="taskForm authForm" onSubmit={handleSubmit}>
          <div className="formHeader">
            <span>{isRegister ? 'New account' : 'Welcome back'}</span>
            <h2>{isRegister ? 'Create account' : 'Login'}</h2>
          </div>

          {isRegister && (
            <label>
              Name
              <input
                type="text"
                name="name"
                placeholder="Your name"
                value={form.name}
                onChange={handleChange}
                autoComplete="name"
                required
              />
            </label>
          )}

          <label>
            Email
            <input
              type="email"
              name="email"
              placeholder="you@example.com"
              value={form.email}
              onChange={handleChange}
              autoComplete="email"
              required
            />
          </label>

          <label>
            Password
            <input
              type="password"
              name="password"
              placeholder="Your password"
              value={form.password}
              onChange={handleChange}
              autoComplete={isRegister ? 'new-password' : 'current-password'}
              required
            />
          </label>

          {message && <p className="formMessage">{message}</p>}

          <div className="formActions">
            <button className="primaryButton" type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Please wait...' : isRegister ? 'Register' : 'Login'}
            </button>
          </div>

          <button
            type="button"
            className="linkButton"
            onClick={() => onNavigate(isRegister ? 'login' : 'register')}
          >
            {isRegister ? 'Already have an account? Login' : 'Need an account? Register'}
          </button>
        </form>
      </section>
    </main>
  )
}

export default Auth
