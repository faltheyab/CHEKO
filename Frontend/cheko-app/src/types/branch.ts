// Branch interfaces

export interface Branch {
  id: number;
  name: string;
  address: string;
  phoneNumber: string;
  email: string;
  latitude: number;
  longitude: number;
  isActive: boolean;
}

export interface BranchRequest {
  name: string;
  address: string;
  phoneNumber: string;
  email: string;
  latitude: number;
  longitude: number;
  isActive: boolean;
}

