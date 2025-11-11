import { BrowserRouter, Route, Routes } from 'react-router-dom'
import './App.css'
import AboutPage from './components/AboutPage'
import LandingPage from './components/LandingPage'

function App() {

  return (
    <BrowserRouter>
        <Routes>
          <Route path='/' element={<LandingPage/>} />
          <Route path='/about' element={<AboutPage/>} />
        </Routes>
      </BrowserRouter>
  )
}

export default App
