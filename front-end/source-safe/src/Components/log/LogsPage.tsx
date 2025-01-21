import { useEffect, useState } from "react";
import { getLogs } from "../../Services/logService";
import Pagination from "./Pagination";
import styles from "./LogsPage.module.css";


interface LogProps {
  id: number,
  action: string,
  user_name: string,
  time: string
}

export enum LogsFilter {
  NONE = '',
  GROUPS = 'GROUPS',
  FILES = 'FILES',
  INVITES = 'INVITES'
}

const LogsPage = () => {
    const [filter, setFilter] = useState<LogsFilter>(LogsFilter.NONE);
    const [logs, setLogs] = useState<LogProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(10);
  
  
    const handlePrevPage = (prevPage: number) => {
      setPage((prevPage) => prevPage - 1);
    };
  
    const handleNextPage = (nextPage: number) => {
      setPage((nextPage) => nextPage + 1);
    };
    
    const fetchLogs = async () => {
      const token = localStorage.getItem("token");

      if (!token) {
        setError("User is not authenticated");
        setLoading(false);
        return;
      }
  
     try {
       var logs;
   
       logs = await getLogs(token, page, filter);
       
       setTotalPages(logs.totalPages);
       setLogs(logs.content);
       setLoading(false);
       
     } catch (error: any) {
      setLoading(false);
      setError(error.message);
     }
    }
  
    useEffect(() => {
      fetchLogs();
    }, [page, filter]);


  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;
    
    return(
            <div className={styles.container}>
              <table>
                <thead>
                  <tr>
                    <th>Action</th>
                    <th>Username</th>
                    <th>Time</th>
                  </tr>
                </thead>
                <tbody>{
                    logs.map((log) => (
                      <tr>
                        <td>
                          {log.action}
                        </td>
                        <td>
                          {log.user_name}
                        </td>
                        <td>
                          {log.time}
                        </td>
                      </tr>
                  ))}
                </tbody>
              </table>
                <Pagination
            totalPages={totalPages}
            currentPage={page}
            handlePrevPage={handlePrevPage}
            handleNextPage={handleNextPage}
          />
          <div className={styles.dropdown}>
                
            <button onClick={() => setFilter(LogsFilter.NONE)} disabled={filter == LogsFilter.NONE} title="list all logs">All</button>
            <button onClick={() => setFilter(LogsFilter.GROUPS)} disabled={filter == LogsFilter.GROUPS} title="list groups logs">Groups</button>
            <button onClick={() => setFilter(LogsFilter.FILES)} disabled={filter == LogsFilter.FILES} title="list files logs">Files</button>
            <button onClick={() => setFilter(LogsFilter.INVITES)} disabled={filter == LogsFilter.INVITES} title="list invites logs">Invites</button>
        </div>
      </div>
    );
}

export default LogsPage;