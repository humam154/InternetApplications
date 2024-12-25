import { useState } from "react";
import { CgProfile } from "react-icons/cg"
import { MdGroup, MdSettings, MdArrowBack, MdArrowForward } from "react-icons/md";
import styles from './sideNav.module.css';

interface TileProps {
    title: string;
    icon: React.ReactNode;
}

const Tile = ({ title, icon, open }: TileProps & { open: boolean }) => {
    return (
        <div className={open ? styles.tile_container_open : styles.tile_container_close}>
            <div className={styles.tile_icon}>{icon}</div>
            {open && <div className={styles.tile_title}>{title}</div>}
        </div>
    );
}

const SideNav = () => {
    const [open, setOpen] = useState<boolean>(true);

    const profile = { icon: <CgProfile />, title: "Profile" };
    const group = { icon: <MdGroup />, title: "Groups" };
    const settings = { icon: <MdSettings />, title: "Settings" };

    return (
        <div className={open ? styles.container_open : styles.container_close}>
            <button className={styles.arrow} onClick={() => setOpen(!open)}>{ open? <MdArrowBack /> : <MdArrowForward />}</button>
            <Tile {...profile} open={open} />
            <Tile {...group} open={open} />
            <Tile {...settings} open={open} />
        </div>
    );
}

export default SideNav;
