import { FormEvent, useState } from "react";
import { createGroup } from "../../Services/groupService";


const NewGroup = () => {
    const [name, setName] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [error, setError] = useState<string>('');
    
    const handleSubmit = async (e: FormEvent<HTMLFormElement>) =>{
        e.preventDefault();
        setError('');

        try {
            const token = localStorage.getItem('token');
            const data = {
                name: name,
                description: description
            }
            console.log(data);
            if (!token) {
                return;
            }
            const responseData = await createGroup(token, data);
        } catch (err: any) {
            setError(err.message);
        }
    }

    return (
        <div>
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
                <button type="submit" >Create</button>
            </form>
            {error && <div>{error}</div>}
        </div>
      );
}

export default NewGroup;