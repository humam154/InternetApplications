import { useLocation, useParams } from "react-router-dom";
import { invite } from "../../Services/invitationService";
import { useState } from "react";
import { removeMember } from "../../Services/groupService";

interface UserProps {
    id: number;
    first_name: string;
    last_name: string;
    email: string;
}

const UserCard = (props: UserProps) => {
    const { id, first_name, last_name, email } = props;

    const location = useLocation();
    const state = location.state;
    const { gid } = state;
    const { isMember } = state;
    const { is_owner } = state;
    const { group_name } = state;

    const handleInvite = async () => {
        const token = localStorage.getItem("token");
          
        if (!token) {
          //TODO deal with this case, maybe redirect to login page
          alert("Your session has ended, please log in again!");
          return;
        }
    
        try {
          await invite(token, parseInt(gid), id);
          alert("User invited successfully!");
        } catch (error) {
          console.error("Error uploading file:", error);
          alert("An error occurred while inviting user");
        }
    };

    const handleRemoveMember = async () => {
      const token = localStorage.getItem("token");
        
      if (!token) {
        //TODO deal with this case, maybe redirect to login page
        alert("Your session has ended, please log in again!");
        return;
      }
  
      try {
        await removeMember(token, parseInt(gid), id);
        alert("User removed successfully!");
      } catch (error) {
        console.error("Error uploading file:", error);
        alert("An error occurred while removing member");
      }
  };
    return (
        <div>
          <p>
            {first_name} {last_name}
            <br/>
            {email}
          </p>
          {!isMember && gid && <button onClick={handleInvite} title={`invite user to group:${group_name}`}>Invite</button>}
          {isMember && is_owner && <button onClick={handleRemoveMember} title="remove member from group">Remove</button>}
        </div>
    );

};

export type {UserProps};
export default UserCard;