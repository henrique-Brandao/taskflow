import { useEffect, useState, useRef } from 'react'
import './style.css'
import Trash from '../../assets/trash.svg'
import api from '../../services/api.js'

function Home() {
  const [tasks, setTasks] = useState([])

  const inputTitleRef = useRef()
  const inputDescriptionRef = useRef()
  
  
  async function getTask() {
    const response = await api.get('/task')
    setTasks(response.data)
  }

  async function createTask(){
    await api.post('/task', {
      title: inputTitleRef.current.value,
      description: inputDescriptionRef.current.value
    })

    inputTitleRef.current.value = ''
    inputDescriptionRef.current.value = ''
    getTask()
  }

  async function deleteTask(id) {
    await api.delete(`/task/${id}`)
    getTask()
  }

  useEffect(() => {
    getTask()
  }, []);

  return (
    <div className="container">
      <form action="" className='taskForm'>
        <h1>TaskFLow</h1>
        <input type="text" placeholder='Title' name="Title" ref={inputTitleRef}/>
        <input type="text" placeholder='Description' name="description" ref={inputDescriptionRef} />
        <button type='button' onClick={createTask}>Create Task</button>
      </form>

      {tasks.map(task => (
        <div key={task.id} className='taskCard'>
          <div>
            <p>Name: <span>{task.title}</span></p>
            <p>Description: <span>{task.description}</span></p>
            <p>situation: <span>{task.completed ? 'Done' : 'Pending'}</span></p>
            <p>Created at: <span>{task.createdAt}</span></p>
          </div>
          <button>
            <img src={Trash} alt="Trash image" width="24" height="24" onClick={() => deleteTask(task.id)} />
          </button>
        </div>
      ))}

    </div>


  )
}

export default Home
