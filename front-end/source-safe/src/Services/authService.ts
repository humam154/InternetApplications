import axios, { AxiosRequestConfig, AxiosPromise, AxiosResponse } from 'axios';

const apiUrl = `${import.meta.env.VITE_API_URL}/auth`;

export const loginUser = async (email: string, password: string) => {
    try {
        const response = await axios.post(`${apiUrl}/authenticate`, {
            email,
            password,
        });

        return response.data;
    } catch (error: any) {
        
        if (error.response) {
            throw new Error(error.response.data.message || 'Login failed. Please try again.');
        } else {
            throw new Error('Network error. Please try again later.');
        }
    }
};

export const registerUser = async (first_name: string, last_name: string, email: string, password: string) => {
    try {
        const response = await axios.post(`${apiUrl}/register`, {
            first_name,
            last_name,
            email,
            password,
        });

        return response.data;
    } catch (error: any) {
        
        if (error.response) {
            throw new Error(error.response.data.message || 'Sign Up failed. Please try again.');
        } else {
            throw new Error('Network error. Please try again later.');
        }
    }
};

export const confirmEmail = async (email: string, code: number) => {
    try {
        const response = await axios.post('/api/verify-code', { email ,code });
        console.log('Verification successful:', response.data);

      } catch (error: any) {
        console.error('Verification failed:', error.response?.data || error.message);
      }
}

export const resendCode =  async (email: string) => {
    try {
        const response = await axios.post('/api/resend-code', { email });

      } catch (error: any) {
        console.error('Verification failed:', error.response?.data || error.message);
      }
}