import React, { useState } from 'react';

interface FilterRange {
  min: number;
  max: number;
}

interface DietaryPreferences {
  vegetarian: boolean;
  vegan: boolean;
  glutenFree: boolean;
}

interface Filters {
  priceRange: FilterRange;
  caloriesRange: FilterRange;
  dietary: DietaryPreferences;
}

interface SearchBarProps {
  onSearch: (query: string, filters: Filters) => void;
}

const SearchBar: React.FC<SearchBarProps> = ({ onSearch }) => {
  const [query, setQuery] = useState('');
  const [showFilters, setShowFilters] = useState(false);
  const [filters, setFilters] = useState<Filters>({
    priceRange: { min: 0, max: 100 },
    caloriesRange: { min: 0, max: 1000 },
    dietary: {
      vegetarian: false,
      vegan: false,
      glutenFree: false,
    }
  });

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(query, filters);
  };

  const handleFilterChange = (category: keyof Filters, field: string, value: number | boolean) => {
    setFilters(prev => ({
      ...prev,
      [category]: {
        ...prev[category],
        [field]: value
      }
    }));
  };

  return (
    <div className="w-full mb-6">
      <form onSubmit={handleSearch} className="relative">
        <div className="flex">
          <input
            type="text"
            placeholder="Search for meals..."
            className="w-full px-4 py-2 border border-gray-300 rounded-l-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-800 dark:border-gray-600 dark:text-white"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
          <button
            type="button"
            onClick={() => setShowFilters(!showFilters)}
            className="px-4 py-2 bg-gray-200 text-gray-700 hover:bg-gray-300 dark:bg-gray-700 dark:text-gray-200 dark:hover:bg-gray-600"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path fillRule="evenodd" d="M3 3a1 1 0 011-1h12a1 1 0 011 1v3a1 1 0 01-.293.707L12 11.414V15a1 1 0 01-.293.707l-2 2A1 1 0 018 17v-5.586L3.293 6.707A1 1 0 013 6V3z" clipRule="evenodd" />
            </svg>
          </button>
          <button
            type="submit"
            className="px-4 py-2 bg-blue-500 text-white rounded-r-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-blue-600 dark:hover:bg-blue-700"
          >
            Search
          </button>
        </div>

        {showFilters && (
          <div className="absolute z-10 mt-1 w-full bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-md shadow-lg p-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <h3 className="font-medium mb-2 dark:text-white">Price Range</h3>
                <div className="flex items-center space-x-2">
                  <input
                    type="number"
                    min="0"
                    className="w-20 px-2 py-1 border border-gray-300 rounded dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                    value={filters.priceRange.min}
                    onChange={(e) => handleFilterChange('priceRange', 'min', parseInt(e.target.value) || 0)}
                  />
                  <span className="dark:text-white">to</span>
                  <input
                    type="number"
                    min="0"
                    className="w-20 px-2 py-1 border border-gray-300 rounded dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                    value={filters.priceRange.max}
                    onChange={(e) => handleFilterChange('priceRange', 'max', parseInt(e.target.value) || 0)}
                  />
                </div>
              </div>

              <div>
                <h3 className="font-medium mb-2 dark:text-white">Calories</h3>
                <div className="flex items-center space-x-2">
                  <input
                    type="number"
                    min="0"
                    className="w-20 px-2 py-1 border border-gray-300 rounded dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                    value={filters.caloriesRange.min}
                    onChange={(e) => handleFilterChange('caloriesRange', 'min', parseInt(e.target.value) || 0)}
                  />
                  <span className="dark:text-white">to</span>
                  <input
                    type="number"
                    min="0"
                    className="w-20 px-2 py-1 border border-gray-300 rounded dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                    value={filters.caloriesRange.max}
                    onChange={(e) => handleFilterChange('caloriesRange', 'max', parseInt(e.target.value) || 0)}
                  />
                </div>
              </div>

              <div className="md:col-span-2">
                <h3 className="font-medium mb-2 dark:text-white">Dietary Preferences</h3>
                <div className="flex flex-wrap gap-4">
                  <label className="flex items-center space-x-2 dark:text-white">
                    <input
                      type="checkbox"
                      checked={filters.dietary.vegetarian}
                      onChange={(e) => handleFilterChange('dietary', 'vegetarian', e.target.checked)}
                      className="rounded text-blue-500 focus:ring-blue-500 dark:bg-gray-700"
                    />
                    <span>Vegetarian</span>
                  </label>
                  <label className="flex items-center space-x-2 dark:text-white">
                    <input
                      type="checkbox"
                      checked={filters.dietary.vegan}
                      onChange={(e) => handleFilterChange('dietary', 'vegan', e.target.checked)}
                      className="rounded text-blue-500 focus:ring-blue-500 dark:bg-gray-700"
                    />
                    <span>Vegan</span>
                  </label>
                  <label className="flex items-center space-x-2 dark:text-white">
                    <input
                      type="checkbox"
                      checked={filters.dietary.glutenFree}
                      onChange={(e) => handleFilterChange('dietary', 'glutenFree', e.target.checked)}
                      className="rounded text-blue-500 focus:ring-blue-500 dark:bg-gray-700"
                    />
                    <span>Gluten Free</span>
                  </label>
                </div>
              </div>
            </div>
          </div>
        )}
      </form>
    </div>
  );
};

export default SearchBar;