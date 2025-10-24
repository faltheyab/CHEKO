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

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isMobileMenuOpen, toggleMobileMenu]);

  return (
    <nav className="bg-black dark:bg-black shadow-md menu">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="flex-shrink-0">
            <Link href="/home" className="flex items-center">
              <span className="text-xl font-bold text-blue-600 dark:text-blue-400 brand-name">CHEKO</span>
            </Link>
          </div>
          
          {/* Navigation Links */}
          <div className="hidden md:block">
            <div className="ml-10 flex items-center space-x-4">
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
            <div className="flex items-center ml-auto mr-2">
              <label htmlFor="dark-toggle" className="flex items-center cursor-pointer">
                <div className="relative flex items-center dark-light-container">
                  {/* Sun icon */}
                  <svg className="text-black dark-light-svg mr-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
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
                  <svg className="text-black dark-light-svg ml-1" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                    <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z"></path>
                  </svg>
                </div>
              </label>
            </div>  
          : null}
          
          
          {/* Mobile menu button */}
          <div className="md:hidden">
            <button 
              type="button" 
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 focus:outline-none"
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
        </div>
      </div>
      
      {/* Mobile menu */}
      <div className={`${isMobileMenuOpen ? '' : 'hidden'} md:hidden`} id="mobile-menu">
        <div className="px-2 pt-2 pb-3 space-y-1">
          {navItems.map((item) => (
            <Link
              key={item.path}
              href={item.path}
              className={`block px-3 py-2 rounded-md text-base font-medium ${
                isActive(item.path)
                  ? 'bg-blue-500 text-white'
                  : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800'
              }`}
            >
              {item.name}
            </Link>
          ))}
          
          {/* Theme Toggle Button - Mobile */}
          <div className="mt-3 px-3 py-2">
            <label htmlFor="dark-toggle-mobile" className="flex items-center cursor-pointer w-full px-3 py-2 rounded-md text-base font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800">
              <div className="relative flex items-center">
                {/* Sun icon */}
                <svg className="w-5 h-5 text-yellow-500 mr-2" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
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
                <div className="relative block border-[1px] dark:border-white border-gray-900 w-14 h-8 rounded-full transition-colors duration-300">
                  <div className={`absolute left-1 top-1 dark:bg-white bg-gray-800 w-6 h-6 rounded-full shadow-md z-10 transition-all duration-300 ease-in-out transform ${theme === 'dark' ? 'translate-x-6' : ''}`}></div>
                </div>
                
                {/* Moon icon */}
                <svg className="w-5 h-5 text-gray-400 dark:text-blue-100 ml-2" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                  <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z"></path>
                </svg>
              </div>
            </label>
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
