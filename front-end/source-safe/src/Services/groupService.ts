import axios from "axios";

const apiUrl = `${import.meta.env.VITE_API_URL}/groups`;

export const getGroups = async (token: string) => {
  try {
    const response = await axios.get(`${apiUrl}/`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching groups:", error);
    throw error;
  }
};

export interface CreateGroupData {
  name: string;
  description: string;
}

export const createGroup = async (token: string, data: CreateGroupData) => {
  try {
    const response = await axios.post(`${apiUrl}/create`, 
    data,  
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching groups:", error);
    throw error;
  }
};


export const deleteGroup = async (token: string, gid: number)  => {
  try {
    const response = await axios.delete(`${apiUrl}/delete/${gid}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error deleting group:", error);
    throw error;
  }
};
