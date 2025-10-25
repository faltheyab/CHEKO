import { httpClient } from './httpClient';
import { MenuSection, MenuSectionWithItemCount } from '@/src/types/menuSection';
import { logger } from '@/src/utils/logger';

export const menuSectionService = {
  

  // Get menu sections with item counts by branch ID
  getWithItemCountsByBranchId: (branchId: number) => {
    try {
      return httpClient.get<MenuSectionWithItemCount[]>(`/menu-sections/branch/${branchId}/with-counts`);
    } catch (error) {
      logger.error(`Failed to fetch menu sections with counts for branch ID ${branchId}:`, error);
      throw error;
    }
  },
    
};

export default menuSectionService;