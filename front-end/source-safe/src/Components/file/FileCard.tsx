import { useState } from 'react';
import styles from './FileCard.module.css';
import { CiMenuKebab } from 'react-icons/ci';

interface FileProps {
  id: number;
  name: string;
  group_name: string;
  created_by: string;
  is_owner: boolean;
  is_group_owner: boolean;
  checked_by_user: boolean;
  accepted: boolean;
  in_use: boolean;
  version: number;
}

const FileCard = (props: FileProps) => {
  const { id, name, group_name, created_by, is_owner, is_group_owner, checked_by_user, accepted, in_use, version } = props;
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = () => {
    setMenuOpen((prev) => !prev);
  };

  return (
    <div className={styles.container}>
      <div className={styles.content}>
        <h3>File name: {name}</h3>
        <p>Created by: {is_owner ? 'You' : created_by}</p>
        <p>In use? {in_use ? 'Yes' : 'No'}</p>
        <p>Version: {version}</p>
      </div>

      <div className={styles.owner}>
        {is_group_owner && accepted && <button className={`${styles.button} ${styles.delete}`}>Delete</button>}
        {is_group_owner && !accepted && <button className={`${styles.button} ${styles.accept}`}>Accept</button>}
        {is_group_owner && !accepted && <button className={`${styles.button} ${styles.reject}`}>Reject</button>}
      </div>

      <button className={styles.menubutton} onClick={toggleMenu}>
        <CiMenuKebab />
      </button>

      {menuOpen && (
        <div className={styles.menu}>
          <button className={styles.menuButton} disabled={!checked_by_user}>Update</button>
          <button className={styles.menuButton} disabled={in_use}>Download</button>
        </div>
      )}
    </div>
  );
};

export type { FileProps };
export default FileCard;
