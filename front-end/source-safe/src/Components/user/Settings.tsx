import { MdLogout, MdVisibility, MdVisibilityOff } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import { logout } from "../../Services/authService";
import { changePassword } from "../../Services/userService";
import { useState } from "react";

import styles from "./ProfileAndSettings.module.css";

const Settings = () => {
    
    const [currentPassword, setCurrentPassword] = useState<string>('');
    const [newPassword, setNewPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [isPasswordEditing, setIsPasswordEditing] = useState<boolean>(false);
    const [error, setError] = useState<string>('');

    const navigate = useNavigate();

    const handleLogOut = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
          return;
        }
        await logout(token);
        navigate("/login");
    }

    const handlePasswordChange = async () => {

        if(newPassword != confirmPassword){
            setError("New password does not match with confirm password");
            return;
        }
        try {
            const token = localStorage.getItem("token");
            if (!token) {
              return;
            }
            const response = await changePassword(token, { currentPassword, newPassword, confirmPassword });
            localStorage.setItem('token', response.data.token);
            setIsPasswordEditing(false);
        } catch (error: any) {
            setError(error.response.data.message);
        }
    };

    return (
        <div>
            <div>
                <button onClick={handleLogOut}>
                    Log Out {<MdLogout />}
                </button>

                <div>

                <button className={styles.showHide} type='button' onClick={() => {
                    setShowPassword(!showPassword) }
                    }> {showPassword? <MdVisibility /> : <MdVisibilityOff />} </button>
            <div className={styles.input}>
                <input 
                    id='password'
                    type= {showPassword ? "text" : "password"} 
                    placeholder='Current password' 
                    value={currentPassword} 
                    onChange={(e) => setCurrentPassword(e.target.value)} 
                    disabled={!isPasswordEditing}
                />
            </div>
            <div className={styles.input}>
                <input 
                    id='newpassword'
                    type= {showPassword ? "text" : "password"} 
                    placeholder='New password' 
                    value={newPassword} 
                    onChange={(e) => setNewPassword(e.target.value)} 
                    disabled={!isPasswordEditing}
                />
            </div>
            <div className={styles.input}>
                <input 
                    id='confirmpassword'
                    type= {showPassword ? "text" : "password"} 
                    placeholder='Confirm password' 
                    value={confirmPassword} 
                    onChange={(e) => setConfirmPassword(e.target.value)} 
                    disabled={!isPasswordEditing}
                />
            </div>
            <button onClick={handlePasswordChange} disabled={!isPasswordEditing}>Save</button>
            <button onClick={() => setIsPasswordEditing(!isPasswordEditing)}>{isPasswordEditing? 'Cancel' : 'Change password'}</button>
            {error && <div className={styles.error}>{error}</div>}
                </div>
            </div>
        </div>
    );
}

export default Settings;