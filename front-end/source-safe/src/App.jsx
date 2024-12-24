import { useState } from 'react';
import { BiRightArrow } from 'react-icons/bi';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Login from './Components/auth/Login'; 
import './App.css';


function Home() {
  return (
        <Link to="/login" className='link' replace="true">
          <div className='Enter'> Enter <BiRightArrow className='arrow'/></div>
        </Link>
  );
}

function App() {
  const [count, setCount] = useState(0);

  return (
    <Router>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
        </Routes>
    </Router>
  );
}


export default App;
