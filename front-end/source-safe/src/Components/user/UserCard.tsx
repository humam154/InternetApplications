import { useLocation, useParams } from "react-router-dom";
import { invite } from "../../Services/invitationService";
import { useState } from "react";

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

    const handleInvite = async () => {
        const token = localStorage.getItem("token");
          
            if (!token) {
              //TODO deal with this case, maybe redirect to login page
              alert("Your session has ended, please log in again!");
              return;
            }
        
                try {
                  await invite(token, parseInt(gid), id);
                  alert("File updated successfully!");
                } catch (error) {
                  console.error("Error uploading file:", error);
                  alert("An error occurred while uploading the file.");
                }
    };

    return (
        <div>
          <p>
            {first_name} {last_name}
            <br/>
            {email}
          </p>
          {!isMember && <button onClick={handleInvite}>Invite</button>}
        </div>
    );

};

export type {UserProps};
export default UserCard;