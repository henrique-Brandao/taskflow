import { Outlet } from 'react-router-dom'
import Sidebar from '../Sidebar'
import './style.css'

function Layout({ onLogout, themeSwitch }) {
  return (
    <div className="layoutWrapper">
      <Sidebar onLogout={onLogout} />
      <main className="mainContent">
        <div className="topbar">
          {themeSwitch}
        </div>
        <div className="pageContainer">
          <Outlet />
        </div>
      </main>
    </div>
  )
}

export default Layout
