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
  selectedBranchIds: number[];
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
  const [selectedBranchIds, setSelectedBranchIds] = useState<number[]>([]);

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
    setSelectedBranchIds([]);
   
    if (!query.trim()) {
      return;
    }

    const searchTerm = query.trim().toLowerCase();
    const filtered = branches.filter(branch => branch.branchName.toLowerCase().includes(searchTerm));
    console.log("Seached: "+query)
    console.log("Result:" +filtered);
    setFilteredBranches(filtered);
    
    if (filtered.length > 0) {
      const ids = filtered.map(branch => branch.id);
      setSelectedBranchIds(ids);
    }
  };

  // Handle marker click
  const handleMarkerClick = (id: number) => {
    setSelectedBranchIds(prevIds => {
      if (prevIds.includes(id)) {
        return prevIds.filter(branchId => branchId !== id);
      } else {
        return [...prevIds, id];
      }
    });
  };

  const mapMarkers: MapMarker[] = branches.map(branch => ({
    id: branch.id,
    lng: branch.longitude,
    lat: branch.latitude,
    title: branch.branchName,
    description: branch.address,
    isActive: branch.isActive,
    openingHours: branch.openingHours,
    phone: branch.phone,
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
    selectedBranchIds,
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