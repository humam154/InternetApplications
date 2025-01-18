import { useState } from "react";
import { MdArrowBack, MdArrowForward } from "react-icons/md";
import { Link } from "react-router-dom";
import styles from "./sideNav.module.css";

export interface TileProps {
  title: string;
  icon: React.ReactNode;
  path: string;
}

interface NavBarProps {
  items: Array<TileProps>;
}

const Tile = (
  { title, icon, path, open, isSelected, onSelect }: TileProps & 
  { open: boolean; isSelected: boolean; onSelect: (title: string) => void }
) => {
  return (
    <Link to={path} className={styles.link} onClick={() => onSelect(title)}>
      <div className={`${open ? styles.tile_container_open : styles.tile_container_close} ${isSelected ? styles.selected_tile : styles.unselected_tile}`}>
        <div className={styles.tile_icon}>{icon}</div>
        {open && <div className={styles.tile_title}>{title}</div>}
      </div>
    </Link>
  );
};

const NavBar = ({ items }: NavBarProps) => {
  const [open, setOpen] = useState<boolean>(true);
  const [selectedTile, setSelectedTile] = useState<string>("");

  const handleTileSelect = (title: string) => {
    setSelectedTile(title);
  };

  return (
    <div className={open ? styles.container_open : styles.container_close}>
      <button className={styles.arrow} onClick={() => setOpen(!open)}>
        {open ? <MdArrowBack title="close navigation bar" /> : <MdArrowForward title="open navigation bar" />}
      </button>
      {items.map((item) => (
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

export default NavBar;
