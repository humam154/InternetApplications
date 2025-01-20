import { useEffect, useState } from "react";
import { MdVisibility, MdVisibilityOff } from "react-icons/md";

import styles from './Profile.module.css';
import { changePassword, getProfile, profileData, updateProfile } from "../../Services/userService";


const Profile = () => {
    const [first_name, setFirstName] = useState<string>('');
    const [last_name, setLastName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [currentPassword, setCurrentPassword] = useState<string>('');
    const [newPassword, setNewPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [isEditing, setIsEditing] = useState<boolean>(false);
    const [isPasswordEditing, setIsPasswordEditing] = useState<boolean>(false);
    const [error, setError] = useState<string>('');

    const fetchProfileData = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
          return;
        }
        const profileData = await getProfile(token);
        setFirstName(profileData.data.first_name);
        setLastName(profileData.data.last_name);
        setEmail(profileData.data.email);
    };
    
    const handleProfileUpdate = async () => {
        try {
            const token = localStorage.getItem("token");
            
            if (!token) {
              return;
            }
            await updateProfile(token, { first_name, last_name, email });
            setIsEditing(false);
        } catch (error: any) {
            setError(error.message);
        }
    };

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

    useEffect(() => {
        fetchProfileData();
    }, []);

    return (
        <div>
            {}
            <div className={styles.inputgroup}>
                <input 
                    className={styles.fname}
                    type="text" 
                    placeholder='First Name' 
                    value={first_name} 
                    onChange={(e) => setFirstName(e.target.value)}
                    disabled={!isEditing}
                />
                <input 
                    className={styles.lname}
                    type="text" 
                    placeholder='Last Name' 
                    value={last_name} 
                    onChange={(e) => setLastName(e.target.value)} 
                    disabled={!isEditing}
                />
            </div>
            <div className={styles.input}>
                <input 
                    type="email" 
                    placeholder='Email' 
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                    disabled={!isEditing}
                />
            </div>
            <button onClick={handleProfileUpdate} disabled={!isEditing}>Save</button>
            <button onClick={() => setIsEditing(!isEditing)}>{isEditing? 'Cancel' : 'Edit'}</button>

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
    );
}

export default Profile;
