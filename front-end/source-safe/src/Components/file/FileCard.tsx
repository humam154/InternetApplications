import { useState } from 'react';
import styles from './FileCard.module.css';
import { CiMenuKebab } from 'react-icons/ci';
import { acceptFile, downloadFile, rejectFile, updateFile, updateFileData } from '../../Services/fileService';

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
  const { id, name, created_by, is_owner, is_group_owner, checked_by_user, accepted, in_use, version } = props;
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = () => {
    setMenuOpen((prev) => !prev);
  };

  const handleFileUpdate = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const token = localStorage.getItem("token");
  
        if (!token) {
          //TODO deal with this case, maybe redirect to login page
          alert("Your session has ended, please log in again!");
          return;
        }
  
        const file = e.target.files?.[0];
  
        if (id && file) {
          const data: updateFileData = {
            file: file,
            fileId: id.toString(),
          };
  
          try {
            await updateFile(token, data);
            alert("File updated successfully!");
          } catch (error) {
            console.error("Error updating file:", error);
            alert("An error occurred while updating the file.");
          }
        } else {
          alert("Please select a file to update.");
        }
  };

  const handleFileDownload = async (e: React.MouseEvent<HTMLButtonElement>) => {
    const token = localStorage.getItem("token");

    if (!token) {
      //TODO deal with this case, maybe redirect to login page
      alert("Your session has ended, please log in again!");
      return;
    }

      try {
        await downloadFile(token, id);
        alert("File downloaded successfully!");
      } catch (error) {
        console.error("Error downloading file:", error);
        alert("An error occurred while downloading the file.");
      }
  };

  const handleFileAccept = async (e: React.MouseEvent<HTMLButtonElement>) => {
    const token = localStorage.getItem("token");

    if (!token) {
      //TODO deal with this case, maybe redirect to login page
      alert("Your session has ended, please log in again!");
      return;
    }

    try {
      await acceptFile(token, id);
      alert("File accepted successfully!");
    } catch (error) {
      console.error("Error accepting file:", error);
      alert(error);
    }
};

const handleFileReject = async (e: React.MouseEvent<HTMLButtonElement>) => {
  const token = localStorage.getItem("token");

  if (!token) {
    //TODO deal with this case, maybe redirect to login page
    alert("Your session has ended, please log in again!");
    return;
  }

  try {
    await rejectFile(token, id);
    alert("File updated successfully!");
  } catch (error) {
    console.error("Error rejecting file:", error);
    alert(error);
  }
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
        {is_group_owner && !accepted && <button className={`${styles.button} ${styles.accept}`} onClick={handleFileAccept}>Accept</button>}
        {is_group_owner && !accepted && <button className={`${styles.button} ${styles.reject}`} onClick={handleFileReject}>Reject</button>}
      </div>

      <button className={styles.menubutton} onClick={toggleMenu}>
        <CiMenuKebab />
      </button>

      {menuOpen && (
        <div className={styles.menu}>
          <button className={styles.menuButton} disabled={in_use ? checked_by_user ? false : true : true} onClick={() => {
              const input = document.getElementById('fileUpdate') as HTMLInputElement;
              if(input) {
                input.click();
              }
            }}
            title="updating the file checks it out and unlocks it"
            >Update</button>
          <input type="file" id="fileUpdate" style={{ display: 'none' }} onChange={handleFileUpdate} />
          <button className={styles.menuButton} disabled={in_use} onClick={handleFileDownload} title="downloading the file checks it in and locks it until you update it">Download</button>
        </div>
      )}
    </div>
  );
};

export type { FileProps };
export default FileCard;
