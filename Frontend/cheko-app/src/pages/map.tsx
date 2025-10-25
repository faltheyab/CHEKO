import React from 'react';
import Head from 'next/head';
import Map from '@/src/components/ui/Map';
import BranchSearch from '@/src/components/ui/BranchSearch';
import TopMenuBar from '@/src/components/ui/TopMenuBar';
import Loader from '@/src/components/ui/Loader';
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
      <div className="container">
        {/*  Search Bar */}
        <section className="mb-8 layout px-4 py-6 ">
          <BranchSearch onSearch={handleSearch} />
        </section>
      
        
        {/* Status messages */}
        {error && <p className="text-center py-4 text-red-500">{error}</p>}
        {!loading && !error && filteredBranches.length === 0 && (
          <p className="text-center py-4">No branches found. Try a different search.</p>
        )}
        
        <Loader isLoading={loading} size="large">
          {/* Map component - visible on all devices */}
          <div className="w-screen h-screen map-container absolute inset-0 z-0">
            <Map
              markers={mapMarkers}
              onMarkerClick={handleMarkerClick}
            />
          </div>
          
        </Loader>
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