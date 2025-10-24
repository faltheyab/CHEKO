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
    totalItems,
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
          <h2 className="text-2xl font-bold text-primary-color">{getCategoryName()}</h2>
          <div className="text-sm font-bold text-primary-color">
            { totalItems || 0} 
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
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
                {menuItems?.map(item => (
                  <div
                    key={item.id}
                    className="flex items-center justify-between bg-[var(--card-bg)] border border-[var(--card-border)] rounded-xl p-4 shadow-sm hover:shadow-md transition-all"
                    onClick={() => handleItemClick(item)}
                  >
                    {/* Left: Image */}
                    <img
                      src={item.imageUrl || "https://images.unsplash.com/photo-1604909053002-e9ad26553097?auto=format&fit=crop&w=400&q=80"}
                      alt={item.name}
                      className="w-28 h-28 object-cover rounded-lg"
                    />

                    {/* Middle: Info */}
                    <div className="flex-1 px-3">
                      <h3 className="text-[var(--text-primary)] font-semibold text-lg">
                        {item.name}
                      </h3>
                      <p className="text-[var(--text-secondary)] text-sm mt-1">{item.calories || '0'} Cal</p>
                      <p className="text-[#e4b6cc] font-semibold text-lg mt-2">{item.price} SR</p>
                    </div>

                    {/* Right: Quantity Controls */}
                    <div className="flex items-center gap-2" onClick={(e) => e.stopPropagation()}>
                      <button
                        className="w-8 h-8 flex items-center justify-center bg-[#f4cadf] text-black rounded-md hover:bg-[#f0bcd8] transition"
                        onClick={(e) => {
                          e.stopPropagation();
                          // Decrease quantity logic would go here
                        }}
                      >
                        −
                      </button>
                      <span className="w-4 text-center text-[var(--text-primary)]">0</span>
                      <button
                        className="w-8 h-8 flex items-center justify-center bg-[#f4cadf] text-black rounded-md hover:bg-[#f0bcd8] transition"
                        onClick={(e) => {
                          e.stopPropagation();
                          handleAddToCart(item, 1);
                        }}
                      >
                        +
                      </button>
                    </div>
                  </div>
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
                          ? 'pagination-buttoncursor-not-allowed'
                          : 'pagination-button'
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
                            ? 'pagination-button'
                            : 'pagination-button'
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
                          ? 'pagination-button cursor-not-allowed'
                          : 'pagination-button'
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
          className="relative flex items-center justify-center w-16 h-16 rounded-full shadow-lg cart-btn transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500"
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
