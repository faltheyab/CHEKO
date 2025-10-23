import { httpClient } from './httpClient';
import { MenuItem, PaginatedResponse, PaginationParams } from '@/src/types/menuItem';
import { logger } from '@/src/utils/logger';

export const menuItemService = {
  // Get all menu items
  getAll: () => httpClient.get<MenuItem[]>('/menu-items'),
  
  // Get paginated menu items
  getPaginated: (params: PaginationParams) => {
    const { page = 0, size = 10, sort } = params;
    const queryParams = new URLSearchParams();
    queryParams.append('page', page.toString());
    queryParams.append('size', size.toString());
    
    if (sort) {
      const [sortField, direction] = sort.split(',');
      queryParams.append('sort', sortField);
      queryParams.append('direction', direction || 'asc');
    }
    
    return httpClient.get<PaginatedResponse<MenuItem>>(`/menu-items/branch/1/paginated?${queryParams.toString()}`);
  },
  
  // Get menu item by ID
  getById: (id: number) => {
    try {
      return httpClient.get<MenuItem>(`/menu-items/${id}`);
    } catch (error) {
      logger.error(`Failed to fetch menu item with ID ${id}:`, error);
      throw error;
    }
  },
  
  // Get menu items by section ID
  getBySectionId: (sectionId: number) => {
    try {
      return httpClient.get<MenuItem[]>(`/menu-sections/${sectionId}/menu-items`);
    } catch (error) {
      logger.error(`Failed to fetch menu items for section ID ${sectionId}:`, error);
      throw error;
    }
  },
  
  // Get paginated menu items by section ID
  getPaginatedBySectionId: (sectionId: number, params: PaginationParams) => {
    try {
      const { page = 0, size = 10, sort } = params;
      const queryParams = new URLSearchParams();
      queryParams.append('page', page.toString());
      queryParams.append('size', size.toString());
      
      if (sort) {
        const [sortField, direction] = sort.split(',');
        queryParams.append('sort', sortField);
        queryParams.append('direction', direction || 'asc');
      }
      
      return httpClient.get<PaginatedResponse<MenuItem>>(
        `/menu-sections/${sectionId}/menu-items?${queryParams.toString()}`
      );
    } catch (error) {
      logger.error(`Failed to fetch paginated menu items for section ID ${sectionId}:`, error);
      throw error;
    }
  },
  
  // Get menu items by branch ID
  getByBranchId: (branchId: number) => {
    try {
      return httpClient.get<MenuItem[]>(`/branches/${branchId}/menu-items`);
    } catch (error) {
      logger.error(`Failed to fetch menu items for branch ID ${branchId}:`, error);
      throw error;
    }
  },
  
  // Get paginated menu items by branch ID
  getPaginatedByBranchId: (branchId: number, params: PaginationParams) => {
    try {
      const { page = 0, size = 10, sort } = params;
      const queryParams = new URLSearchParams();
      queryParams.append('page', page.toString());
      queryParams.append('size', size.toString());
      
      if (sort) {
        const [sortField, direction] = sort.split(',');
        queryParams.append('sort', sortField);
        queryParams.append('direction', direction || 'asc');
      }
      
      return httpClient.get<PaginatedResponse<MenuItem>>(
        `/branches/${branchId}/menu-items?${queryParams.toString()}`
      );
    } catch (error) {
      logger.error(`Failed to fetch paginated menu items for branch ID ${branchId}:`, error);
      throw error;
    }
  },
  
  // Get second highest calorie meal per category
  getSecondHighestCalorieMealPerCategory: () =>
    httpClient.get<Record<string, MenuItem>>('/menu-items/second-highest-calorie-per-category'),
    
  // Get paginated menu items by section ID with custom query parameters
  getPaginatedBySectionIdWithParams: (sectionId: number, page: number = 0, size: number = 10, sort: string = 'name,asc') => {
    try {
      const queryParams = new URLSearchParams();
      queryParams.append('page', page.toString());
      queryParams.append('size', size.toString());
      
      if (sort) {
        const [sortField, direction] = sort.split(',');
        queryParams.append('sort', sortField);
        queryParams.append('direction', direction || 'asc');
      }
      
      return httpClient.get<PaginatedResponse<MenuItem>>(
        `/menu-sections/${sectionId}/menu-items?${queryParams.toString()}`
      );
    } catch (error) {
      logger.error(`Failed to fetch paginated menu items for section ID ${sectionId}:`, error);
      throw error;
    }
  },
  
  // Get paginated menu items with search and filters
  getPaginatedWithFilters: (
    page: number = 0,
    size: number = 10,
    sort: string = 'name,asc',
    searchQuery?: string,
    minPrice?: number,
    maxPrice?: number,
    minCalories?: number,
    maxCalories?: number,
    vegetarian?: boolean,
    vegan?: boolean,
    glutenFree?: boolean
  ) => {
    try {
      const queryParams = new URLSearchParams();
      queryParams.append('page', page.toString());
      queryParams.append('size', size.toString());
      

      if (sort) {
        const [sortField, direction] = sort.split(',');
        queryParams.append('sort', sortField);
        queryParams.append('direction', direction || 'asc');
      }
      
      if (searchQuery) queryParams.append('search', searchQuery);
      
      // Add filter parameters
      if (minPrice !== undefined) queryParams.append('minPrice', minPrice.toString());
      if (maxPrice !== undefined) queryParams.append('maxPrice', maxPrice.toString());
      if (minCalories !== undefined) queryParams.append('minCalories', minCalories.toString());
      if (maxCalories !== undefined) queryParams.append('maxCalories', maxCalories.toString());
      if (vegetarian) queryParams.append('vegetarian', 'true');
      if (vegan) queryParams.append('vegan', 'true');
      if (glutenFree) queryParams.append('glutenFree', 'true');
      
      return httpClient.get<PaginatedResponse<MenuItem>>(
        `/menu-items/branch/1/paginated?${queryParams.toString()}`
      );
    } catch (error) {
      logger.error('Failed to fetch filtered menu items:', error);
      throw error;
    }
  }
};

export default menuItemService;
