import axios from "axios";

const apiUrl = `${import.meta.env.VITE_API_URL}/groups`;

export const getGroups = async (token: string) => {
  const response = await axios.get(`${apiUrl}/`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
};
