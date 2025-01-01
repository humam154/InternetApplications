import { useState } from 'react';
import { useLocation } from 'react-router-dom';
import styles from './InviteCard.module.css';
import { accept, reject } from '../../Services/invitationService';

interface InviteProps {
  id: number;
  group_name: string;
  inviter: string;
  invitee: string;
}

const handleAccept = (id: number) => {
  const token = localStorage.getItem("token");
  if (!token) {
    return;
  }
  console.log(`Accepted invite with id: ${id}`);
  accept(token, id);
};

const handleReject = (id: number) => {
  const token = localStorage.getItem("token");
  if (!token) {
    return;
  }
  console.log(`Rejected invite with id: ${id}`);
  reject(token, id);
};

const handleRevoke = (id: number) => {
    const token = localStorage.getItem("token");
    if (!token) {
      return;
    }
    console.log(`Rejected invite with id: ${id}`);
};

const InviteCard = (props: InviteProps) => {
  const { id, group_name, inviter, invitee } = props;

  const location = useLocation();
  const isOutbox = location.pathname.includes("outbox");

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <p>Group Name: {group_name}</p>
        <p>{isOutbox ? `To: ${invitee}` : `From: ${inviter}`}</p>
      </div>

      {isOutbox ?
        <div className={styles.owner}>
            <button className={`${styles.button} ${styles.revoke}`} onClick={() => handleRevoke(id)}>Revoke</button>
        </div>
      :(
        <div className={styles.owner}>
          <button className={`${styles.button} ${styles.accept}`} onClick={() => handleAccept(id)}>Accept</button>
          <button className={`${styles.button} ${styles.reject}`} onClick={() => handleReject(id)}>Reject</button>
        </div>
      )}
    </div>
  );
};

export type { InviteProps };
export default InviteCard;
