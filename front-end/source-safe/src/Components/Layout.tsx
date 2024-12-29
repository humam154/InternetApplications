import React from "react";
import { Outlet } from "react-router-dom";
import SideNav from "./navigation/sideNav";
import styles from "./Layout.module.css";

const Layout = () => {
  return (
    <div className={styles.layout}>
      <SideNav />
      <div className={styles.content}>
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;
