import React from 'react';

interface LoaderProps {
  isLoading: boolean;
  children: React.ReactNode;
  size?: 'small' | 'medium' | 'large';
  fullScreen?: boolean;
  text?: string;
}

const Loader: React.FC<LoaderProps> = ({
  isLoading,
  children,
  size = 'medium',
  fullScreen = false,
  text = 'Loading...'
}) => {
  // Size mappings
  const sizeMap = {
    small: {
      spinner: 'h-6 w-6',
      text: 'text-sm'
    },
    medium: {
      spinner: 'h-12 w-12',
      text: 'text-base'
    },
    large: {
      spinner: 'h-16 w-16',
      text: 'text-lg'
    }
  };

  // If not loading, just render children
  if (!isLoading) {
    return <>{children}</>;
  }

  return (
    <div className="relative">

      <div className={`${isLoading ? 'blur-sm' : ''} transition-all duration-300`}>
        {children}
      </div>

      <div 
        className={`
          absolute inset-0 flex flex-col items-center justify-center 
          bg-white/50 dark:bg-gray-900/50 backdrop-blur-sm z-50
          ${fullScreen ? 'fixed' : ''}
        `}
      >

        <div className={`animate-spin rounded-full border-4 border-gray-300 border-t-blue-500 ${sizeMap[size].spinner}`}></div>
        
        {text && (
          <p className={`mt-4 text-gray-700 dark:text-gray-300 font-medium ${sizeMap[size].text}`}>
            {text}
          </p>
        )}
      </div>
    </div>
  );
};

export default Loader;
