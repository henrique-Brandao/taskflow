import { NavLink } from 'react-router-dom'
import { LayoutDashboard, CheckSquare, BookOpen, Calendar, Settings, Sparkles, LogOut } from 'lucide-react'
import './style.css'

function Sidebar({ onLogout }) {
  return (
    <aside className="sidebar">
      <div className="sidebarHeader">
        <Sparkles className="logoIcon" />
        <h2>Taskflow</h2>
      </div>

      <nav className="sidebarNav">
        <NavLink to="/" className={({ isActive }) => (isActive ? 'navItem active' : 'navItem')} end>
          <LayoutDashboard size={20} />
          <span>Dashboard</span>
        </NavLink>
        <NavLink to="/tasks" className={({ isActive }) => (isActive ? 'navItem active' : 'navItem')}>
          <CheckSquare size={20} />
          <span>Tasks</span>
        </NavLink>
        <NavLink to="/notes" className={({ isActive }) => (isActive ? 'navItem active' : 'navItem')}>
          <BookOpen size={20} />
          <span>Notebook</span>
        </NavLink>
        <NavLink to="/calendar" className={({ isActive }) => (isActive ? 'navItem active' : 'navItem')}>
          <Calendar size={20} />
          <span>Calendar</span>
        </NavLink>
      </nav>

      <div className="sidebarFooter">
        <button type="button" className="navItem">
          <Settings size={20} />
          <span>Settings</span>
        </button>
        <button type="button" className="navItem logoutBtn" onClick={onLogout}>
          <LogOut size={20} />
          <span>Logout</span>
        </button>
      </div>
    </aside>
  )
}

export default Sidebar
