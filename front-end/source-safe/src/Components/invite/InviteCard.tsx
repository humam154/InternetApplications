import { useState } from 'react';
import { useLocation } from 'react-router-dom';
import styles from './InviteCard.module.css';
import { accept, reject, revoke } from '../../Services/invitationService';

interface InviteProps {
  id: number;
  group_name: string;
  inviter: string;
  invitee: string;
  invite_date: Date;
}

const handleAccept = async (id: number) => {
  const token = localStorage.getItem("token");
  if (!token) {
    return;
  }
  await accept(token, id);
};

const handleReject = async (id: number) => {
  const token = localStorage.getItem("token");
  if (!token) {
    return;
  }
  await reject(token, id);
};

const handleRevoke = async (id: number) => {
    const token = localStorage.getItem("token");
    if (!token) {
      return;
    }
    await revoke(token, id);
};

const InviteCard = (props: InviteProps) => {
  const { id, group_name, inviter, invitee, invite_date } = props;
  const date = new Date(invite_date).toDateString();

  const location = useLocation();
  const isOutbox = location.pathname.includes("outbox");

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <p>Group Name: {group_name}</p>
        <p>{isOutbox ? `To: ${invitee}` : `From: ${inviter}`}</p>
        <p>On: {date}</p>
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
