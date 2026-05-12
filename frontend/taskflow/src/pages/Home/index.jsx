import { useState } from 'react'
import './style.css'
import Trash from '../../assets/trash.svg'

function Home() {
  const [count, setCount] = useState(0)

  return (
    <div className="container">
      <form action="">
        <h1>TaskFLow</h1>
        <input type="text" placeholder='Task name' name="task" />
        <input type="text" placeholder='Description' name="description" />
        <button type='button'>Create Task</button>
      </form>

      <div>
        <div>
          <p>Name: </p>
          <p>Description: </p>
          <p>situation: </p>
          <p>Created at: </p>
        </div>
        <button>
          <img src={Trash} alt="Trash image"width="24" height="24"/>
        </button>
      </div>
    </div>


  )
}

export default Home
