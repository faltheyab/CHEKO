// Menu Item interfaces

export interface MenuItem {
  id: number;
  sectionId: number;
  sectionName: string;
  branchId: number;
  branchName: string;
  name: string;
  description: string;
  price: number;
  calories: number;
  imageUrl?: string;
  isAvailable: boolean;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; 
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string;
}
