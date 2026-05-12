import './style.css'
import Trash from '../../assets/trash.svg'

function Home() {

  const Tasks = [{
    id: 1,
    name: 'wash car',
    description: 'i need to wash my car',
    situation: false,
    createdAt: '25/12/2026'
  },
  {
    id: 2,
    name: 'read harry potter',
    description: 'i need to wash my car',
    situation: true,
    createdAt: '25/12/2026'
  }]

  return (
    <div className="container">
      <form action="" className='taskForm'>
        <h1>TaskFLow</h1>
        <input type="text" placeholder='Task name' name="task" />
        <input type="text" placeholder='Description' name="description" />
        <button type='button'>Create Task</button>
      </form>

      {Tasks.map(task => (
        <div key={task.id} className='taskCard'>
          <div>
            <p>Name: <span>{task.name}</span></p>
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
