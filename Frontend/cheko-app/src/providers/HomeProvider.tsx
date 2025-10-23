'use client';

import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { MenuItem, PaginatedResponse, PaginationParams } from '@/src/types/menuItem';
import { MenuSection, MenuSectionWithItemCount } from '@/src/types/menuSection';
import { menuItemService } from '@/src/services/menuItemService';
import { menuSectionService } from '@/src/services/menuSectionService';
import { initializeApiClient, getApiClient } from '@/src/services/apiClient';
import { LoadingProvider, useLoading } from '@/src/context/LoadingContext';

// Define filter types
interface FilterRange {
  min: number;
  max: number;
}

interface DietaryPreferences {
  vegetarian: boolean;
  vegan: boolean;
  glutenFree: boolean;
}

interface Filters {
  priceRange: FilterRange;
  caloriesRange: FilterRange;
  dietary: DietaryPreferences;
}

// Cart item type
interface CartItem {
  item: MenuItem;
  quantity: number;
}

// Define the context type
interface HomeContextType {
  menuItems: MenuItem[];
  categories: MenuSectionWithItemCount[];
  selectedCategory: number | null;
  searchQuery: string;
  filters: Filters;
  pagination: PaginationParams;
  totalPages: number;
  selectedItem: MenuItem | null;
  isModalOpen: boolean;
  cart: CartItem[];
  cartItemCount: number;
  isLoading: boolean;
  handleSearch: (query: string, newFilters: Filters) => void;
  handleCategorySelect: (categoryId: number | null) => void;
  handlePageChange: (newPage: number) => void;
  handleItemClick: (item: MenuItem) => void;
  handleAddToCart: (item: MenuItem, quantity: number) => void;
  handleCartClick: () => void;
  setIsModalOpen: (isOpen: boolean) => void;
  getCategoryName: () => string;
}

// Create the context
const HomeContext = createContext<HomeContextType | undefined>(undefined);

// Provider props
interface HomeProviderProps {
  children: ReactNode;
}

export const HomeProvider: React.FC<HomeProviderProps> = ({ children }) => {
  // Get loading state
  const { isLoading, startLoading, stopLoading } = useLoading();
  
  // State for menu items and categories
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [categories, setCategories] = useState<MenuSectionWithItemCount[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [filters, setFilters] = useState<Filters>({
    priceRange: { min: 0, max: 100 },
    caloriesRange: { min: 0, max: 1000 },
    dietary: {
      vegetarian: false,
      vegan: false,
      glutenFree: false,
    }
  });
  
  // Pagination with default values to avoid undefined
  const [pagination, setPagination] = useState<PaginationParams>({
    page: 0,
    size: 10,
    sort: 'name,asc'
  });
  const [totalPages, setTotalPages] = useState(1);
  
  // Modal state
  const [selectedItem, setSelectedItem] = useState<MenuItem | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  
  // Cart state
  const [cart, setCart] = useState<CartItem[]>([]);
  
  // Calculate total items in cart
  const cartItemCount = cart.reduce((total, item) => total + item.quantity, 0);
  
  // Handle cart button click
  const handleCartClick = () => {
    alert("Faisal has not implemented this yet");
  };
  
  // Fetch categories
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        // Initialize API client if needed
        try {
          getApiClient(); // Check if already initialized
        } catch (e) {
          // If not initialized, do it now
          console.log('Initializing API client from home page');
          await initializeApiClient();
        }
        
        startLoading();
        try {
          const data = await menuSectionService.getMenuSectionsWithCounts();
          setCategories(data || []);
        } finally {
          stopLoading();
        }
      } catch (error) {
        console.error('Error fetching categories:', error);
        // Set default empty array for categories in case of error
        setCategories([]);
      }
    };
    
    fetchCategories();
  }, [startLoading, stopLoading]);
  
  // Fetch menu items when selected category, search query, filters, or pagination changes
  useEffect(() => {
    const fetchMenuItems = async () => {
      try {
        // Ensure API client is initialized
        try {
          getApiClient(); // Check if already initialized
        } catch (e) {
          // If not initialized, do it now
          console.log('Initializing API client from home page');
          await initializeApiClient();
        }
        
        let data;
        
        startLoading();
        try {
          if (selectedCategory) {
            // Fetch items for selected category
            data = await menuItemService.getPaginatedBySectionIdWithParams(
              selectedCategory,
              pagination.page || 0,
              pagination.size || 10,
              pagination.sort || 'name,asc'
            );
          } else {
            // Fetch all items with search and filters
            data = await menuItemService.getPaginatedWithFilters(
              pagination.page || 0,
              pagination.size || 10,
              pagination.sort || 'name,asc',
              searchQuery,
              filters.priceRange.min > 0 ? filters.priceRange.min : undefined,
              filters.priceRange.max < 100 ? filters.priceRange.max : undefined,
              filters.caloriesRange.min > 0 ? filters.caloriesRange.min : undefined,
              filters.caloriesRange.max < 1000 ? filters.caloriesRange.max : undefined,
              filters.dietary.vegetarian,
              filters.dietary.vegan,
              filters.dietary.glutenFree
            );
          }
          
          setMenuItems(data?.content || []);
          setTotalPages(data?.totalPages || 1);
        } finally {
          stopLoading();
        }
      } catch (error) {
        console.error('Error fetching menu items:', error);
        // Set default values in case of error
        setMenuItems([]);
        setTotalPages(1);
      }
    };
    
    fetchMenuItems();
  }, [startLoading, stopLoading, selectedCategory, searchQuery, filters, pagination]);
  
  // Handle search
  const handleSearch = (query: string, newFilters: Filters) => {
    setSearchQuery(query);
    setFilters(newFilters);
    setPagination(prev => ({ ...prev, page: 0 })); // Reset to first page
  };
  
  // Handle category selection
  const handleCategorySelect = (categoryId: number | null) => {
    setSelectedCategory(categoryId);
    setPagination(prev => ({ ...prev, page: 0 })); // Reset to first page
  };
  
  // Handle pagination
  const handlePageChange = (newPage: number) => {
    setPagination(prev => ({ ...prev, page: newPage }));
  };
  
  // Handle item click to open modal
  const handleItemClick = (item: MenuItem) => {
    setSelectedItem(item);
    setIsModalOpen(true);
  };
  
  // Handle add to cart
  const handleAddToCart = (item: MenuItem, quantity: number) => {
    setCart(prevCart => {
      // Check if item already exists in cart
      const existingItemIndex = prevCart.findIndex(cartItem => cartItem.item.id === item.id);
      
      if (existingItemIndex >= 0) {
        // Update quantity if item exists
        const updatedCart = [...prevCart];
        updatedCart[existingItemIndex].quantity += quantity;
        return updatedCart;
      } else {
        // Add new item to cart
        return [...prevCart, { item, quantity }];
      }
    });
  };
  
  // Get category name
  const getCategoryName = () => {
    if (!selectedCategory) return 'All Categories';
    const category = categories.find(cat => cat.id === selectedCategory);
    return category ? category.name : 'Selected Category';
  };

  // Context value
  const contextValue: HomeContextType = {
    menuItems,
    categories,
    selectedCategory,
    searchQuery,
    filters,
    pagination,
    totalPages,
    selectedItem,
    isModalOpen,
    cart,
    cartItemCount,
    isLoading,
    handleSearch,
    handleCategorySelect,
    handlePageChange,
    handleItemClick,
    handleAddToCart,
    handleCartClick,
    setIsModalOpen,
    getCategoryName
  };

  return (
    <HomeContext.Provider value={contextValue}>
      {children}
    </HomeContext.Provider>
  );
};

// Custom hook to use the home context
export const useHome = (): HomeContextType => {
  const context = useContext(HomeContext);
  if (context === undefined) {
    throw new Error('useHome must be used within a HomeProvider');
  }
  return context;
};

// Wrapper component that includes the LoadingProvider
export const HomeProviderWithLoading: React.FC<HomeProviderProps> = ({ children }) => (
  <LoadingProvider>
    <HomeProvider>{children}</HomeProvider>
  </LoadingProvider>
);