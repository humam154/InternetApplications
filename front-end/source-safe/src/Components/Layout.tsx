import React from "react";
import { Outlet } from "react-router-dom";
import SideNav from "./navigation/SideNav";
import styles from "./Layout.module.css";

const Layout = () => {
  return (
    <div className={styles.layout}>
      <div className={styles.SideNav}>
        <SideNav />
      </div>
      <div className={styles.content}>
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;
