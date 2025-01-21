import { useState } from "react";
import { MdAlternateEmail, MdPassword, MdVisibility, MdVisibilityOff, MdNumbers } from "react-icons/md";
import { Link, useNavigate } from "react-router-dom";

import styles from "./PasswordReset.module.css";
import { resetPasswowrd } from "../../Services/authService";

const PasswordReset = () => {
    const [code, setCode] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [passwordConfirm, setCPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [showPassword, setShowPassword] = useState<boolean>(false);
    
    const navigate = useNavigate();

    const handleSubmit = async (e:  React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (password !== passwordConfirm) {
            setError('Passwords do not match');
            return;
        }

        try {
            const responseData = await resetPasswowrd({code, email, password, passwordConfirm});
            alert(responseData.message);
            navigate("/home");
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
                <h2 className={styles.text}>Reset Password</h2>
                <div className={styles.underline}></div>
            </div>
            <form onSubmit={handleSubmit} className={styles.inputs}>
                <div className={styles.inputgroup}>
                <div className={styles.icon}><MdNumbers /></div>
                <input 
                    className={styles.code}
                    type="text" 
                    maxLength={6}
                    minLength={6}
                    placeholder='Code' 
                    value={code} 
                    onChange={(e) => {
                        const { value } = e.target;
                        if (/^\d*\.?\d*$/.test(value)) {
                            setCode(value);
                        }
                    }} 
                    required 
                />
                </div>
                <div className={styles.input}>
                    <div className={styles.icon}><MdAlternateEmail /></div>
                    <input className={styles.passinput}
                        type="email" 
                        placeholder='Email' 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                        required 
                    />
                </div>
                <div className={styles.input}>
                    <div className={styles.icon}><MdPassword /></div>
                    <input className={styles.passinput}
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
                    <input className={styles.passinput}
                        id='password'
                        type= {showPassword ? "text" : "password"} 
                        placeholder='Confirm Password' 
                        value={passwordConfirm} 
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
                    <button type="submit" className={styles.submit}>Reset Password</button>
                </div>
                <div className={styles.bottomtext}>
                    Did not receive code? <button className={styles.resendtext}>Resend code.</button>
                </div>
            </form>
        </div>
        </div>
    );
}

export default PasswordReset;