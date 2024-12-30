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
