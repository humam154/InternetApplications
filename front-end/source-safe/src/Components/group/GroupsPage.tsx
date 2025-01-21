import React, { useEffect, useState } from "react";
import { Link, Outlet } from "react-router-dom";
import { MdRefresh } from "react-icons/md";

import GroupsList from "./GroupsList";
import { getAllGroups, getGroups } from "../../Services/groupService";
import GroupCard, { GroupProps } from "./GroupCard";
import styles from "./GroupsPage.module.css";


const GroupsPage = () => {
  const [groups, setGroups] = useState<GroupProps[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  
  const fetchGroups = async () => {
    const token = localStorage.getItem("token");
    const isAdmin: boolean = JSON.parse(localStorage.getItem("isAdmin")!,  (k, v) => v === "true" ? true : v === "false" ? false : v);
  
    if (!token) {
      setError("User is not authenticated");
      setLoading(false);
      return;
    }

   try {
     var groups;
 
     if(isAdmin) {
       groups = await getAllGroups(token);
     } else {
       groups = await getGroups(token);
     }
 
     setGroups(groups.data);
     setLoading(false);
     
   } catch (error: any) {
    setLoading(false);
    setError(error.message);
   }
  }

  useEffect(() => {
    fetchGroups();
  }, []);
  


  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className={styles.container}>
    <div className={styles.groups}>
    <GroupsList
            items={groups}
            renderer={(group) => <GroupCard key={group.gid} {...group} />}
          />
    </div>
      
      <div className={styles.outlet}>
      <button className={styles.refresh} title="refresh page" onClick={() => {window.location.reload();}}>{<MdRefresh />}</button>
          <Link to="newgroup" className={styles.newGroup}>
            <button title="create a new group">New Group</button>
          </Link>
        <Outlet />
      </div>
    </div>
  );
};

export default GroupsPage;
