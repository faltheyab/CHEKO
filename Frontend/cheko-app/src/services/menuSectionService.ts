import { httpClient } from './httpClient';
import { MenuSection, MenuSectionWithItemCount } from '@/src/types/menuSection';
import { logger } from '@/src/utils/logger';

export const menuSectionService = {
  // Get all menu sections
  getAll: () => httpClient.get<MenuSection[]>('/menu-sections'),
  
  // Get menu section by ID
  getById: (id: number) => {
    try {
      return httpClient.get<MenuSection>(`/menu-sections/${id}`);
    } catch (error) {
      logger.error(`Failed to fetch menu section with ID ${id}:`, error);
      throw error;
    }
  },
  
  // Get all menu sections with item counts
  getAllWithItemCounts: () => httpClient.get<MenuSectionWithItemCount[]>('/menu-sections/with-counts'),
  
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
  getMenuSectionsWithCounts: () => httpClient.get<MenuSectionWithItemCount[]>('/menu-sections/with-counts'),
};

export default menuSectionService;