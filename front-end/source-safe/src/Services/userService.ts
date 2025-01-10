import axios, { AxiosRequestConfig, AxiosPromise, AxiosResponse } from 'axios';

const apiUrl = `${import.meta.env.VITE_API_URL}/user`;

export const searchUser = async (token: string, query: string) => {
    try {
      const response = await axios.get(`${apiUrl}/search`, {
        params: {query},
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
  