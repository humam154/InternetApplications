import axios from "axios";

const apiUrl = `${import.meta.env.VITE_API_URL}/invitations`;

export const getInbox = async (token: string) => {
  try {
    const response = await axios.get(`${apiUrl}/inbox`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching inbox:", error);
    throw error;
  }
};

export const getOutbox = async (token: string) => {
    try {
      const response = await axios.get(`${apiUrl}/outbox`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching inbox:", error);
      throw error;
    }
};

export const accept = async (token: string, id: number) => {
    try {
      const response = await axios.post(
        `${apiUrl}/accept/${id}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error("Error accepting invitation:", error);
      throw error;
    }
  };
  
export const reject = async (token: string, id: number) => {
  try {
    const response = await axios.post(
        `${apiUrl}/reject/${id}`, 
        {},    
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
    return response.data;
  } catch (error) {
    console.error("Error rejecting invitation:", error);
    throw error;
  }
};
  