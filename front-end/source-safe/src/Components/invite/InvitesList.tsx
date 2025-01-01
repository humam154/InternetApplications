import React from "react";

import styles from './InvitesList.module.css';
import { InviteProps } from './InviteCard';

interface PropsType {
    items: InviteProps[];
    renderer: (item: InviteProps) => React.ReactNode;
}
  

const InvitesList = (props: PropsType) => {
  
    return (
        <ul className={styles.list}>
          {Array.from(props.items).map((item) => {
            return <li key={item.id}>{props.renderer(item)}</li>;
          })}
        </ul>
      );
  };

export default InvitesList;