import React from 'react';

import styles from './GroupCard.module.css';

interface GroupProps {
  id: number;
  isOwner: boolean;
  name: string;
  description: string;
  owner: string;
  creation_date: string;
  members_count: number;
  //onClick: () => void;
}

const GroupCard = (props :GroupProps) => {
    const {isOwner, name, description, owner, creation_date, members_count} = props
  return (
    <div className={styles.container}>
        <div className={styles.content}>
            <h3>name: {name}</h3>
            <p>{description}</p>
            <p>Owner: {isOwner? 'You' : owner}</p>
        </div>
      
      <div className={styles.owner}>
        {isOwner && <button className={styles.invite_button} >Invite someone</button>}
        {isOwner && <p>Date Created: {creation_date}</p>}
        {isOwner && <p>No. of Members: {members_count}</p>}
        </div>
    </div>
  );
};

export type { GroupProps };
export default GroupCard;