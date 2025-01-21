import React, { useState } from "react";
import { MdArrowBack, MdFileOpen, MdVerifiedUser } from "react-icons/md";
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
  const { gid } = useParams();
  const location = useLocation();
  const state = location.state;
  const { group_name } = state;
  const { is_owner } = state;

  const groupMenu: Array<TileProps> = [
    { icon: <MdFileOpen />, title: "Files", path: `/home/groups/${gid}/files`, state: { group_name, is_owner } },
    { icon: <MdVerifiedUser />, title: "Members", path: `/home/groups/${gid}/members`, state: { gid, isMember: true, group_name, is_owner } },
  ];

  return (
    <div className={styles.container}>
      <div className={styles.banner}>
        <Link className={styles.arrow} to="/home/groups" title="Back to groups page">
          <MdArrowBack />
        </Link>
        <h2>{group_name}</h2>
      </div>
      <div className={styles.groupcontent}>

      <div className={styles.navbar}>
          <NavBar items={groupMenu} />
      </div>
      <div className={styles.content}>
        <Outlet />
      </div>
      </div>
    </div>
  );
};

export default GroupPage;
