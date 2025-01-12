import { useState } from 'react';
import styles from './FileCard.module.css';
import { CiMenuKebab } from 'react-icons/ci';
import { acceptFile, downloadFile, updateFile, updateFileData } from '../../Services/fileService';

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
            console.error("Error uploading file:", error);
            alert("An error occurred while uploading the file.");
          }
        } else {
          alert("Please select a file to upload.");
        }
  };

  const handleFileDownload = async (e: React.MouseEvent<HTMLButtonElement>) => {
    const token = localStorage.getItem("token");

    console.log('sldsbclknkcdnk')
    if (!token) {
      //TODO deal with this case, maybe redirect to login page
      alert("Your session has ended, please log in again!");
      return;
    }

      try {
        console.log('sldsbclknkcdnk')
        await downloadFile(token, id);
        alert("File downloaded successfully!");
      } catch (error) {
        console.error("Error uploading file:", error);
        alert("An error occurred while uploading the file.");
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
      alert("File updated successfully!");
    } catch (error) {
      console.error("Error uploading file:", error);
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
        {is_group_owner && !accepted && <button className={`${styles.button} ${styles.reject}`}>Reject</button>}
      </div>

      <button className={styles.menubutton} onClick={toggleMenu}>
        <CiMenuKebab />
      </button>

      {menuOpen && (
        <div className={styles.menu}>
          <button className={styles.menuButton} disabled={in_use ? checked_by_user ? false : true : true} onClick={() => {
              const input = document.getElementById('fileUpload') as HTMLInputElement;
              if(input) {
                input.click();
              }
            }}>Update</button>
          <input type="file" id="fileUpload" style={{ display: 'none' }} onChange={handleFileUpdate} />
          <button className={styles.menuButton} disabled={in_use} onClick={handleFileDownload}>Download</button>
        </div>
      )}
    </div>
  );
};

export type { FileProps };
export default FileCard;
