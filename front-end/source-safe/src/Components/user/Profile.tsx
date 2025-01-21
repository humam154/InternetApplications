import { useEffect, useState } from "react";

import styles from './ProfileAndSettings.module.css';
import { getProfile, updateProfile } from "../../Services/userService";


const Profile = () => {
    const [first_name, setFirstName] = useState<string>('');
    const [last_name, setLastName] = useState<string>('');
    const [email, setEmail] = useState<string>('');
    const [isEditing, setIsEditing] = useState<boolean>(false);
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

    useEffect(() => {
        fetchProfileData();
    }, []);

    return (
        <div className={styles.page}>
            <div className={styles.container}>

            <div className={styles.input}>
                <p>First name:</p>
                <input 
                    className={styles.fname}
                    type="text" 
                    placeholder='First Name' 
                    value={first_name} 
                    onChange={(e) => setFirstName(e.target.value)}
                    disabled={!isEditing}
                    title="first name"
                />
                <p>Last name:</p>
                <input 
                    className={styles.lname}
                    type="text" 
                    placeholder='Last Name' 
                    value={last_name} 
                    onChange={(e) => setLastName(e.target.value)} 
                    disabled={!isEditing}
                    title="last name"
                />
                <p>Email:</p>
                <input 
                    type="email" 
                    placeholder='Email' 
                    value={email} 
                    onChange={(e) => setEmail(e.target.value)} 
                    disabled={!isEditing}
                    title="email address"
                />
            </div>
            <button className={styles.save} onClick={handleProfileUpdate} disabled={!isEditing}>Save</button>
            <button className={isEditing? styles.cancel : styles.edit} onClick={() => setIsEditing(!isEditing)}>{isEditing? 'Cancel' : 'Edit'}</button>

            {error && <div className={styles.error}>{error}</div>}
            </div>
        </div>
    );
}

export default Profile;
