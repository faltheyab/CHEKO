import { httpClient } from './httpClient';
import { MenuItem, PaginatedResponse, PaginationParams } from '@/src/types/menuItem';
import { logger } from '@/src/utils/logger';

export const menuItemService = {

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
    
    return httpClient.get<PaginatedResponse<MenuItem>>(`/menu-items/branch/1/available/paginated?${queryParams.toString()}`);
  },
  
  // Get paginated menu items with search by name
  getPaginatedWithFilters: async (
    page: number = 0,
    size: number = 10,
    sort: string = 'name,asc',
    searchQuery?: string
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
      
      return await httpClient.get<PaginatedResponse<MenuItem>>(
        `/menu-items/branch/1/available/paginated?${queryParams.toString()}`
      );
    } catch (error) {
      logger.error('Failed to fetch menu items:', error);
      return {
        content: [],
        totalPages: 1,
        totalElements: 0,
        size: size,
        number: page,
        sort: sort
      };
    }
  },
  
  // Get paginated menu items with search by name and section ID
  getPaginatedWithFiltersAndSection: async (
    page: number = 0,
    size: number = 10,
    sort: string = 'name,asc',
    searchQuery?: string,
    sectionId?: number | null,
    branchId: number = 1
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
      
      // If section ID is provided, use the branch and section-specific endpoint
      if (sectionId) {
        return await httpClient.get<PaginatedResponse<MenuItem>>(
          `/menu-items/branch/${branchId}/section/${sectionId}/available?${queryParams.toString()}`
        );
      } else {
        // Otherwise use the branch endpoint
        return await httpClient.get<PaginatedResponse<MenuItem>>(
          `/menu-items/branch/${branchId}/available/paginated?${queryParams.toString()}`
        );
      }
    } catch (error) {
      logger.error('Failed to fetch menu items:', error);
      return {
        content: [],
        totalPages: 1,
        totalElements: 0,
        size: size,
        number: page,
        sort: sort
      };
    }
  }
};

export default menuItemService;
