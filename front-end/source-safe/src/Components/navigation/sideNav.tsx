import { useState } from "react";
import { CgProfile } from "react-icons/cg";
import { MdGroup, MdSettings, MdArrowBack, MdArrowForward } from "react-icons/md";
import { Link } from "react-router-dom";
import styles from "./sideNav.module.css";

interface TileProps {
  title: string;
  icon: React.ReactNode;
  path: string;
}

const Tile = ({ title, icon, path, open }: TileProps & { open: boolean }) => {
  return (
    <Link to={path} className={styles.link}>
      <div className={open ? styles.tile_container_open : styles.tile_container_close}>
        <div className={styles.tile_icon}>{icon}</div>
        {open && <div className={styles.tile_title}>{title}</div>}
      </div>
    </Link>
  );
};

const SideNav = () => {
  const [open, setOpen] = useState<boolean>(true);

  const menuItems = [
    { icon: <CgProfile />, title: "Profile", path: "/profile" },
    { icon: <MdGroup />, title: "Groups", path: "/groups" },
    { icon: <MdSettings />, title: "Settings", path: "/settings" },
  ];

  return (
    <div className={open ? styles.container_open : styles.container_close}>
      <button className={styles.arrow} onClick={() => setOpen(!open)}>
        {open ? <MdArrowBack /> : <MdArrowForward />}
      </button>
      {menuItems.map((item) => (
        <Tile key={item.title} {...item} open={open} />
      ))}
    </div>
  );
};

export default SideNav;
