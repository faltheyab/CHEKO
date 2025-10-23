import { AxiosRequestConfig } from 'axios';
import { getApiClient as getAxiosClient } from './apiClient';
import { logger } from '@/src/utils/logger';


type LoadingManager = {
  startLoading: () => void;
  stopLoading: () => void;
};

// Default no-op loading manager (will be replaced when initialized)
let loadingManager: LoadingManager = {
  startLoading: () => {},
  stopLoading: () => {}
};

// Function to set the loading manager
export const setLoadingManager = (manager: LoadingManager): void => {
  loadingManager = manager;
};

// Initialize loading manager with the LoadingContext
// This should be called from a component that has access to the LoadingContext
export const initializeLoading = (startLoading: () => void, stopLoading: () => void): void => {
  setLoadingManager({ startLoading, stopLoading });
};

// Create and export the httpClient with all functionality
export const httpClient = {
  // Export getApiClient directly
  getApiClient: getAxiosClient,
  
  // HTTP methods with loading by default and logging
  get: async <T>(
    url: string, 
    config?: AxiosRequestConfig, 
    disableLoading: boolean = false
  ): Promise<T> => {
    const client = getAxiosClient();
    logger.info(`GET request to ${url}`);
    
    try {
      if (!disableLoading) {
        loadingManager.startLoading();
      }
      
      const response = await client.get<T>(url, config);
      logger.debug(`GET response from ${url}`, response.data as unknown);
      return response.data;
    } catch (error) {
      logger.error(`GET request to ${url} failed`, error);
      throw error;
    } finally {
      if (!disableLoading) {
        loadingManager.stopLoading();
      }
    }
  },
  
  post: async <T, D = Record<string, unknown>>(
    url: string, 
    data?: D, 
    config?: AxiosRequestConfig, 
    disableLoading: boolean = false
  ): Promise<T> => {
    const client = getAxiosClient();
    logger.info(`POST request to ${url}`, data as unknown);
    
    try {
      if (!disableLoading) {
        loadingManager.startLoading();
      }
      
      const response = await client.post<T>(url, data, config);
      logger.debug(`POST response from ${url}`, response.data as unknown);
      return response.data;
    } catch (error) {
      logger.error(`POST request to ${url} failed`, error);
      throw error;
    } finally {
      if (!disableLoading) {
        loadingManager.stopLoading();
      }
    }
  },
  
  put: async <T, D = Record<string, unknown>>(
    url: string, 
    data?: D, 
    config?: AxiosRequestConfig, 
    disableLoading: boolean = false
  ): Promise<T> => {
    const client = getAxiosClient();
    logger.info(`PUT request to ${url}`, data as unknown);
    
    try {
      if (!disableLoading) {
        loadingManager.startLoading();
      }
      
      const response = await client.put<T>(url, data, config);
      logger.debug(`PUT response from ${url}`, response.data as unknown);
      return response.data;
    } catch (error) {
      logger.error(`PUT request to ${url} failed`, error);
      throw error;
    } finally {
      if (!disableLoading) {
        loadingManager.stopLoading();
      }
    }
  },
  
  delete: async <T>(
    url: string, 
    config?: AxiosRequestConfig, 
    disableLoading: boolean = false
  ): Promise<T> => {
    const client = getAxiosClient();
    logger.info(`DELETE request to ${url}`);
    
    try {
      if (!disableLoading) {
        loadingManager.startLoading();
      }
      
      const response = await client.delete<T>(url, config);
      logger.debug(`DELETE response from ${url}`, response.data as unknown);
      return response.data;
    } catch (error) {
      logger.error(`DELETE request to ${url} failed`, error);
      throw error;
    } finally {
      if (!disableLoading) {
        loadingManager.stopLoading();
      }
    }
  }
};

export default httpClient;