import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { BiRightArrow } from 'react-icons/bi';

import Layout from "./Components/Layout";
import Login from "./Components/auth/Login";
import Signup from "./Components/auth/Signup";
import ConfirmCode from "./Components/auth/ConfirmCode";
import GroupsPage from "./Components/group/GroupsPage";
import NewGroup from "./Components/group/NewGroup";
import GroupPage from "./Components/group/GroupPage";
import styles from './App.module.css';
import InvitesPage from "./Components/invite/InvitesPage";
import UsersComponent from "./Components/user/UsersComponent";
import Profile from "./Components/user/Profile";
import GroupFiles from "./Components/group/GroupFiles";

function Enter() {
  return (
    <div className={styles.home}>
      <Link to="/login" className={styles.link}>
        <div className={styles.enter} title="enter">
          Enter <BiRightArrow className={styles.arrow}/>
        </div>
      </Link>
    </div>
  );
}

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/confirmcode" element={<ConfirmCode />} />
        <Route path="/" element={<Enter />} />
        
        <Route path="/home" element={<Layout />}>
            <Route path="groups" element={<GroupsPage />} >
            <Route path="newgroup" element={<NewGroup />} />
            <Route path="searchuser" element={<UsersComponent />} />
        </Route>
          <Route path="groups/:gid" element={<GroupPage />}>
            <Route path="members" element={<UsersComponent />}/>
            <Route path="files" element={<GroupFiles />}/>
          </Route>
          <Route path="profile" element={<h1><Profile /></h1>} />
          <Route path="inbox" element={<InvitesPage />} />
          <Route path="outbox" element={<InvitesPage />} />
          <Route path="settings" element={<Login />} />
        </Route>
      </Routes>
    </Router>
  );
};

export default App;
