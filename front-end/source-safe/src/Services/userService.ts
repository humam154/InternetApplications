import axios, { AxiosRequestConfig, AxiosPromise, AxiosResponse } from 'axios';

const apiUrl = `${import.meta.env.VITE_API_URL}/user`;

export const searchUserInGroup = async (token: string, query: string, isMember: boolean, groupId: number) => {
    try {
      const response = await axios.get(`${apiUrl}/search`, {
        params: {query, isMember, groupId},
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching users:", error);
      throw error;
    }
  };

  export const searchUser = async (token: string, query: string) => {
    try {
      const response = await axios.get(`${apiUrl}/allusers`, {
        params: {query},
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching users:", error);
      throw error;
    }
  };
  
export const getProfile = async (token: string) => {
  try {
    const response = await axios.get(`${apiUrl}/profile`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching profile:", error);
    throw error;
  }
};

export interface profileData {
  first_name: string,
  last_name: string,
  email:string
}

export const updateProfile = async (token: string, data: profileData) => {
  try {
    const response = await axios.put(`${apiUrl}/update`, data,{
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error updating profile:", error);
    throw error;
  }
};

export interface passwordData {
  currentPassword: string,
  newPassword: string,
  confirmPassword: string
}

export const changePassword = async (token: string, data: passwordData) => {
  try {
    const response = await axios.patch(`${apiUrl}/change`, data,{
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error changing password:", error);
    throw error;
  }
};

export const downloadReport = async (token: string, type: string) => {
  try {
    const response = await axios.get(`${apiUrl}/most-active-${type}`, {
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