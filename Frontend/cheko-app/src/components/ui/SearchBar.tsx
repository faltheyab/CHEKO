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
        <div className="flex search-bar-container">
          <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="var(--search-icon)"
              strokeWidth="2"
              className="absolute mt-2 left-3 top-3 h-6 w-6 text-gray-400"
              style={{ flexShrink: 0 }}
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M21 21l-4.35-4.35M10 18a8 8 0 100-16 8 8 0 000 16z"
              />
            </svg>
          <input
            type="text"
            placeholder="Search"
            className="w-full search-bar"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
         <button
              type="button"
              onClick={() => setShowFilters(!showFilters)}
              className="flex items-center gap-2 px-4 py-2 filter-btn">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className="w-5 h-5"
                viewBox="0 0 20 20"
                fill="currentColor"
              >
                <path
                  fillRule="evenodd"
                  d="M3 3a1 1 0 011-1h12a1 1 0 011 1v3a1 1 0 01-.293.707L12 11.414V15a1 1 0 01-.293.707l-2 2A1 1 0 018 17v-5.586L3.293 6.707A1 1 0 013 6V3z"
                  clipRule="evenodd"
                />
              </svg>
              <span>Filter</span>
            </button>

          <button
            type="submit"
            className="px-4 py-2 sreach-button">
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