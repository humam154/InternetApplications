import React from 'react';

import styles from './GroupCard.module.css';
import { Link } from 'react-router-dom';

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
  return (
    <Link to={`/home/groups/${gid}`}>
      <div className={styles.container}>
          <div className={styles.content}>
              <h3>name: {name}</h3>
              <p>{description}</p>
              <p>Owner: {is_owner? 'You' : owner}</p>
          </div>
        
        <div className={styles.owner}>
          {is_owner && <button className={styles.invite_button} >Invite someone</button>}
          {is_owner && <p>{date}</p>}
          {is_owner && <p>No. of Members: {members_count}</p>}
          </div>
      </div>
    </Link>
  );
};

export type { GroupProps };
export default GroupCard;