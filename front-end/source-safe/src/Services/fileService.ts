import axios from "axios";
import { Filter } from "../Components/group/GroupPage";

const apiUrl = `${import.meta.env.VITE_API_URL}/files`;

export const getFiles = async (token: string, gid: any, filter: Filter) => {
  try {
    const response = await axios.get(`${apiUrl}/groupfiles/${gid}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      params: {
        filter: filter
      }
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching files:", error);
    throw error;
  }
};

export interface uploadFileData {
  file: File,
  groupId: string
}

export const uploadFile = async (token: string, data: uploadFileData) => {
  try {
    const formData = new FormData();
    formData.append('file', data.file);
    formData.append('groupId', data.groupId);

    const response = await axios.post(`${apiUrl}/upload`, formData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error uploading file:", error);
    throw error;
  }
};
