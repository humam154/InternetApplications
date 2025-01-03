import React, { useEffect, useState } from "react";
import { MdArrowBack, MdUpload } from "react-icons/md";
import { Link, useLocation, useParams } from "react-router-dom";

import styles from './GroupPage.module.css';
import FileCard, { FileProps } from '../file/FileCard';
import FilesList from '../file/FilesList';
import { getFiles } from '../../Services/fileService';

const handleFileUpload = async (e: any) => {
  const token = localStorage.getItem("token");
  if (!token) {
    //TODO deal with this case, maybe redirect to login page
    alert("your session has ended, please log in again!");
    throw ("User is not authenticated!");
  }

  const file = e.target.files[0];

};

const GroupPage = () => {
    const [files, setFiles] = useState<FileProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const { gid } = useParams();
    const location = useLocation();
    const state = location.state;
    const { group_name } = state;

      useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
          setError("User is not authenticated");
          setLoading(false);
          return;
        }
        getFiles(token, gid)
          .then((data) => {
            setFiles(data.data);
            setLoading(false);
          })
          .catch((err) => {
            setError("Failed to fetch files");
            setLoading(false);
          });
      }, []);
    
      if (loading) return <p>Loading...</p>;
      if (error) return <p>{error}</p>;
    
    
    return (
      <div className={styles.container}>
          <div className={styles.banner}>
              <Link to="/home/groups">
                  <MdArrowBack />
              </Link>
              <h2>{group_name}</h2>

              <button onClick={() => {
                const input = document.getElementById('fileInput') as HTMLElement;
                if(input) {
                  input.onclick;
                }
                }}> <MdUpload /></button>
              <input type="file" id="fileInput" style={{ display: 'none' }} onChange={handleFileUpload} />
              
          </div>

          <FilesList
              items={files}
              renderer={(file) => <FileCard key={file.id} {...file} />}
          />

      </div>
    );
}

export default GroupPage;