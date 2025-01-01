import axios from "axios";

const apiUrl = `${import.meta.env.VITE_API_URL}/files`;

export const getFiles = async (token: string, gid: any) => {
  try {
    const response = await axios.get(`${apiUrl}/groupfiles/${gid}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching files:", error);
    throw error;
  }
};
