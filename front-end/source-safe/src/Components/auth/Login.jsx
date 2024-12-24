import React from 'react'
import { Link } from "react-router-dom";
import './Login.css'
import { MdAlternateEmail, MdPassword } from 'react-icons/md'

const Login = () => {
    return (
        <div className='container'>
            <div className='header'>
                <div className='text'>Login</div>
                <div className='underline'></div>
            </div>
            <div className='inputs'>
                <div className='input'>
                    <div className="icon"><MdAlternateEmail/></div>
                    <input type="email" placeholder='email' />
                </div>
                <div className='input'>
                    <div className="icon"><MdPassword/></div>
                    <input type="password" placeholder='password' />
                </div>
            </div>
            <div className="forgot">Forgot password? <Link to="/">Reset password.</Link></div>
            <div className='submitConatiner'>
                <div className="submit">Login</div>
            </div>
        </div>
    )
}

export default Login;