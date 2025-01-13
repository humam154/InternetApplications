
import { UserProps } from './UserCard';

interface PropsType {
    items: UserProps[];
    renderer: (item: UserProps) => React.ReactNode;
}

const UsersList = (props: PropsType) => {

    return (
        <ul>
          {Array.from(props.items).map((item) => {
            return <li key={item.id}>{props.renderer(item)}</li>;
          })}
        </ul>
      );
}

export default UsersList;