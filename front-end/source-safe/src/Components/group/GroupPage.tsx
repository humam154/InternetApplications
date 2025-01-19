import React, { useState } from "react";
import { MdArrowBack, MdFileOpen, MdVerifiedUser, MdLock } from "react-icons/md";
import { Link, Outlet, useLocation, useParams } from "react-router-dom";

import styles from './GroupPage.module.css';
import NavBar, { TileProps } from "../navigation/NavBar";

export enum Filter {
  NONE = '',
  PENDING = 'pending',
  ACCEPTED = 'accepted',
  IN_USE = 'in_use',
  IN_USE_BY_ME = 'in_use_by_me'
}

const GroupPage = () => {
   const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const { gid } = useParams();
    const location = useLocation();
    const state = location.state;
    const { group_name } = state;

    const groupMenu: Array<TileProps> = [
        { icon: <MdFileOpen />, title: "Files", path:`/home/groups/${gid}/files`, state: {} },
        { icon: <MdVerifiedUser />, title: "Members", path:`/home/groups/${gid}/members`, state: { gid, isMember:true }},
    ];

    return (
      <div className={styles.container}>
          <div className={styles.banner}>
              <Link to="/home/groups" title="back to groups page">
                  <MdArrowBack />
              </Link>
              <h2>{group_name}</h2>

              <div className={styles.navbar}>
                  <NavBar items={groupMenu} />
              </div>
          </div>
          <div className={styles.content}>
              <Outlet />
          </div>
      </div>
    );
}

export default GroupPage;
