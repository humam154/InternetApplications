import { useState } from "react";
import { CgProfile } from "react-icons/cg";
import { MdGroups, MdSettings, MdArrowBack, MdArrowForward, MdInbox, MdOutbox } from "react-icons/md";
import { Link } from "react-router-dom";
import styles from "./sideNav.module.css";

interface TileProps {
  title: string;
  icon: React.ReactNode;
  path: string;
  open: boolean;
  isSelected: boolean;
  onSelect: (title: string) => void;
}

const Tile = ({ title, icon, path, open, isSelected, onSelect }: TileProps) => {
  return (
    <Link to={path} className={styles.link} onClick={() => onSelect(title)}>
      <div
        className={`${open ? styles.tile_container_open : styles.tile_container_close} ${
          isSelected ? styles.selected_tile : styles.unselected_tile
        }`}
      >
        <div className={styles.tile_icon}>{icon}</div>
        {open && <div className={styles.tile_title}>{title}</div>}
      </div>
    </Link>
  );
};

const SideNav = () => {
  const [open, setOpen] = useState<boolean>(true);
  const [selectedTile, setSelectedTile] = useState<string>("");

  const menuItems = [
    { icon: <CgProfile />, title: "Profile", path: "/home/profile" },
    { icon: <MdGroups />, title: "Groups", path: "/home/groups" },
    { icon: <MdInbox />, title: "Inbox", path: "/home/inbox" },
    { icon: <MdOutbox />, title: "Outbox", path: "/home/outbox" },
    { icon: <MdSettings />, title: "Settings", path: "/home/settings" },
  ];

  const handleTileSelect = (title: string) => {
    setSelectedTile(title);
  };

  return (
    <div className={open ? styles.container_open : styles.container_close}>
      <button className={styles.arrow} onClick={() => setOpen(!open)}>
        {open ? <MdArrowBack /> : <MdArrowForward />}
      </button>
      {menuItems.map((item) => (
        <Tile
          key={item.title}
          {...item}
          open={open}
          isSelected={selectedTile === item.title}
          onSelect={handleTileSelect}
        />
      ))}
    </div>
  );
};

export default SideNav;
