'use client';

import React from 'react';
import { MenuItem } from '@/src/types/menuItem';
import { HomeProviderWithLoading, useHome } from '@/src/providers/HomeProvider';

// UI Components
import SearchBar from '@/src/components/ui/SearchBar';
import CategoryMenu from '@/src/components/ui/CategoryMenu';
import MenuItemModal from '@/src/components/ui/MenuItemModal';
import Loader from '@/src/components/ui/Loader';
import TopMenuBar from '@/src/components/ui/TopMenuBar';

// Custom card component with counter
const MenuItemCardWithCounter: React.FC<{
  item: MenuItem;
  onItemClick: (item: MenuItem) => void;
}> = ({ item, onItemClick }) => {
  const [itemCount, setItemCount] = React.useState(0);
  
  return (
    <div
      className="card flex items-center bg-[var(--card-bg)] border border-[var(--card-border)] rounded-xl shadow-sm hover:shadow-lg transform transition-transform duration-200 ease-out hover:scale-[1.03] will-change-transform cursor-pointer"
      onClick={() => onItemClick(item)}
    >
      {/* Left: Image */}
      <img
        src={item.imageUrl || "https://images.pexels.com/photos/566566/pexels-photo-566566.jpeg"}
        alt={item.name}
        className="w-28 h-28 object-cover rounded-lg card-image"
        onError={(e) => {
          e.currentTarget.onerror = null; // prevents infinite loop if fallback fails
          e.currentTarget.src = "https://images.pexels.com/photos/566566/pexels-photo-566566.jpeg";
        }}
      />

      {/* Right: */}
      <div className="flex-1 w-full block card-right-side">
        <h3 className="text-[var(--text-primary)] font-semibold text-lg card-info">
          {item.name}
        </h3>
        <p className="text-[var(--text-secondary)] text-sm mt-1">{item.calories || '0'} Cal</p>
        <p className="text-[#e4b6cc] font-semibold text-lg mt-2">{item.price} SR</p>
        <div className="w-full card-buttons flex items-right justify-end space-x-2" onClick={(e) => e.stopPropagation()}>
          <button
            className="w-6 h-6 flex items-center justify-center bg-[#f4cadf] text-black rounded-md hover:bg-[#f0bcd8] transition"
            onClick={(e) => {
              e.stopPropagation();
              if (itemCount > 0) {
                setItemCount(itemCount - 1);
              }
            }}
          >
            −
          </button>
          <span className="w-4 text-center text-[var(--text-primary)]">{itemCount}</span>
          <button
            className="w-6 h-6 flex items-center justify-center bg-[#f4cadf] text-black rounded-md hover:bg-[#f0bcd8] transition"
            onClick={(e) => {
              e.stopPropagation();
              setItemCount(itemCount + 1);
            }}
          >
            +
          </button>
        </div>
      </div>
    </div>
  );
};

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
      <div className="container px-4 py-6 pb-20 layout">
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
              <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4 justify-items-center mx-auto">
                {menuItems?.map(item => (
                  <MenuItemCardWithCounter
                    key={item.id}
                    item={item}
                    onItemClick={handleItemClick}
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
                          ? 'pagination-button cursor-not-allowed selected-opacity'
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
                            ? 'pagination-button selected-opacity'
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
                          ? 'pagination-button cursor-not-allowed selected-opacity'
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
