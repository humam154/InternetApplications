import { CgProfile } from "react-icons/cg";
import { MdGroups, MdSettings, MdInbox, MdOutbox } from "react-icons/md";
import NavBar, { TileProps } from "./NavBar";

const SideNav = () => {
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

  return <NavBar items={menuItems} />;
};

export default SideNav;
