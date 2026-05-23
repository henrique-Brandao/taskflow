import { useState } from 'react'
import api, { saveSession } from '../../services/api.js'
import '../Home/style.css'

function isValidEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}

function getApiErrorMessage(error, isRegister) {
  const status = error.response?.status
  const apiMessage = error.response?.data?.message || error.response?.data?.error

  if (!isRegister && (status === 400 || status === 401 || status === 403)) {
    return 'Invalid email or password.'
  }

  if (isRegister && (status === 400 || status === 409)) {
    return apiMessage || 'This email is already registered.'
  }

  return apiMessage || (isRegister ? 'Could not create your account. Please try again.' : 'Could not sign in. Please try again.')
}

function Auth({ mode, onAuthenticated, onNavigate }) {
  const isRegister = mode === 'register'
  const [form, setForm] = useState({
    name: '',
    email: '',
    confirmEmail: '',
    password: '',
    confirmPassword: ''
  })
  const [errors, setErrors] = useState({})
  const [message, setMessage] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  function handleChange(event) {
    const { name, value } = event.target
    setForm(currentForm => ({
      ...currentForm,
      [name]: value
    }))

    setErrors(currentErrors => ({
      ...currentErrors,
      [name]: ''
    }))
    setMessage('')
  }

  function validateForm() {
    const nextErrors = {}
    const email = form.email.trim()
    const confirmEmail = form.confirmEmail.trim()

    if (isRegister && form.name.trim() === '') {
      nextErrors.name = 'Name is required.'
    }

    if (email === '') {
      nextErrors.email = 'Email is required.'
    } else if (!isValidEmail(email)) {
      nextErrors.email = 'Enter a valid email address.'
    }

    if (isRegister) {
      if (confirmEmail === '') {
        nextErrors.confirmEmail = 'Confirm your email.'
      } else if (email !== confirmEmail) {
        nextErrors.confirmEmail = 'Email confirmation does not match.'
      }
    }

    if (form.password === '') {
      nextErrors.password = 'Password is required.'
    }

    if (isRegister) {
      if (form.confirmPassword === '') {
        nextErrors.confirmPassword = 'Confirm your password.'
      } else if (form.password !== form.confirmPassword) {
        nextErrors.confirmPassword = 'Password confirmation does not match.'
      }
    }

    setErrors(nextErrors)
    return Object.keys(nextErrors).length === 0
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setMessage('')

    if (!validateForm()) {
      return
    }

    setIsSubmitting(true)

    try {
      if (isRegister) {
        await api.post('/auth/register', {
          name: form.name.trim(),
          email: form.email.trim(),
          password: form.password
        })

        onNavigate('login')
        return
      }

      const response = await api.post('/auth/login', {
        email: form.email.trim(),
        password: form.password
      })
      const accessToken = response.data.accessToken || response.data.acessToken

      if (!accessToken) {
        setMessage('Login response did not include an access token.')
        return
      }

      saveSession({
        accessToken,
        expiresIn: response.data.expiresIn,
        user: {
          email: form.email.trim()
        }
      })
      onAuthenticated()
    } catch (error) {
      setMessage(getApiErrorMessage(error, isRegister))
    } finally {
      setIsSubmitting(false)
    }
  }

  function renderError(fieldName) {
    return errors[fieldName] ? <span className="fieldError">{errors[fieldName]}</span> : null
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
        <form className="taskForm authForm" onSubmit={handleSubmit} noValidate>
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
                className={errors.name ? 'inputError' : ''}
              />
              {renderError('name')}
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
              className={errors.email ? 'inputError' : ''}
            />
            {renderError('email')}
          </label>

          {isRegister && (
            <label>
              Confirm email
              <input
                type="email"
                name="confirmEmail"
                placeholder="Repeat your email"
                value={form.confirmEmail}
                onChange={handleChange}
                autoComplete="email"
                className={errors.confirmEmail ? 'inputError' : ''}
              />
              {renderError('confirmEmail')}
            </label>
          )}

          <label>
            Password
            <input
              type="password"
              name="password"
              placeholder="Your password"
              value={form.password}
              onChange={handleChange}
              autoComplete={isRegister ? 'new-password' : 'current-password'}
              className={errors.password ? 'inputError' : ''}
            />
            {renderError('password')}
          </label>

          {isRegister && (
            <label>
              Confirm password
              <input
                type="password"
                name="confirmPassword"
                placeholder="Repeat your password"
                value={form.confirmPassword}
                onChange={handleChange}
                autoComplete="new-password"
                className={errors.confirmPassword ? 'inputError' : ''}
              />
              {renderError('confirmPassword')}
            </label>
          )}

          {message && <p className="formMessage errorMessage">{message}</p>}

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
