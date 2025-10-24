'use client';

import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { MenuItem, PaginatedResponse, PaginationParams } from '@/src/types/menuItem';
import { MenuSection, MenuSectionWithItemCount } from '@/src/types/menuSection';
import { menuItemService } from '@/src/services/menuItemService';
import { menuSectionService } from '@/src/services/menuSectionService';
import { LoadingProvider, useLoading } from '@/src/context/LoadingContext';

interface CartItem {
  item: MenuItem;
  quantity: number;
}
interface HomeContextType {
  menuItems: MenuItem[];
  categories: MenuSectionWithItemCount[];
  selectedCategory: number | null;
  searchQuery: string;
  pagination: PaginationParams;
  totalPages: number;
  selectedItem: MenuItem | null;
  isModalOpen: boolean;
  cart: CartItem[];
  cartItemCount: number;
  isLoading: boolean;
  totalItems: number | null;
  handleSearch: (query: string) => void;
  handleCategorySelect: (categoryId: number | null) => void;
  handlePageChange: (newPage: number) => void;
  handleItemClick: (item: MenuItem) => void;
  handleAddToCart: (item: MenuItem, quantity: number) => void;
  handleCartClick: () => void;
  setIsModalOpen: (isOpen: boolean) => void;
  getCategoryName: () => string;
}
interface HomeProviderProps {
  children: ReactNode;
}

const HomeContext = createContext<HomeContextType | undefined>(undefined);

export const HomeProvider: React.FC<HomeProviderProps> = ({ children }) => {

  const { isLoading, startLoading, stopLoading } = useLoading();
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [categories, setCategories] = useState<MenuSectionWithItemCount[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
  const [totalItems, setTotalItems] = useState<number | null>(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [pagination, setPagination] = useState<PaginationParams>({
    page: 0,
    size: 9,
    sort: 'name,asc'
  });
  const [totalPages, setTotalPages] = useState(1);
  const [selectedItem, setSelectedItem] = useState<MenuItem | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [cart, setCart] = useState<CartItem[]>([]);
  const cartItemCount = cart.reduce((total, item) => total + item.quantity, 0);
  const handleCartClick = () => {
    alert("Faisal has not implemented this yet");
  };
  
  // Fetch categories
  useEffect(() => {
    const fetchCategories = async () => {
      startLoading();
      const data = await menuSectionService.getMenuSectionsWithCounts();
      setCategories(data);
      stopLoading();
    };
    fetchCategories();
  }, [startLoading, stopLoading]);
  
  // Fetch menu items when selected category, search query, or pagination changes
  useEffect(() => {
    const fetchMenuItems = async () => {
      startLoading();
      
      // Use the new method that supports both search and category filtering
      const data = await menuItemService.getPaginatedWithFiltersAndSection(
        pagination.page || 0,
        pagination.size || 9,
        pagination.sort || 'name,asc',
        searchQuery,
        selectedCategory
      );
      
      setMenuItems(data.content);
      setTotalPages(data.totalPages);
      setTotalItems(data.totalElements);
      stopLoading();
    };
    
    fetchMenuItems();
  }, [startLoading, stopLoading, selectedCategory, searchQuery, pagination]);
  
  const handleSearch = (query: string) => {
    setSearchQuery(query);
    setPagination(prev => ({ ...prev, page: 0 }));
  };
  
  const handleCategorySelect = (categoryId: number | null) => {
    setSelectedCategory(categoryId);
    setPagination(prev => ({ ...prev, page: 0 })); 
  };
  
  const handlePageChange = (newPage: number) => {
    setPagination(prev => ({ ...prev, page: newPage }));
  };
  
  const handleItemClick = (item: MenuItem) => {
    setSelectedItem(item);
    setIsModalOpen(true);
  };
  
  const handleAddToCart = (item: MenuItem, quantity: number) => {
    setCart(prevCart => {
      const existingItemIndex = prevCart.findIndex(cartItem => cartItem.item.id === item.id);      
      if (existingItemIndex >= 0) {
        const updatedCart = [...prevCart];
        updatedCart[existingItemIndex].quantity += quantity;
        return updatedCart;
      } else {
        return [...prevCart, { item, quantity }];
      }
    });
  };
  
  const getCategoryName = () => {
    if (!selectedCategory) return 'All Categories';
    const category = categories.find(cat => cat.id === selectedCategory);
    return category ? category.name : 'Selected Category';
  };

  const contextValue: HomeContextType = {
    menuItems,
    categories,
    selectedCategory,
    searchQuery,
    pagination,
    totalPages,
    selectedItem,
    isModalOpen,
    cart,
    cartItemCount,
    isLoading,
    totalItems,
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

export const useHome = (): HomeContextType => {
  const context = useContext(HomeContext);
  if (context === undefined) {
    throw new Error('useHome must be used within a HomeProvider');
  }
  return context;
};

export const HomeProviderWithLoading: React.FC<HomeProviderProps> = ({ children }) => (
  <LoadingProvider>
    <HomeProvider>{children}</HomeProvider>
  </LoadingProvider>
);