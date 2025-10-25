import { httpClient } from './httpClient';
import { MenuSection, MenuSectionWithItemCount } from '@/src/types/menuSection';
import { logger } from '@/src/utils/logger';

export const menuSectionService = {
  
  // Get menu sections by branch ID
  getByBranchId: (branchId: number) => {
    try {
      return httpClient.get<MenuSection[]>(`/branches/${branchId}/menu-sections`);
    } catch (error) {
      logger.error(`Failed to fetch menu sections for branch ID ${branchId}:`, error);
      throw error;
    }
  },
  
  // Get menu sections with item counts by branch ID
  getWithItemCountsByBranchId: (branchId: number) => {
    try {
      return httpClient.get<MenuSectionWithItemCount[]>(`/branches/${branchId}/menu-sections/with-counts`);
    } catch (error) {
      logger.error(`Failed to fetch menu sections with counts for branch ID ${branchId}:`, error);
      throw error;
    }
  },
    
  // Get menu sections with item counts
  getMenuSectionsWithCounts: async () => {
    try {
      return await httpClient.get<MenuSectionWithItemCount[]>('/menu-sections/with-counts');
    } catch (error) {
      logger.error('Failed to fetch menu sections with counts:', error);
      return []; 
    }
  },
};

export default menuSectionService;