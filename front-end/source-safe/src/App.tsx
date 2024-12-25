import { useState } from 'react';
import { BiRightArrow } from 'react-icons/bi';
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Login from './Components/auth/Login'; 
import Signup from './Components/auth/Signup'; 
import ConfirmCode from './Components/auth/CofirmCode'; 
import './App.css';

function Home() {
  return (
        <Link to="/login" className='link'>
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
          <Route path="/signup" element={<Signup />} />
          <Route path="/confirmcode" element={<ConfirmCode />} />
        </Routes>
    </Router>
  );
}


export default App;
