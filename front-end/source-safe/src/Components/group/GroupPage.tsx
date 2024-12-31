import React, { useEffect, useState } from "react";
import { MdArrowBack } from "react-icons/md";
import { Link, useParams } from "react-router-dom";

import styles from './GroupPage.module.css';
import FileCard, { FileProps } from '../file/FileCard';
import FilesList from '../file/FilesList';
import { getFiles } from '../../Services/fileService';


const GroupPage = () => {
    const [files, setFiles] = useState<FileProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const { gid } = useParams();

      useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
          setError("User is not authenticated");
          setLoading(false);
          return;
        }
        //TODO fix this data.data because that looks ugly
        getFiles(token, gid)
          .then((data) => {
            setFiles(data.data);
            setLoading(false);
          })
          .catch((err) => {
            setError("Failed to fetch groups");
            setLoading(false);
          });
      }, []);
    
      if (loading) return <p>Loading...</p>;
      if (error) return <p>{error}</p>;
    
    
    return <div className={styles.container}>
        <div className={styles.banner}>
            <Link to="/groups">
                <MdArrowBack />
            </Link>
        </div>

        <FilesList
            items={files}
            renderer={(file) => <FileCard key={file.id} {...file} />}
        />

    </div>
}

export default GroupPage;