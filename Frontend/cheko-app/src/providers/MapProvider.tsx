import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useRouter } from 'next/router';
import branchService from '@/src/services/branchService';
import { Branch } from '@/src/types/branch';
import { MapMarker } from '@/src/components/ui/Map';


// Define the context type
interface MapContextType {
  branches: Branch[];
  filteredBranches: Branch[];
  loading: boolean;
  error: string | null;
  mapMarkers: MapMarker[];
  handleSearch: (query: string) => void;
  handleMarkerClick: (id: number) => void;
}

// Create the context
const MapContext = createContext<MapContextType | undefined>(undefined);

// Provider props
interface MapProviderProps {
  children: ReactNode;
}

export const MapProvider: React.FC<MapProviderProps> = ({ children }) => {
  const [branches, setBranches] = useState<Branch[]>([]);
  const [filteredBranches, setFilteredBranches] = useState<Branch[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  // Fetch branches on component mount
  useEffect(() => {
    const fetchBranches = async () => {
      setLoading(true);
      const branches = await branchService.getAll();
      setBranches(branches);
      setFilteredBranches(branches);
      setLoading(false);
    };

    fetchBranches();
  }, []);

  // Handle search
  const handleSearch = (query: string) => {
    if (!query.trim()) {
      setFilteredBranches(branches);
      return;
    }

    const filtered = branches.filter(branch => 
      branch.branchName.toLowerCase().includes(query.toLowerCase()) ||
      branch.address.toLowerCase().includes(query.toLowerCase())
    );
    setFilteredBranches(filtered);
  };

  // Handle marker click
  const handleMarkerClick = (id: number) => {
    // Just handle the marker click without navigation
    console.log(`Marker clicked: ${id}`);
  };

  // Convert branches to map markers - show all branches, not just filtered ones
  const mapMarkers: MapMarker[] = branches.map(branch => ({
    id: branch.id,
    lng: branch.longitude,
    lat: branch.latitude,
    title: branch.branchName,
    description: branch.address,
    isActive: branch.isActive,
    openingHours: branch.openingHours,
    phone: branch.phoneNumber,
    email: branch.email,
    isMainBranch: branch.isMainBranch
  }));

  // Context value
  const contextValue: MapContextType = {
    branches,
    filteredBranches,
    loading,
    error,
    mapMarkers,
    handleSearch,
    handleMarkerClick
  };

  return (
    <MapContext.Provider value={contextValue}>
      {children}
    </MapContext.Provider>
  );
};

// Custom hook to use the map context
export const useMap = (): MapContextType => {
  const context = useContext(MapContext);
  if (context === undefined) {
    throw new Error('useMap must be used within a MapProvider');
  }
  return context;
};

// Made with Bob
