import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { TopMenuBarProvider, useTopMenuBar } from '@/src/providers/TopMenuBarProvider';
import { ThemeProvider, useTheme } from '@/src/providers/ThemeProvider';

const TopMenuBarContent: React.FC = () => {
  const { navItems, isMobileMenuOpen, toggleMobileMenu, isActive } = useTopMenuBar();
  const { theme, toggleTheme } = useTheme();
  const [mounted, setMounted] = useState(false);

  // Mount effect to prevent hydration mismatch
  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setMounted(true);
  }, []);

  // Close mobile menu when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      const mobileMenu = document.getElementById('mobile-menu');
      const mobileMenuButton = document.querySelector('[aria-controls="mobile-menu"]');
      
      if (
        mobileMenu &&
        !mobileMenu.contains(event.target as Node) &&
        mobileMenuButton &&
        !mobileMenuButton.contains(event.target as Node) &&
        isMobileMenuOpen
      ) {
        toggleMobileMenu();
      }
    };

    // Add overlay click handler for mobile menu
    const handleOverlayClick = () => {
      if (isMobileMenuOpen) {
        toggleMobileMenu();
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    
    // Add event listener for overlay
    const overlay = document.querySelector('.mobile-menu-overlay');
    if (overlay) {
      overlay.addEventListener('click', handleOverlayClick);
    }
    
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      if (overlay) {
        overlay.removeEventListener('click', handleOverlayClick);
      }
    };
  }, [isMobileMenuOpen, toggleMobileMenu]);

  return (
    <nav className="bg-black dark:bg-black shadow-md menu">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          {/* Mobile menu button - Moved to the left */}
          <div className="md:hidden">
            <button
              type="button"
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 focus:outline-none sandwish-button"
              aria-controls="mobile-menu"
              aria-expanded={isMobileMenuOpen}
              onClick={toggleMobileMenu}
            >
              <span className="sr-only">Open main menu</span>
              <svg className="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            </button>
          </div>
          
          {/* Logo */}
          <div className="flex-shrink-0">
            <Link href="/home" className="flex items-center">
              <span className="text-xl font-bold text-blue-600 dark:text-blue-400 brand-name">CHEKO</span>
            </Link>
          </div>
          
          {/* Navigation Links */}
          <div className="hidden md:block">
            <div className="ml-10 menu flex items-center space-x-4">
              {navItems.map((item) => (
                <Link
                  key={item.path}
                  href={item.path}
                  className={`px-3 py-2 rounded-md text-sm font-medium ${
                    isActive(item.path)
                      ? 'active-top-menu'
                      : 'nonactive-top-menu'
                  }`}
                >
                  {item.name}
                </Link>
              ))}
            </div>
          </div>
          
          {/* Theme Toggle Button */}
          {mounted ?
            <div className="flex items-center ml-auto mr-2 hide-if-mobile">
              <label htmlFor="dark-toggle" className="flex items-center cursor-pointer">
                <div className="relative flex items-center dark-light-container">
                  {/* Sun icon */}
                  <svg className="text dark-light-svg mr-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                    <path d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z" fillRule="evenodd" clipRule="evenodd"></path>
                  </svg>
                  
                  <input
                    type="checkbox"
                    name="dark-mode"
                    id="dark-toggle"
                    className="checkbox hidden"
                    checked={theme === 'dark'}
                    onChange={toggleTheme}
                  />
                  <div className="relative block bg-black border-[1px] dark:border-white border-gray-900 w-12 h-5 rounded-full transition-colors duration-300 dark-light-pill-container">
                    <div className={`absolute left-1 top-1 dark:bg-white bg-gray-800 w-4 h-4 dark-light-pill rounded-full shadow-md z-10 transition-all duration-300 ease-in-out transform ${theme === 'dark' ? 'translate-x-6' : ''}`}></div>
                  </div>
                  
                  {/* Moon icon */}
                  <svg className="text dark-light-svg ml-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                    <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z"></path>
                  </svg>
                </div>
              </label>
            </div>
          : null}
        </div>
      </div>
      
      {/* Mobile menu overlay */}
      {isMobileMenuOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden mobile-menu-overlay"
          aria-hidden="true"
        ></div>
      )}
      
      {/* Mobile menu */}
      <div
        className={`${isMobileMenuOpen ? 'slide-in-active' : 'slide-in-inactive'} md:hidden fixed top-0 left-0 h-full w-3/4 bg-mobile-menu shadow-lg z-50 transform transition-transform duration-300 ease-in-out`}
        id="mobile-menu"
      >
        <div className="px-4 pt-6 pb-3 space-y-1">
          {/* Close button */}
          <div className="flex justify-end mb-4">
            <button
              onClick={toggleMobileMenu}
              className="p-2 rounded-md text-gray-500 hover:text-gray-700 dark:text-gray-300 dark:hover:text-gray-100 focus:outline-none"
            >
              <span className="sr-only text">Close menu</span>
              <svg className="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          {navItems.map((item) => (
            <Link
              key={item.path}
              href={item.path}
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive(item.path)
                  ? 'primary-button'
                  : 'text'
              }`}
            >
              {item.name}
            </Link>
          ))}
          
          {/* Theme Toggle Button - Mobile - Enlarged for better touch */}
          <div className="mt-6 px-3 py-4">
            <div className="flex flex-col">
              <span className="text-lg font-medium mb-3 text">Theme</span>
              <label htmlFor="dark-toggle-mobile" className="flex items-center justify-center cursor-pointer w-full px-3 py-4 rounded-md text-base font-medium text">
                {/* Sun icon */}
                <svg className="w-7 h-7  mr-5 text" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                  <path d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z" fillRule="evenodd" clipRule="evenodd"></path>
                </svg>
                
                <input
                  type="checkbox"
                  name="dark-mode-mobile"
                  id="dark-toggle-mobile"
                  className="checkbox hidden"
                  checked={theme === 'dark'}
                  onChange={toggleTheme}
                />
                <div className="relative block border-[1px] text w-16 h-10 rounded-full transition-colors duration-300 mx-5">
                  <div className={`absolute left-1 top-1 bg-white text w-8 h-8 rounded-full shadow-md z-10 transition-all duration-300 ease-in-out transform ${theme === 'dark' ? 'translate-x-6' : ''}`}></div>
                </div>
                
                {/* Moon icon */}
                <svg className="text w-7 h-7 " fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                  <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z"></path>
                </svg>
              </label>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
};


const TopMenuBar: React.FC = () => (
  <TopMenuBarProvider>
    <TopMenuBarContent />
  </TopMenuBarProvider>
);

export default TopMenuBar;
