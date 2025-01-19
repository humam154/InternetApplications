import React, { useState } from 'react';
import { Link, useNavigate } from "react-router-dom";
import styles from './Login.module.css';
import { MdAlternateEmail, MdPassword, MdVisibility, MdVisibilityOff } from 'react-icons/md';

import { loginUser, resetPasswordCode } from '../../Services/authService';

const Login: React.FC = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [showPassword, setShowPassword] = useState<boolean>(false);

    const navigate = useNavigate();
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');

        try {
            const responseData = await loginUser(email, password);
            localStorage.setItem('token', JSON.parse(JSON.stringify(responseData.data.token)));
            localStorage.setItem('isAdmin', JSON.parse(responseData.data.is_admin));
            navigate('/home');
        } catch (err: any) {
            setError(err.message);
        }
    };

    const handleForgotPassword = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');

        try {
            await resetPasswordCode(email);
            navigate('/password-reset');
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
        <div className={styles.page}>
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
                        type={showPassword ? "text" : "password"} 
                        placeholder='Password' 
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)} 
                        required 
                    />
                    <button className={styles.showHide} type='button' onClick={() => {
                        setShowPassword(!showPassword);
                        focusInput();
                    }}> 
                        {showPassword ? <MdVisibility /> : <MdVisibilityOff />} 
                    </button>
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
        </div>
    );
};

export default Login;
