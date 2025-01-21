import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Components/auth/Login";
import Signup from "./Components/auth/Signup";
import ConfirmCode from "./Components/auth/ConfirmCode";
import PasswordReset from "./Components/auth/PasswordReset";
import Layout from "./Components/Layout";
import GroupsPage from "./Components/group/GroupsPage";
import NewGroup from "./Components/group/NewGroup";
import GroupPage from "./Components/group/GroupPage";
import InvitesPage from "./Components/invite/InvitesPage";
import UsersComponent from "./Components/user/UsersComponent";
import Profile from "./Components/user/Profile";
import GroupFiles from "./Components/group/GroupFiles";
import Home from "./Components/Home";
import Settings from "./Components/user/Settings";
import LogsPage from "./Components/log/LogsPage";

const App = () => {
    return (
        <Router>
            <Routes>
                {/* Auth Routes */}
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/confirmcode" element={<ConfirmCode />} />
                <Route path="/password-reset" element={<PasswordReset />} />

                {/* Home Page */}
                <Route path="/" element={<Home />} />

                {/* Protected Routes */}
                <Route path="/home" element={<Layout />}>
                    <Route path="groups" element={<GroupsPage />}>
                        <Route path="newgroup" element={<NewGroup />} />
                        <Route path="searchuser" element={<UsersComponent />} />
                    </Route>
                    <Route path="groups/:gid" element={<GroupPage />}>
                        <Route path="members" element={<UsersComponent />} />
                        <Route path="files" element={<GroupFiles />} />
                    </Route>
                    <Route path="profile" element={<Profile />} />
                    <Route path="inbox" element={<InvitesPage />} />
                    <Route path="outbox" element={<InvitesPage />} />
                    <Route path="users" element={<UsersComponent />} />
                    <Route path="logs" element={<LogsPage />} />
                    <Route path="settings" element={<Settings />} />
                </Route>
            </Routes>
        </Router>
    );
};

export default App;
