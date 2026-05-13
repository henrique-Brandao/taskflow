import { useEffect } from 'react'
import './style.css'
import Trash from '../../assets/trash.svg'
import api from '../../services/api.js'

function Home() {

  let tasks = []
  
  async function getTask() {
    tasks = await api.get('/task')
  }

  useEffect(() => {
    getTask()
  }, []);

  return (
    <div className="container">
      <form action="" className='taskForm'>
        <h1>TaskFLow</h1>
        <input type="text" placeholder='Title' name="Title" />
        <input type="text" placeholder='Description' name="description" />
        <button type='button'>Create Task</button>
      </form>

      {Tasks.map(task => (
        <div key={task.id} className='taskCard'>
          <div>
            <p>Name: <span>{task.title}</span></p>
            <p>Description: <span>{task.description}</span></p>
            <p>situation: <span>{task.situation ? 'Done' : 'Pending'}</span></p>
            <p>Created at: <span>{task.createdAt}</span></p>
          </div>
          <button>
            <img src={Trash} alt="Trash image" width="24" height="24" />
          </button>
        </div>
      ))}

    </div>


  )
}

export default Home
