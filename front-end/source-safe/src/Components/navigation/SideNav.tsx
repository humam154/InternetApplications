import { CgProfile } from "react-icons/cg";
import { MdGroups, MdSettings, MdInbox, MdOutbox } from "react-icons/md";
import NavBar, { TileProps } from "./NavBar";

const SideNav = () => {
  const menuItems: Array<TileProps> = [
    { icon: <CgProfile />, title: "Profile", path: "/home/profile" },
    { icon: <MdGroups />, title: "Groups", path: "/home/groups" },
    { icon: <MdInbox />, title: "Inbox", path: "/home/inbox" },
    { icon: <MdOutbox />, title: "Outbox", path: "/home/outbox" },
    { icon: <MdSettings />, title: "Settings", path: "/home/settings" },
  ];

  return <NavBar items={menuItems} />;
};

export default SideNav;
