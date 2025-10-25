import React, { useState } from 'react';

interface BranchSearchProps {
  onSearch: (query: string) => void;
}

const BranchSearch: React.FC<BranchSearchProps> = ({ onSearch }) => {
  const [query, setQuery] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(query);
  };

  const handleButtonClick = () => {
    onSearch(query);
  };

  return (
    <form onSubmit={handleSubmit} className="w-full mb-6 relative z-10">
      <div className='flex search-bar-container'>
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
          placeholder="Search ..."
          className="w-full search-bar"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <button
          type="submit"
          onClick={handleButtonClick}
          className="px-4 py-2 sreach-button">
          Search
        </button>
      </div>
    </form>
  );
};

export default BranchSearch;
