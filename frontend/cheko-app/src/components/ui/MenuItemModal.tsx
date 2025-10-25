import React, { useEffect, useRef } from 'react';
import { MenuItem } from '@/src/types/menuItem';
import Image from 'next/image';

interface MenuItemModalProps {
  item: MenuItem | null;
  isOpen: boolean;
  onClose: () => void;
  onAddToCart: (item: MenuItem, quantity: number) => void;
}

const MenuItemModal: React.FC<MenuItemModalProps> = ({ item, isOpen, onClose, onAddToCart }) => {
  // Use the item ID as a key to reset state when item changes
  const itemId = item?.id || 'no-item';
  const [quantity, setQuantity] = React.useState(1);
  const modalRef = useRef<HTMLDivElement>(null);

  // Close modal when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (modalRef.current && !modalRef.current.contains(event.target as Node)) {
        onClose();
      }
    };

    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside);
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isOpen, onClose]);

  // Close modal on escape key
  useEffect(() => {
    const handleEscKey = (event: KeyboardEvent) => {
      if (event.key === 'Escape') {
        onClose();
      }
    };

    if (isOpen) {
      document.addEventListener('keydown', handleEscKey);
    }

    return () => {
      document.removeEventListener('keydown', handleEscKey);
    };
  }, [isOpen, onClose]);

  if (!isOpen || !item) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 modal-backdrop-enter modal-backdrop-enter-active">
      <div
        ref={modalRef}
        className="modal rounded-lg shadow-xl max-w-md w-full max-h-[90vh] overflow-y-auto modal-content-enter modal-content-enter-active"
      >
        {/* Image */}
        <div className="relative h-64 w-full">
          {item.imageUrl ? (
            <Image
              src={item.imageUrl} 
              alt={item.name}
              fill
              className="object-cover rounded-t-lg"
              onError={(e) => {
                e.currentTarget.onerror = null; // prevents infinite loop if fallback fails
                e.currentTarget.src = "https://images.pexels.com/photos/566566/pexels-photo-566566.jpeg";
              }}
            />
          ) : (
            <div className="w-full h-full bg-gray-200 dark:bg-gray-700 flex items-center justify-center rounded-t-lg">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-16 w-16 text-gray-400 dark:text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
          )}

          {/* Close button */}
          <button
            onClick={onClose}
            className="absolute top-2 right-2 bg-white dark:bg-gray-800 rounded-full p-1 shadow-md cursor-pointer hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
            aria-label="Close modal"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 text-gray-700 dark:text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        {/* Content */}
        <div className="p-6">
          <div className="flex justify-between items-start mb-4">
            <h2 className="text-2xl font-bold">{item.name}</h2>
            <span className="text-xl font-bold text">SR{item.price.toFixed(2)}</span>
          </div>
          
          <div className="flex items-center mb-4">
            <span className="bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200 text-xs font-medium px-2.5 py-0.5 rounded">
              {item.calories} calories
            </span>
            {!item.isAvailable && (
              <span className="ml-2 bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200 text-xs font-medium px-2.5 py-0.5 rounded">
                Currently unavailable
              </span>
            )}
          </div>
          
          <p className="text mb-6">{item.description}</p>
          
          {/* Quantity selector and add to cart */}
          {item.isAvailable && (
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-2">
                <button
                  onClick={() => quantity > 1 && setQuantity(quantity - 1)}
                  className="w-10 h-10 flex items-center justify-center rounded-full bg-[#f4cadf] text-black rounded-md hover:bg-[#f0bcd8]"
                  aria-label="Decrease quantity"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 12H4" />
                  </svg>
                </button>
                
                <span className="text text-xl w-8 text-center">{quantity}</span>
                
                <button
                  onClick={() => setQuantity(quantity + 1)}
                  className="w-10 h-10 flex items-center justify-center rounded-full  bg-[#f4cadf] text-black rounded-md hover:bg-[#f0bcd8]"
                  aria-label="Increase quantity"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                  </svg>
                </button>
              </div>
              
              <button
                onClick={() => {
                  onAddToCart(item, quantity);
                  onClose();
                }}
                className="px-6 py-2 primary-button"
              >
                Add to Cart
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

// Use a wrapper component to reset state when item changes
const MenuItemModalWrapper: React.FC<MenuItemModalProps> = (props) => {
  const { item } = props;
  const itemKey = item?.id || 'no-item';
  
  return <MenuItemModal key={itemKey} {...props} />;
};

export default MenuItemModalWrapper;