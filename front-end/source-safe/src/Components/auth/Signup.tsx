import React, { useState } from 'react';
import { Link, useNavigate } from "react-router-dom";
import styles from './Signup.module.css';
import { MdAlternateEmail, MdPassword, MdVisibility, MdVisibilityOff } from 'react-icons/md';

import { registerUser } from '../../Services/authService';

const Signup = () => {
    const [first_name, setFirstName] = useState<string>('');
    const [last_name, setLastName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirm_password, setCPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [showPassword, setShowPassword] = useState<boolean>(false);

    const navigate = useNavigate();
    const handleSubmit = async (e:  React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (password !== confirm_password) {
            setError('Passwords do not match');
            return;
        }

        try {
            const responseData = await registerUser(first_name, last_name, email, password); 
            localStorage.setItem('email', JSON.stringify(email)); 
            localStorage.setItem('token', JSON.stringify(responseData.token));
            navigate('/confirmcode');
        } catch (err: any) {
            setError(err.message);
        }
    };

    function focusInput(): void {
        const passwordInput = document.getElementById("password") as HTMLInputElement;
        if (passwordInput) {
            passwordInput.focus();
        }
    }
    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h2 className={styles.text}>Sign Up</h2>
                <div className={styles.underline}></div>
            </div>
            <form onSubmit={handleSubmit} className={styles.inputs}>
                <div className={styles.inputgroup}>
                    <input 
                        className={styles.fname}
                        type="text" 
                        placeholder='First Name' 
                        value={first_name} 
                        onChange={(e) => setFirstName(e.target.value)} 
                        required 
                    />
                    <input 
                        className={styles.lname}
                        type="text" 
                        placeholder='Last Name' 
                        value={last_name} 
                        onChange={(e) => setLastName(e.target.value)} 
                        required 
                    />
                </div>
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
                <div className={styles.input}>
                    <div className={styles.icon}><MdPassword /></div>
                    <input 
                        id='password'
                        type= {showPassword ? "text" : "password"} 
                        placeholder='Confirm Password' 
                        value={confirm_password} 
                        onChange={(e) => setCPassword(e.target.value)} 
                        required 
                        
                    />
                    <button className={styles.showHide} type='button' onClick={() => {
                        setShowPassword(!showPassword)
                        focusInput() }
                        }> {showPassword? <MdVisibility /> : <MdVisibilityOff />} </button>
                </div>
                {error && <div className={styles.error}>{error}</div>}
                <div className={styles.submitContainer}>
                    <button type="submit" className={styles.submit}>Sign Up</button>
                </div>
                <div className={styles.signup}>
                    Already have an account? <Link className={styles.logintext} to="/login">Login.</Link>
                </div>
            </form>
        </div>
    );
};

export default Signup;