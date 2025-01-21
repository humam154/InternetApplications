import axios from "axios";


const apiUrl = `${import.meta.env.VITE_API_URL}/logs`;

export const getLogs = async (token: string, page: number) => {
    try {
      const response = await axios.get(`${apiUrl}/`, 
        {
        params: {page},
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching logs:", error);
      throw error;
    }
  };
  