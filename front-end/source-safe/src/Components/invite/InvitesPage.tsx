import React, { useEffect, useState } from "react";
import InvitesList from "./InvitesList";
import { getInbox, getOutbox } from "../../Services/invitationService";
import InviteCard, { InviteProps } from "./InviteCard";
import { useLocation } from "react-router-dom";

const InvitesPage = () => {
  const [invites, setInvites] = useState<InviteProps[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [message, setMessage] = useState<string | null>(null);

  const location = useLocation();
  const isOutbox = location.pathname.includes("outbox");
  
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      setError("User is not authenticated");
      setLoading(false);
      return;
    }

    if(isOutbox) {
        getOutbox(token)
      .then((data) => {
        if (data.data && data.data.length > 0) {
          setInvites(data.data);
        } else {
          setMessage(data.message || "No invites available.");
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(`Failed to fetch invites: ${err}`);
        setLoading(false);
      });
    } else {
        getInbox(token)
      .then((data) => {
        if (data.data && data.data.length > 0) {
          setInvites(data.data);
        } else {
          setMessage(data.message || "No invites available.");
        }
        setLoading(false);
      })
      .catch((err) => {
        setError(`Failed to fetch invites: ${err}`);
        setLoading(false);
      });
    }
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div>
      {invites.length > 0 ? (
        <InvitesList
          items={invites}
          renderer={(invite) => <InviteCard key={invite.id} {...invite} />}
        />
      ) : (
        <p>{message}</p>
      )}
    </div>
  );
};

export default InvitesPage;
