import { CgProfile } from "react-icons/cg";
import { MdGroups, MdSettings, MdInbox, MdOutbox, MdFilePresent } from "react-icons/md";
import { CgUser } from "react-icons/cg";
import NavBar, { TileProps } from "./NavBar";

const SideNav = () => {

  const isAdmin: boolean = JSON.parse(localStorage.getItem("isAdmin")!,  (k, v) => v === "true" ? true : v === "false" ? false : v);
  
  const menuItems: Array<TileProps> = [
    {
      icon: <CgProfile />, title: "Profile", path: "/home/profile",
      state: {}
    },
    {
      icon: <MdGroups />, title: "Groups", path: "/home/groups",
      state: {}
    },
    {
      icon: <MdInbox />, title: "Inbox", path: "/home/inbox",
      state: {}
    },
    {
      icon: <MdOutbox />, title: "Outbox", path: "/home/outbox",
      state: {}
    },
    {
      icon: <MdSettings />, title: "Settings", path: "/home/settings",
      state: {}
    },
  ];

  if (isAdmin) {
    menuItems.splice(4, 0, 
    
    {
      icon: <MdFilePresent />,
      title: "Logs",
      path: "/home/logs",
      state: {},
    },
    {
      icon: <CgUser />,
      title: "Users",
      path: "/home/users",
      state: {gid:null, isMember: false},
    },
  );
  }

  return <NavBar items={menuItems} />;
};

export default SideNav;
