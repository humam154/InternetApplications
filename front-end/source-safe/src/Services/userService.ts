import axios, { AxiosRequestConfig, AxiosPromise, AxiosResponse } from 'axios';

const apiUrl = `${import.meta.env.VITE_API_URL}/user`;

export const searchUser = async (token: string, query: string, isMember: boolean, groupId: number) => {
    try {
      const response = await axios.get(`${apiUrl}/search`, {
        params: {query, isMember, groupId},
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
  
export const getProfile = async (token: string) => {
  try {
    const response = await axios.get(`${apiUrl}/profile`, {
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
    console.error("Error fetching inbox:", error);
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
    console.error("Error fetching inbox:", error);
    throw error;
  }
};