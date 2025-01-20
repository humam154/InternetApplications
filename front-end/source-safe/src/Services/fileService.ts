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

export interface updateFileData {
  file: File,
  fileId: string
}

export const updateFile = async (token: string, data: updateFileData) => {
  try {
    const formData = new FormData();
    formData.append('file', data.file);
    const response = await axios.put(`${apiUrl}/update/${data.fileId}`, formData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error updating file:", error);
    throw error;
  }
};

export const downloadFile = async (token: string, fileId: number) => {
  try {
    const response = await axios.get(`${apiUrl}/download/${fileId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      responseType: 'blob',

      withCredentials: true
    });

    const contentDisposition = response.headers['content-disposition'];
    let filename = 'downloaded_file';

    if (contentDisposition && contentDisposition.includes('attachment; filename=')) {
      const matches = contentDisposition.match(/filename[^;=\n]*=((['"]).*?2|[^;\n]*)/);
      if (matches != null && matches[1]) {
        filename = matches[1].replace(/['"]/g, '');
      }
    }

    const url = window.URL.createObjectURL(new Blob([response.data]));

    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();

    link.parentNode?.removeChild(link);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error("Error fetching file:", error);
    throw error;
  }
};

export const downloadManyFiles = async (token: string, fileIds: Array<number>) => {
  try {
    const response = await axios.get(`${apiUrl}/downloadmany`, 
      {params: {fileIds: fileIds
    },
      paramsSerializer:{
        indexes: null
      },
      headers: {
        Authorization: `Bearer ${token}`,
      },
      responseType: 'blob',

      withCredentials: true
    });

    const contentDisposition = response.headers['content-disposition'];
    let filename = 'downloaded_file';

    if (contentDisposition && contentDisposition.includes('attachment; filename=')) {
      const matches = contentDisposition.match(/filename[^;=\n]*=((['"]).*?2|[^;\n]*)/);
      if (matches != null && matches[1]) {
        filename = matches[1].replace(/['"]/g, '');
      }
    }

    const url = window.URL.createObjectURL(new Blob([response.data]));

    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();

    link.parentNode?.removeChild(link);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error("Error fetching file:", error);
    throw error;
  }
};

export const acceptFile = async (token: string, fileId: number)  => {
  try {
    const response = await axios.put(`${apiUrl}/accept/${fileId}`, null, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error updating file:", error);
    throw error;
  }
};

export const rejectFile = async (token: string, fileId: number)  => {
  try {
    const response = await axios.delete(`${apiUrl}/reject/${fileId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error updating file:", error);
    throw error;
  }
};