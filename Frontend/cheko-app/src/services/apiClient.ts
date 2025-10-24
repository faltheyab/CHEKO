import axios, { AxiosInstance } from 'axios';
import { getConfig, loadConfig } from '@/src/config/api.config';

let apiClient: AxiosInstance;

export async function initializeApiClient(): Promise<AxiosInstance> {
  
  const config = await loadConfig();
  apiClient = axios.create({
    baseURL: config.apiBaseUrl,
    headers: {
      'Content-Type': 'application/json',
    },
    timeout: config.timeout,
  });
  
  // request interceptor
  apiClient.interceptors.request.use(
    (config) => {
      // add more stuff here if needed
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );
  
  //  response interceptor
  apiClient.interceptors.response.use(
    (response) => {
      return response;
    },
    (error) => {
      // Handle common errors
      if (error.response) {
        console.error('API Error:', error.response.status, error.response.data);
      } else if (error.request) {
        console.error('No response received:', error.request);
      } else {
        console.error('Request error:', error.message);
      }
      
      return Promise.reject(error);
    }
  );
  
  return apiClient;
}


export async function getApiClient(): Promise<AxiosInstance> {
  if (!apiClient) {
    return await initializeApiClient();
  }
  return apiClient;
}

export default {
  initializeApiClient,
  getApiClient,
};
