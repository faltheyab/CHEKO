import React, { useRef, useState, useEffect } from 'react';
import { MenuSectionWithItemCount } from '@/src/types/menuSection';

// Helper function to get appropriate icon path based on category name
const getCategoryIcon = (categoryName: string): string => {
  // Convert to lowercase for easier matching
  const name = categoryName.toLowerCase();
  
  // Return appropriate SVG path string based on category name
  if (name.includes('breakfast') || name.includes('morning')) {
    // Breakfast icon (coffee cup)
    return "M2 21v-2h18v2H2zM20 8V5h-2v3h2zM6 8V5H4v3h6V5H8v3H6zM4 9v5h16V9H4z M4 19h16v-3H4v3z";
  } else if (name.includes('lunch') || name.includes('main')) {
    // Lunch icon (plate with fork and knife)
    return "M8.1 13.34l2.83-2.83L3.91 3.5c-1.56 1.56-1.56 4.09 0 5.66l4.19 4.18zm6.78-1.81c1.53.71 3.68.21 5.27-1.38 1.91-1.91 2.28-4.65.81-6.12-1.46-1.46-4.2-1.1-6.12.81-1.59 1.59-2.09 3.74-1.38 5.27L3.7 19.87l1.41 1.41L12 14.41l6.88 6.88 1.41-1.41L13.41 13l1.47-1.47z";
  } else if (name.includes('dinner') || name.includes('evening')) {
    // Dinner icon (dining table)
    return "M2 19h20v2H2v-2zm9-11h2v8h-2zM5 8h2v8H5zm12 0h2v8h-2zM2 3v2h20V3H2z";
  } else if (name.includes('dessert') || name.includes('sweet') || name.includes('cake')) {
    // Dessert icon (cake)
    return "M12 6c1.11 0 2-.9 2-2 0-.38-.1-.73-.29-1.03L12 0l-1.71 2.97c-.19.3-.29.65-.29 1.03 0 1.1.9 2 2 2zm4.6 9.99l-1.07-1.07-1.08 1.07c-1.3 1.3-3.58 1.31-4.89 0l-1.07-1.07-1.09 1.07C6.75 16.64 5.88 17 4.96 17c-.73 0-1.4-.23-1.96-.61V21c0 .55.45 1 1 1h16c.55 0 1-.45 1-1v-4.61c-.56.38-1.23.61-1.96.61-.92 0-1.79-.36-2.44-1.01zM18 9h-5V7h-2v2H6c-1.66 0-3 1.34-3 3v1.54c0 1.08.88 1.96 1.96 1.96.52 0 1.02-.2 1.38-.57l2.14-2.13 2.13 2.13c.74.74 2.03.74 2.77 0l2.14-2.13 2.13 2.13c.37.37.86.57 1.38.57 1.08 0 1.96-.88 1.96-1.96V12C21 10.34 19.66 9 18 9z";
  } else if (name.includes('drink') || name.includes('beverage')) {
    // Drinks icon (glass)
    return "M3 2l2.01 18.23C5.13 21.23 5.97 22 7 22h10c1.03 0 1.87-.77 1.99-1.77L21 2H3zm9 17c-1.66 0-3-1.34-3-3 0-2 3-5.4 3-5.4s3 3.4 3 5.4c0 1.66-1.34 3-3 3zm6.33-11H5.67l-.44-4h13.53l-.43 4z";
  } else if (name.includes('appetizer') || name.includes('starter')) {
    // Appetizer icon (small plate)
    return "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-5-9h10v2H7z";
  } else if (name.includes('salad') || name.includes('vegetable') || name.includes('vegan')) {
    // Salad/Vegetable icon (leaf)
    return "M6.05 8.05c-2.73 2.73-2.73 7.17 0 9.9C7.42 19.32 9.21 20 11 20s3.58-.68 4.95-2.05C19.43 14.47 20 4 20 4S9.53 4.57 6.05 8.05zm8.49 8.49c-.95.94-2.2 1.46-3.54 1.46-1.34 0-2.59-.52-3.54-1.46-1.95-1.96-1.95-5.12 0-7.07.97-.97 2.25-1.46 3.54-1.46 1.28 0 2.56.49 3.54 1.46 1.95 1.95 1.95 5.11 0 7.07z";
  } else if (name.includes('seafood') || name.includes('fish')) {
    // Seafood icon (fish)
    return "M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-10.5h2v5h-2zm0-4h2v2h-2z";
  } else if (name.includes('pizza')) {
    // Pizza icon
    return "M12 2C8.43 2 5.23 3.54 3.01 6L12 22l8.99-16C18.78 3.55 15.57 2 12 2zm0 15.92L5.51 6.36C7.32 4.85 9.62 4 12 4s4.68.85 6.49 2.36L12 17.92zM9 5.5c-.83 0-1.5.67-1.5 1.5S8.17 8.5 9 8.5s1.5-.67 1.5-1.5S9.82 5.5 9 5.5zm1.5 7.5c0 .83.67 1.5 1.5 1.5.82 0 1.5-.67 1.5-1.5s-.68-1.5-1.5-1.5-1.5.67-1.5 1.5z";
  } else {
    // Default food icon (fork and knife)
    return "M11 9H9V2H7v7H5V2H3v7c0 2.12 1.66 3.84 3.75 3.97V22h2.5v-9.03C11.34 12.84 13 11.12 13 9V2h-2v7zm5-3v8h2.5v8H21V2c-2.76 0-5 2.24-5 4z";
  }
};

interface CategoryMenuProps {
  categories: MenuSectionWithItemCount[];
  selectedCategory: number | null;
  onSelectCategory: (categoryId: number | null) => void;
}

const CategoryMenu: React.FC<CategoryMenuProps> = ({
  categories,
  selectedCategory,
  onSelectCategory,
}) => {
  const scrollContainerRef = useRef<HTMLDivElement>(null);
  const [showLeftArrow, setShowLeftArrow] = useState(false);
  const [showRightArrow, setShowRightArrow] = useState(false);

  // Check if scroll arrows should be shown
  useEffect(() => {
    const checkScroll = () => {
      if (scrollContainerRef.current) {
        const { scrollLeft, scrollWidth, clientWidth } = scrollContainerRef.current;
        setShowLeftArrow(scrollLeft > 0);
        setShowRightArrow(scrollLeft < scrollWidth - clientWidth - 5); // 5px buffer
      }
    };

    checkScroll();
    window.addEventListener('resize', checkScroll);
    return () => window.removeEventListener('resize', checkScroll);
  }, [categories]);

  // Handle scroll events to update arrow visibility
  const handleScroll = () => {
    if (scrollContainerRef.current) {
      const { scrollLeft, scrollWidth, clientWidth } = scrollContainerRef.current;
      setShowLeftArrow(scrollLeft > 0);
      setShowRightArrow(scrollLeft < scrollWidth - clientWidth - 5); // 5px buffer
    }
  };

  // Scroll left/right
  const scroll = (direction: 'left' | 'right') => {
    if (scrollContainerRef.current) {
      const scrollAmount = 200; // Adjust as needed
      const newScrollLeft = direction === 'left'
        ? scrollContainerRef.current.scrollLeft - scrollAmount
        : scrollContainerRef.current.scrollLeft + scrollAmount;
      
      scrollContainerRef.current.scrollTo({
        left: newScrollLeft,
        behavior: 'smooth'
      });
    }
  };

  return (
    <div className="relative mb-6">
      <div className="flex items-center">
        {/* Scrollable categories */}
        <div className="relative flex-grow overflow-hidden">
          {showLeftArrow && (
            <button
              onClick={() => scroll('left')}
              className="absolute left-0 top-1/2 transform -translate-y-1/2 z-10 bg-white dark:bg-gray-800 bg-opacity-80 dark:bg-opacity-80 rounded-full p-1 shadow-md"
              aria-label="Scroll left"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-gray-700 dark:text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
            </button>
          )}
          
          <div
            ref={scrollContainerRef}
            className="flex space-x-2 overflow-x-auto scrollbar-hide py-2"
            onScroll={handleScroll}
          >
            {/* All Categories option */}
            <button
              type="button"
              onClick={() => onSelectCategory(null)}
              className="category-menu-button"
            >
              {/* Colored icon container */}
              <div className="category-menu-button-icon-container bg-[#f4cadf]">
                {/* Grid icon representing all categories */}
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                  className="category-menu-button-icon"
                >
                  <path d="M3 3h8v8H3V3zm0 10h8v8H3v-8zm10 0h8v8h-8v-8zm0-10h8v8h-8V3z" />
                </svg>
              </div>

              {/* Text content */}
              <span className={`category-menu-button-text ${selectedCategory === null ? 'category-menu-button-text-selected' : 'category-menu-button-text-normal'}`}>All Categories</span>
            </button>
            
            {/* Category buttons */}
            {categories.map((category) => (
              <button
                type="button"
                key={category.id}
                onClick={() => onSelectCategory(category.id)}
                className="category-menu-button"
              >
                {/* Colored icon container - use different colors based on category id */}
                <div className={`category-menu-button-icon-container ${
                  // Assign different background colors based on category id
                  ['bg-[#f4cadf]', 'bg-[#cadbf4]', 'bg-[#d0f4ca]', 'bg-[#f4e5ca]', 'bg-[#e0caf4]'][category.id % 5]
                }`}>
                  {/* Dynamic SVG based on category name */}
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="currentColor"
                    viewBox="0 0 24 24"
                    className="category-menu-button-icon"
                  >
                    <path d={getCategoryIcon(category.name)} />
                  </svg>
                </div>

                {/* Text content */}
                <span className={`category-menu-button-text ${selectedCategory === category.id ? 'category-menu-button-text-selected' : 'category-menu-button-text-normal'}`}>
                  {category.name} <span className="category-menu-button-count">{category.itemCount}</span>
                </span>
              </button>
            ))}
          </div>
          
          {showRightArrow && (
            <button
              onClick={() => scroll('right')}
              className="absolute right-0 top-1/2 transform -translate-y-1/2 z-10 bg-white dark:bg-gray-800 bg-opacity-80 dark:bg-opacity-80 rounded-full p-1 shadow-md"
              aria-label="Scroll right"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-gray-700 dark:text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
              </svg>
            </button>
          )}
        </div>
        
        {/* Divider */}
        <div className="mx-4 h-6 border-l border-gray-300 dark:border-gray-600"></div>
        
        {/* Orders button (not scrollable) */}
        <button
          type="button"
          className="category-menu-button"
        >
          {/* Colored icon container */}
          <div className="category-menu-button-icon-container bg-[#f4d0ca]">
            {/* Shopping bag icon for orders */}
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="currentColor"
              viewBox="0 0 24 24"
              className="category-menu-button-icon"
            >
              <path d="M18 6h-2c0-2.21-1.79-4-4-4S8 3.79 8 6H6c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2zm-8 4c0 .55-.45 1-1 1s-1-.45-1-1V8h2v2zm3-4c1.1 0 2 .9 2 2h-4c0-1.1.9-2 2-2zm3 4c0 .55-.45 1-1 1s-1-.45-1-1V8h2v2z" />
            </svg>
          </div>

          {/* Text content */}
          <span className="category-menu-button-text category-menu-button-text-normal">Orders</span>
        </button>
      </div>
    </div>
  );
};

export default CategoryMenu;
