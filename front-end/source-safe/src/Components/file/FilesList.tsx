import React, { useState, useEffect } from "react";
import styles from './FilesList.module.css';
import FileCard, { FileProps } from './FileCard';

interface PropsType {
    items: FileProps[];
    renderer: (item: FileProps) => React.ReactNode;
    onCheckedChange: (checkedItems: number[]) => void; // New prop
}

const FilesList = (props: PropsType) => {
    const [checkedItems, setCheckedItems] = useState<{ [key: number]: boolean }>({});

    const toggleCheckbox = (id: number) => {
        setCheckedItems((prev) => ({
            ...prev,
            [id]: !prev[id],
        }));
    };

    // Effect to notify parent of checked items
    useEffect(() => {
        const selectedIds = Object.keys(checkedItems)
            .filter((key) => checkedItems[Number(key)])
            .map(Number);
        props.onCheckedChange(selectedIds); // Notify parent
    }, [checkedItems, props]);

    return (
        <ul className={styles.list}>
            {Array.from(props.items).map((item) => {
                return (
                    <li key={item.id} className={styles.listItem}>
                        <input
                            type="checkbox"
                            checked={!!checkedItems[item.id]}
                            onChange={() => toggleCheckbox(item.id)}
                            className={styles.checkbox}
                        />
                        {props.renderer(item)}
                    </li>
                );
            })}
        </ul>
    );
};

export default FilesList;
