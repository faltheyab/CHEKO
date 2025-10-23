'use client';

import React from 'react';
import { MenuItem } from '@/src/types/menuItem';
import { HomeProviderWithLoading, useHome } from '@/src/providers/HomeProvider';

// UI Components
import SearchBar from '@/src/components/ui/SearchBar';
import CategoryMenu from '@/src/components/ui/CategoryMenu';
import MenuItemCard from '@/src/components/ui/MenuItemCard';
import MenuItemModal from '@/src/components/ui/MenuItemModal';
import Loader from '@/src/components/ui/Loader';
import TopMenuBar from '@/src/components/ui/TopMenuBar';

const HomePage: React.FC = () => {
  // Get state and handlers from provider
  const {
    menuItems,
    categories,
    selectedCategory,
    pagination,
    totalPages,
    selectedItem,
    isModalOpen,
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
  } = useHome();
  
  return (
    <>
      <TopMenuBar />
      <div className="container mx-auto px-4 py-6 pb-20">
      {/* Section 1: Search Bar */}
      <section className="mb-8">
        <SearchBar onSearch={handleSearch} />
      </section>
      
      {/* Section 2: Category Menu */}
      <section className="mb-8">
        <CategoryMenu 
          categories={categories}
          selectedCategory={selectedCategory}
          onSelectCategory={handleCategorySelect}
        />
      </section>
      
      {/* Section 3: Menu Items */}
      <section>
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white">{getCategoryName()}</h2>
          <div className="text-sm text-gray-500 dark:text-gray-400">
            {menuItems?.length || 0} items
          </div>
        </div>
        
        <div className="h-px bg-gray-200 dark:bg-gray-700 w-full mb-6"></div>
        
        <Loader isLoading={isLoading} size="large">
          {!menuItems || menuItems.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500 dark:text-gray-400">No items found matching your criteria.</p>
            </div>
          ) : (
            <>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                {menuItems?.map(item => (
                  <MenuItemCard
                    key={item.id}
                    item={item}
                    onAddToCart={handleAddToCart}
                    onViewDetails={handleItemClick}
                  />
                ))}
              </div>
              
              {/* Pagination */}
              {totalPages > 1 && (
                <div className="flex justify-center mt-8">
                  <nav className="flex items-center space-x-2">
                    <button
                      onClick={() => handlePageChange(Math.max(0, (pagination.page || 0) - 1))}
                      disabled={(pagination.page || 0) === 0}
                      className={`px-3 py-1 rounded-md ${
                        (pagination.page || 0) === 0
                          ? 'bg-gray-100 text-gray-400 dark:bg-gray-800 dark:text-gray-600 cursor-not-allowed'
                          : 'bg-gray-200 text-gray-700 dark:bg-gray-700 dark:text-gray-300 hover:bg-gray-300 dark:hover:bg-gray-600'
                      }`}
                    >
                      Previous
                    </button>
                    
                    {[...Array(totalPages)].map((_, i) => (
                      <button
                        key={i}
                        onClick={() => handlePageChange(i)}
                        className={`w-8 h-8 flex items-center justify-center rounded-md ${
                          (pagination.page || 0) === i
                            ? 'bg-blue-500 text-white'
                            : 'bg-gray-200 text-gray-700 dark:bg-gray-700 dark:text-gray-300 hover:bg-gray-300 dark:hover:bg-gray-600'
                        }`}
                      >
                        {i + 1}
                      </button>
                    ))}
                    
                    <button
                      onClick={() => handlePageChange(Math.min(totalPages - 1, (pagination.page || 0) + 1))}
                      disabled={(pagination.page || 0) === totalPages - 1}
                      className={`px-3 py-1 rounded-md ${
                        (pagination.page || 0) === totalPages - 1
                          ? 'bg-gray-100 text-gray-400 dark:bg-gray-800 dark:text-gray-600 cursor-not-allowed'
                          : 'bg-gray-200 text-gray-700 dark:bg-gray-700 dark:text-gray-300 hover:bg-gray-300 dark:hover:bg-gray-600'
                      }`}
                    >
                      Next
                    </button>
                  </nav>
                </div>
              )}
            </>
          )}
        </Loader>
      </section>
      
      {/* Floating Cart Button */}
      <div className="fixed bottom-6 right-6 z-50">
        <button
          onClick={handleCartClick}
          className="relative flex items-center justify-center w-16 h-16 bg-blue-500 text-white rounded-full shadow-lg hover:bg-blue-600 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500"
          aria-label="View Cart"
        >
        
          <svg xmlns="http://www.w3.org/2000/svg" className="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
          </svg>
          
          {cartItemCount > 0 && (
            <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs font-bold rounded-full h-6 w-6 flex items-center justify-center">
              {cartItemCount}
            </span>
          )}
        </button>
      </div>
      
      {/* Menu Item Modal */}
      <MenuItemModal
        item={selectedItem}
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onAddToCart={handleAddToCart}
      />
    </div>
    </>
  );
};

// Wrap the HomePage with our provider
const HomePageWithProvider: React.FC = () => (
  <HomeProviderWithLoading>
    <HomePage />
  </HomeProviderWithLoading>
);

export default HomePageWithProvider;
