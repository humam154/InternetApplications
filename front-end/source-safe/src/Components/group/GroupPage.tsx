import React, { useEffect, useState } from "react";
import { MdArrowBack, MdUpload } from "react-icons/md";
import { Link, useLocation, useParams } from "react-router-dom";

import styles from './GroupPage.module.css';
import FileCard, { FileProps } from '../file/FileCard';
import FilesList from '../file/FilesList';
import { downloadManyFiles, getFiles, uploadFile, uploadFileData } from '../../Services/fileService';

export enum Filter {
  NONE = '',
  PENDING = 'pending',
  ACCEPTED = 'accepted',
  IN_USE = 'in_use'
}

const GroupPage = () => {
    const [checkedFileIds, setCheckedFileIds] = useState<number[]>([]);
    const [filter, setFilter] = useState<Filter>(Filter.NONE);
    const [files, setFiles] = useState<FileProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const { gid } = useParams();
    const location = useLocation();
    const state = location.state;
    const { group_name } = state;
    const { is_owner } = state;

    const fetchFiles = async (token: string) => {
      try {
        const data = await getFiles(token, gid, filter);
        setFiles(data.data);
      } catch (err) {
        setError("Failed to fetch files");
      } finally {
        setLoading(false);
      }
    };

    useEffect(() => {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("User is not authenticated");
        setLoading(false);
        return;
      }
      fetchFiles(token);
    }, [filter]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;

    const handleFileUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
      const token = localStorage.getItem("token");

      if (!token) {
        //TODO deal with this case, maybe redirect to login page
        alert("Your session has ended, please log in again!");
        return;
      }

      const file = e.target.files?.[0];

      if (gid && file) {
        const data: uploadFileData = {
          file: file,
          groupId: gid,
        };

        try {
          await uploadFile(token, data);
          alert("File uploaded successfully!");
          fetchFiles(token);
        } catch (error) {
          console.error("Error uploading file:", error);
          alert("An error occurred while uploading the file.");
        }
      } else {
        alert("Please select a file to upload.");
      }
    };

    const handleDownload = async () => {
      if (checkedFileIds.length === 0) return;
        const token = localStorage.getItem("token");

      if (!token) {
        //TODO deal with this case, maybe redirect to login page
        alert("Your session has ended, please log in again!");
        return;
      }

        try {
          console.log('sldsbclknkcdnk')
          await downloadManyFiles(token, checkedFileIds);
          alert("File downloaded successfully!");
        } catch (error) {
          console.error("Error uploading file:", error);
          alert("An error occurred while uploading the file.");
        }
    };

    const disableDownloadButton = () => {
      if(checkedFileIds.length === 0) {
        return true;
      }
      return checkedFileIds.some(id => {
        const file = files.find(file => file.id === id);
        return file && file.in_use;
      });
    };

    return (
      <div className={styles.container}>
          <div className={styles.banner}>
              <Link to="/home/groups">
                  <MdArrowBack />
              </Link>
              <h2>{group_name}</h2>

              <button onClick={() => {
                const input = document.getElementById('fileInput') as HTMLInputElement;
                if(input) {
                  input.click();
                }
              }}> <MdUpload /></button>
              <input type="file" id="fileInput" style={{ display: 'none' }} onChange={handleFileUpload} />
          </div>

          <div className={styles.filterButtons}>
            {is_owner && (
              <>
                <button onClick={() => setFilter(Filter.NONE)} disabled={filter == Filter.NONE}>All</button>
                <button onClick={() => setFilter(Filter.PENDING)} disabled={filter == Filter.PENDING}>Pending</button>
                <button onClick={() => setFilter(Filter.ACCEPTED)} disabled={filter == Filter.ACCEPTED}>Accepted</button>
              </>
            )}
          </div>

          <button onClick={handleDownload} disabled={disableDownloadButton()}>
                Download Files
            </button>

          <FilesList
                items={files}
                renderer={(file) => <FileCard key={file.id} {...file} />}
                onCheckedChange={setCheckedFileIds}
            />
      </div>
    );
}

export default GroupPage;