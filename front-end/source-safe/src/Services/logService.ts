import axios from "axios";
import { LogsFilter } from "../Components/log/LogsPage";


const apiUrl = `${import.meta.env.VITE_API_URL}/logs`;

export const getLogs = async (token: string, page: number, filter: LogsFilter) => {
    try {
      const response = await axios.get(`${apiUrl}/${filter}`, 
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
  