import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { BiRightArrow } from 'react-icons/bi';

import Layout from "./Components/Layout";
import Login from "./Components/auth/Login";
import Signup from "./Components/auth/Signup";
import ConfirmCode from "./Components/auth/ConfirmCode";
import GroupsPage from "./Components/group/GroupsPage";
import GroupPage from "./Components/group/GroupPage";
import styles from './App.module.css';

function Enter() {
  return (
    <div className={styles.home}>
      <Link to="/login" className={styles.link}>
        <div className={styles.enter}>
          Enter <BiRightArrow className={styles.arrow} />
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
          <Route path="groups" element={<GroupsPage />}>
            <Route path=":gid" element={<GroupPage />} />
          </Route>
          <Route path="profile" element={<h1>Profile Page</h1>} />
          <Route path="inbox" element={<h1>inbox Page</h1>} />
          <Route path="outbox" element={<h1>outbox Page</h1>} />
          <Route path="settings" element={<Login />} />
        </Route>
      </Routes>
    </Router>
  );
};

export default App;
