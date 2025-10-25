import React, { createContext, useContext, useState, ReactNode } from 'react';
import { useRouter } from 'next/router';

// Define navigation item type
interface NavItem {
  name: string;
  path: string;
}

// Define the context type
interface TopMenuBarContextType {
  navItems: NavItem[];
  isMobileMenuOpen: boolean;
  toggleMobileMenu: () => void;
  closeMobileMenu: () => void;
  isActive: (path: string) => boolean;
}

// Create the context
const TopMenuBarContext = createContext<TopMenuBarContextType | undefined>(undefined);

// Provider props
interface TopMenuBarProviderProps {
  children: ReactNode;
}

export const TopMenuBarProvider: React.FC<TopMenuBarProviderProps> = ({ children }) => {
  const router = useRouter();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  
  // Define navigation items
  const navItems: NavItem[] = [
    { name: 'Home', path: '/home' },
    { name: 'Map', path: '/map' },
  ];

  // Toggle mobile menu
  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(prev => !prev);
  };

  // Close mobile menu
  const closeMobileMenu = () => {
    setIsMobileMenuOpen(false);
  };

  // Check if a path is active
  const isActive = (path: string): boolean => {
    return router.pathname === path;
  };

  // Context value
  const contextValue: TopMenuBarContextType = {
    navItems,
    isMobileMenuOpen,
    toggleMobileMenu,
    closeMobileMenu,
    isActive
  };

  return (
    <TopMenuBarContext.Provider value={contextValue}>
      {children}
    </TopMenuBarContext.Provider>
  );
};

// Custom hook to use the top menu bar context
export const useTopMenuBar = (): TopMenuBarContextType => {
  const context = useContext(TopMenuBarContext);
  if (context === undefined) {
    throw new Error('useTopMenuBar must be used within a TopMenuBarProvider');
  }
  return context;
};