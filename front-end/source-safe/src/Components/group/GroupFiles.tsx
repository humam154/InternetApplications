import React, { useEffect, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import { MdUpload } from 'react-icons/md';
import styles from './GroupPage.module.css';
import FileCard, { FileProps } from '../file/FileCard';
import { Filter } from './GroupPage';
import { downloadManyFiles, getFiles, uploadFile, uploadFileData } from '../../Services/fileService';
import FilesList from '../file/FilesList';

const GroupFiles = () => {

        const [filter, setFilter] = useState<Filter>(Filter.NONE);
        const [files, setFiles] = useState<FileProps[]>([]);
        const [checkedFileIds, setCheckedFileIds] = useState<number[]>([]);
        
        const { gid } = useParams();
        const location = useLocation();
        const state = location.state;
        const { is_owner } = state;
        const [loading, setLoading] = useState<boolean>(true);
        const [error, setError] = useState<string | null>(null);
        

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
  
      if (loading) return <p>Loading...</p>;
      if (error) return <p>{error}</p>;
  
    return (
        <div>
            <button onClick={() => {
                const input = document.getElementById('fileInput') as HTMLInputElement;
                if(input) {
                  input.click();
                }
              }} title="upload a file"> <MdUpload/></button>
              <input type="file" id="fileInput" style={{ display: 'none' }} onChange={handleFileUpload} />
          
            <div className={styles.filterButtons}>
                      
                    <button onClick={() => setFilter(Filter.NONE)} disabled={filter == Filter.NONE} title="list all files in group">All</button>
                    <button onClick={() => setFilter(Filter.IN_USE)} disabled={filter == Filter.IN_USE || filter == Filter.IN_USE_BY_ME} title="list only files in use">In-Use</button>
    
                {is_owner && (
                  <>
                    <button onClick={() => setFilter(Filter.PENDING)} disabled={filter == Filter.PENDING} title="list files that have not been accepted yet">Pending</button>
                    <button onClick={() => setFilter(Filter.ACCEPTED)} disabled={filter == Filter.ACCEPTED} title="list only accepted files">Accepted</button>
                  </>
                )}
            </div>

            <button onClick={handleDownload} disabled={disableDownloadButton()} title={disableDownloadButton() ? "download many files after selecting" : "download selected files"}>
                Download Files
            </button>

            {(filter == Filter.IN_USE || filter == Filter.IN_USE_BY_ME)  && 
            <>
            In-Use by You
            <input type="checkbox"
              onChange={() => {filter == Filter.IN_USE_BY_ME? setFilter(Filter.IN_USE) : setFilter(Filter.IN_USE_BY_ME)}} 
              title="list only files in use by you"
              />
            </>
            }

          <FilesList
                items={files}
                renderer={(file) => <FileCard key={file.id} {...file} />}
                onCheckedChange={setCheckedFileIds}
            />
        </div>
    );
};

export default GroupFiles;
