import { FormEvent, useEffect, useState } from "react";
import { createGroup, CreateGroupData } from "../../Services/groupService";
import { useNavigate } from "react-router-dom";
import styles from "./GroupsPage.module.css";

const NewGroup = () => {
    const [name, setName] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [error, setError] = useState<string>('');
    
    const navigate = useNavigate();
    
    const handleSubmit = async (e: FormEvent<HTMLFormElement>) =>{
        e.preventDefault();
        setError('');

        try {
            const token = localStorage.getItem('token');
            const data: CreateGroupData ={
                name: name,
                description: description
            }
            if (!token) {
                return;
            }
            const responseData = await createGroup(token, data);
            navigate("/home/groups");
        } catch (err: any) {
            setError(err.message);
        }
    };

    useEffect(() => {
        handleSubmit
    }, []);

    return (
        <div className={styles.newContainer}>
          <form onSubmit={handleSubmit}>
                <div>
                    <input 
                        type="text" 
                        placeholder='Group name' 
                        value={name} 
                        onChange={(e) => setName(e.target.value)} 
                        required 
                    />
                    <input 
                        type="text" 
                        placeholder='Description' 
                        value={description} 
                        onChange={(e) => setDescription(e.target.value)} 
                        required 
                    />
                </div>
                <button className={styles.create} type="submit" >Create</button>
            </form>
            {error && <div>{error}</div>}
        </div>
      );
}

export default NewGroup;