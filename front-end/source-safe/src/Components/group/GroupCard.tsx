import React, { useEffect } from 'react';

import styles from './GroupCard.module.css';
import { Link } from 'react-router-dom';
import { deleteGroup } from '../../Services/groupService';

interface GroupProps {
  gid: number;
  is_owner: boolean;
  name: string;
  description: string;
  owner: string;
  creation_date: string;
  members_count: number;
  //onClick: () => void;
}

const GroupCard = (props :GroupProps) => {
    const {gid, is_owner, name, description, owner, creation_date, members_count} = props;
    const date = new Date(creation_date).toDateString();

  
    const handleGroupDelete = async (e: React.MouseEvent<HTMLButtonElement>) =>{
        e.preventDefault();

        try {
            const token = localStorage.getItem('token');
            if (!token) {
                return;
            }
            const responseData = await deleteGroup(token, gid);
        } catch (err: any) {
            alert(err.message);
        }
    }


    useEffect(() => {
      handleGroupDelete
    }, []);
    
  return (
    <div className={styles.container}>
      <Link className={styles.group} to={`/home/groups/${gid}`} state={{ group_name: name, is_owner: is_owner }}>
          <div className={styles.content}>
              <h3>name: {name}</h3>
              <p>{description}</p>
              <p>Owner: {is_owner? 'You' : owner}</p>
          </div>
      </Link>
        
        <div className={styles.owner}>
          {is_owner && <Link className={styles.invite_button} to={"searchuser"} state={{gid, isMember: false}} title="add a new group member">Invite someone</Link>}
          {is_owner && <p>{date}</p>}
          {is_owner && <p>No. of Members: {members_count}</p>}
          {is_owner && <button className={styles.delete_button} onClick={handleGroupDelete} title="delete group permanently">Delete group</button>}
        </div>
    </div>
  );
};

export type { GroupProps };
export default GroupCard;