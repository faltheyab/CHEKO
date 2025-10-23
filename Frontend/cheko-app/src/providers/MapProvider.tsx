import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useRouter } from 'next/router';
import branchService from '@/src/services/branchService';
import { Branch } from '@/src/types/branch';
import { MapMarker } from '@/src/components/ui/Map';

// Mock opening hours data - in a real app, this would come from the API
const mockOpeningHours = {
  sunday: { open: '10:00 AM', close: '10:00 PM' },
  monday: { open: '10:00 AM', close: '10:00 PM' },
  tuesday: { open: '10:00 AM', close: '10:00 PM' },
  wednesday: { open: '10:00 AM', close: '10:00 PM' },
  thursday: { open: '10:00 AM', close: '10:00 PM' },
  friday: { open: '10:00 AM', close: '11:00 PM' },
  saturday: { open: '10:00 AM', close: '11:00 PM' },
};

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
      try {
        setLoading(true);
        const branches = await branchService.getAll();
        setBranches(branches);
        setFilteredBranches(branches);
        setLoading(false);
      } catch (err) {
        setError('Failed to load branches. Please try again later.');
        setLoading(false);
        console.error('Error fetching branches:', err);
      }
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
      branch.name.toLowerCase().includes(query.toLowerCase()) ||
      branch.address.toLowerCase().includes(query.toLowerCase())
    );
    setFilteredBranches(filtered);
  };

  // Handle marker click - navigate to branch details
  const handleMarkerClick = (id: number) => {
    router.push(`/branch/${id}`);
  };

  // Convert branches to map markers
  const mapMarkers: MapMarker[] = filteredBranches.map(branch => ({
    id: branch.id,
    lng: branch.longitude,
    lat: branch.latitude,
    title: branch.name,
    description: branch.address,
    isActive: branch.isActive,
    openingHours: mockOpeningHours, // In a real app, this would come from the branch data
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
