import React, { useState } from 'react';
import { Link } from "react-router-dom";
import styles from './Login.module.css';
import { MdAlternateEmail, MdPassword, MdVisibility, MdVisibilityOff } from 'react-icons/md';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [showPassword, setShowPassword] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        const apiUrl = `http://localhost:8080/api/v1/auth/authenticate`;

        try {
            const response = await fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                throw new Error('Login failed. Please check your credentials.');
            }

            const data = await response.json();
            console.log('Login successful:', data);
        } catch (err) {
            setError(err.message);
        }
    };

    function focusInput() {
        document.getElementById("password").focus();
    } 

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h2 className={styles.text}>Login</h2>
                <div className={styles.underline}></div>
            </div>
            <form onSubmit={handleSubmit} className={styles.inputs}>
                <div className={styles.input}>
                    <div className={styles.icon}><MdAlternateEmail /></div>
                    <input 
                        type="email" 
                        placeholder='Email' 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                        required 
                    />
                </div>
                <div className={styles.input}>
                    <div className={styles.icon}><MdPassword /></div>
                    <input 
                        id='password'
                        type= {showPassword ? "text" : "password"} 
                        placeholder='Password' 
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)} 
                        required 
                        
                    />
                    <button className={styles.showHide} type='button' onClick={() => {
                        setShowPassword(!showPassword)
                        focusInput() }
                        }> {showPassword? <MdVisibility /> : <MdVisibilityOff />} </button>
                </div>
                {error && <div className={styles.error}>{error}</div>}
                <div className={styles.submitContainer}>
                    <button type="submit" className={styles.submit}>Login</button>
                </div>
                <div className={styles.forgot}>
                    Forgot password? <Link className={styles.passtext} to="/">Reset password.</Link>
                </div>
                <div className={styles.signup}>
                    Don't have an account? <Link className={styles.signuptext} to="/signup">Sign Up.</Link>
                </div>
            </form>
        </div>
    );
};

export default Login;