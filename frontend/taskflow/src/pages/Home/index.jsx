import { useEffect, useState, useRef } from 'react'
import './style.css'
import Trash from '../../assets/trash.svg'
import api from '../../services/api.js'

function formatCreatedAt(dateValue) {
  if (!dateValue) {
    return 'Unknown date'
  }

  const date = new Date(dateValue)

  if (Number.isNaN(date.getTime())) {
    return dateValue
  }

  return new Intl.DateTimeFormat('en-US', {
    month: 'long',
    day: 'numeric',
    year: 'numeric',
    hour: 'numeric',
    minute: '2-digit'
  }).format(date)
}

function getTaskTime(task) {
  const time = new Date(task.createdAt).getTime()

  return Number.isNaN(time) ? 0 : time
}

function sortTasks(tasks) {
  return [...tasks].sort((firstTask, secondTask) => {
    if (firstTask.completed !== secondTask.completed) {
      return firstTask.completed ? 1 : -1
    }

    return getTaskTime(secondTask) - getTaskTime(firstTask)
  })
}

function Home() {
  const [tasks, setTasks] = useState([])
  const [editingTask, setEditingTask] = useState(null)
  const completedTasks = tasks.filter(task => task.completed).length
  const sortedTasks = sortTasks(tasks)

  const inputTitleRef = useRef()
  const inputDescriptionRef = useRef()

  async function getTask() {
    const response = await api.get('/task')
    setTasks(response.data)
  }

  async function createTask() {
    if (inputTitleRef.current.value.trim() === '' || inputDescriptionRef.current.value.trim() === '') {
      return
    }

    await api.post('/task', {
      title: inputTitleRef.current.value,
      description: inputDescriptionRef.current.value
    })

    clearForm()
    getTask()
  }

  function clearForm() {
    inputTitleRef.current.value = ''
    inputDescriptionRef.current.value = ''
  }

  function startEditing(task) {
    setEditingTask(task)
    inputTitleRef.current.value = task.title
    inputDescriptionRef.current.value = task.description
  }

  function cancelEditing() {
    setEditingTask(null)
    clearForm()
  }

  async function deleteTask(id) {
    await api.delete(`/task/${id}`)

    if (editingTask?.id === id) {
      cancelEditing()
    }

    getTask()
  }

  async function updateTask(id) {
    const request = {}

    if (inputTitleRef.current.value.trim() !== '') {
      request.title = inputTitleRef.current.value
    }

    if (inputDescriptionRef.current.value.trim() !== '') {
      request.description = inputDescriptionRef.current.value
    }

    if (Object.keys(request).length === 0) {
      return
    }

    await api.patch(`/task/${id}`, request)

    setEditingTask(null)
    clearForm()
    getTask()
  }

  async function toggleCompleted(id, completed) {
    await api.patch(`/task/${id}`, {
      completed: !completed
    })

    getTask()
  }

  useEffect(() => {
    async function loadTasks() {
      const response = await api.get('/task')
      setTasks(response.data)
    }

    loadTasks()
  }, [])

  return (
    <main className="appShell">
      <section className="heroPanel">
        <div>
          <p className="eyebrow">Task management</p>
          <h1>TaskFlow</h1>
          <p className="subtitle">Organize tasks, track pending work, and keep your day moving smoothly.</p>
        </div>

        <div className="summaryGrid" aria-label="Task summary">
          <div>
            <strong>{tasks.length}</strong>
            <span>Total</span>
          </div>
          <div>
            <strong>{completedTasks}</strong>
            <span>Completed</span>
          </div>
          <div>
            <strong>{tasks.length - completedTasks}</strong>
            <span>Pending</span>
          </div>
        </div>
      </section>

      <div className="workspace">
        <form action="" className="taskForm">
          <div className="formHeader">
            <span>{editingTask ? 'Editing task' : 'New task'}</span>
            <h2>{editingTask ? 'Update details' : 'Create task'}</h2>
          </div>

          <label>
            Title
            <input type="text" placeholder="Example: Review backlog" name="Title" ref={inputTitleRef} />
          </label>

          <label>
            Description
            <input type="text" placeholder="Example: Prioritize this week's tasks" name="description" ref={inputDescriptionRef} />
          </label>

          <div className="formActions">
            <button className="primaryButton" type="button" onClick={editingTask ? () => updateTask(editingTask.id) : createTask}>
              {editingTask ? 'Save changes' : 'Create task'}
            </button>
            {editingTask && (
              <button type="button" className="secondaryButton" onClick={cancelEditing}>
                Cancel editing
              </button>
            )}
          </div>
        </form>

        <section className="taskList" aria-label="Task list">
          <div className="listHeader">
            <div>
              <span>Tasks</span>
              <h2>Current board</h2>
            </div>
            <p>{tasks.length} item{tasks.length === 1 ? '' : 's'}</p>
          </div>

          <div className="taskListBody">
            {tasks.length === 0 ? (
              <div className="emptyState">
                <strong>No tasks created</strong>
                <span>Use the form to add your first task.</span>
              </div>
            ) : (
              sortedTasks.map(task => (
                <article key={task.id} className={`taskCard ${task.completed ? 'completed' : ''}`}>
                  <div className="taskInfo">
                    <div className="taskTopline">
                      <h3>{task.title}</h3>
                      <span className={`situation ${task.completed ? 'done' : 'pending'}`}>
                        {task.completed ? 'Completed' : 'Incomplete'}
                      </span>
                    </div>

                    <p className="taskDescription">{task.description}</p>
                    <p className="taskDate">Created at {formatCreatedAt(task.createdAt)}</p>

                    <div className="taskActions">
                      <button type="button" className="statusButton" onClick={() => toggleCompleted(task.id, task.completed)}>
                        {task.completed ? 'Reopen' : 'Complete'}
                      </button>
                      <button type="button" className="ghostButton" onClick={() => startEditing(task)}>
                        Edit
                      </button>
                      <button type="button" className="iconButton" aria-label={`Delete ${task.title}`} title="Delete" onClick={() => deleteTask(task.id)}>
                        <img src={Trash} alt="" width="20" height="20" />
                      </button>
                    </div>
                  </div>
                </article>
              ))
            )}
          </div>
        </section>
      </div>
    </main>
  )
}

export default Home
