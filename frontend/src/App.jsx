import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Dashboard from './pages/Dashboard'
import { AuthProvider } from './context/AuthContext'

function App() {

  return (
      <Dashboard />
  )
}

export default App
