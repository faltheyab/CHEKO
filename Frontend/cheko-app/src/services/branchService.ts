import { httpClient } from './httpClient';
import { Branch } from '@/src/types/branch';
import { logger } from '@/src/utils/logger';

export const branchService = {
  // Get all branches
  getAll: () => httpClient.get<Branch[]>('/branches'),
  
  // Get branch by ID
  getById: (id: number) => {
    try {
      return httpClient.get<Branch>(`/branches/${id}`);
    } catch (error) {
      logger.error(`Failed to fetch branch with ID ${id}:`, error);
      throw error;
    }
  },
};

export default branchService;