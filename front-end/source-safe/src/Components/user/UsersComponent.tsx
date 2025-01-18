import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';

import UsersList from './UsersList';
import { searchUser } from '../../Services/userService';
import UserCard, { UserProps } from './UserCard';

const UsersComponent = () => {
    const [query, setQuery] = useState('');
    const [users, setUsers] = useState<UserProps[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const location = useLocation();
    const state = location.state;
    const {gid} = state;
    const {isMember} = state;
    
    const handleSearch = async (event: React.ChangeEvent<HTMLInputElement>) => {
        const value = event.target.value;
        setQuery(value);


        if (value.trim() === '') {
            setUsers([]);
            return;
        }

        setLoading(true);
        setError(null);

        const token = localStorage.getItem('token');
        if (!token) {
            alert("Your session has ended, please log in again!");
            setLoading(false);
            return;
        }

        try {
            const results = await searchUser(token, value, isMember, gid);
            setUsers(results.data);
        } catch (err) {
            console.error("Error searching for users:", err);
            setError("Failed to fetch users");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <input 
                type="text" 
                value={query} 
                onChange={handleSearch} 
                placeholder="Search for users..."
            />
            {loading && <p>Loading...</p>}
            {error && <p>{error}</p>}
            <UsersList 
                items={users} 
                renderer={(user) => <UserCard {...user} />}
            />
        </div>
    );
};

export default UsersComponent;