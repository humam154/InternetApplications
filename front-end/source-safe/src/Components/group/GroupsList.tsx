import React from "react";

import styles from './GroupList.module.css'
import GroupCard, {GroupProps} from './GroupCard'

interface PropsType {
    items: GroupProps[];
    renderer: (item: GroupProps) => React.ReactNode;
}
  

const GroupsList = (props: PropsType) => {
  
    return (
        <ul className={styles.list}>
          {Array.from(props.items).map((item) => {
            return <li key={item.gid}>{props.renderer(item)}</li>;
          })}
        </ul>
      );
  };

export default GroupsList;