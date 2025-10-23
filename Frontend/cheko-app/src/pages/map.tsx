import React from 'react';
import Head from 'next/head';
import Map from '@/src/components/ui/Map';
import BranchSearch from '@/src/components/ui/BranchSearch';
import TopMenuBar from '@/src/components/ui/TopMenuBar';
import { MapProvider, useMap } from '@/src/providers/MapProvider';

const MapPageContent: React.FC = () => {
  const { 
    filteredBranches, 
    loading, 
    error, 
    mapMarkers, 
    handleSearch, 
    handleMarkerClick 
  } = useMap();

  return (
    <>
      <Head>
        <title>CHEKO - Restaurant Locations</title>
        <meta name="description" content="Find CHEKO restaurant locations on the map" />
      </Head>

      <TopMenuBar />
      
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold mb-6 text-gray-800 dark:text-white">Restaurant Locations</h1>
        
        {/* Search component */}
        <BranchSearch onSearch={handleSearch} />
        
        {/* Status messages */}
        {loading && <p className="text-center py-4">Loading branches...</p>}
        {error && <p className="text-center py-4 text-red-500">{error}</p>}
        {!loading && !error && filteredBranches.length === 0 && (
          <p className="text-center py-4">No branches found. Try a different search.</p>
        )}
        
        {/* Map component - visible on all devices */}
        <div className="mt-4">
          <Map 
            markers={mapMarkers}
            onMarkerClick={handleMarkerClick}
            height="400px" // Reduced height for better mobile experience
            mapboxToken="pk.your_mapbox_token_here" // Replace with your actual Mapbox token
          />
        </div>
        
        {/* Branch list - visible on all devices */}
        <div className="mt-8">
          <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Branch List</h2>
          <div className="space-y-4">
            {filteredBranches.map(branch => (
              <div 
                key={branch.id} 
                className="p-4 border rounded-lg shadow-sm bg-white dark:bg-gray-800 dark:border-gray-700 cursor-pointer"
                onClick={() => handleMarkerClick(branch.id)}
              >
                <h3 className="font-bold text-lg text-gray-800 dark:text-white">{branch.name}</h3>
                <p className="text-gray-600 dark:text-gray-300">{branch.address}</p>
                <p className="mt-2">
                  <span className={`font-semibold ${branch.isActive ? 'text-green-500' : 'text-red-500'}`}>
                    {branch.isActive ? 'Open' : 'Closed'}
                  </span>
                </p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

// Wrap the MapPage with our provider
const MapPage: React.FC = () => (
  <MapProvider>
    <MapPageContent />
  </MapProvider>
);

export default MapPage;