import React from "react";

import styles from './FilesList.module.css';
import FileCard, { FileProps } from './FileCard';

interface PropsType {
    items: FileProps[];
    renderer: (item: FileProps) => React.ReactNode;
}
  

const GroupsList = (props: PropsType) => {
  
    return (
        <ul className={styles.list}>
          {Array.from(props.items).map((item) => {
            return <li key={item.id}>{props.renderer(item)}</li>;
          })}
        </ul>
      );
  };

export default GroupsList;