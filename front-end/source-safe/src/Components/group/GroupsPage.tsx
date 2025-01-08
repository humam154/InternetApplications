import React, { useEffect, useState } from "react";

import GroupsList from "./GroupsList";
import { getGroups } from "../../Services/groupService";
import GroupCard, { GroupProps } from "./GroupCard";
import { Link, Outlet } from "react-router-dom";

const GroupsPage = () => {
  const [groups, setGroups] = useState<GroupProps[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      setError("User is not authenticated");
      setLoading(false);
      return;
    }
    getGroups(token)
      .then((data) => {
        setGroups(data.data);
        setLoading(false);
      })
      .catch((err) => {
        setError("Failed to fetch groups");
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div>
      <Link to="newgroup">
        <button>New Group</button>
      </Link>
      <GroupsList
      items={groups}
      renderer={(group) => <GroupCard key={group.gid} {...group} />}
      />
      <Outlet />
    </div>
  );
};

export default GroupsPage;
