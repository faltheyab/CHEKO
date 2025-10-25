// Branch interfaces
import { OpeningHoursSchedule } from '@/src/components/ui/Map';

export interface Branch {
  id: number;
  branchName: string;
  address: string;
  phoneNumber: string;
  email: string;
  latitude: number;
  longitude: number;
  isActive: boolean;
  isMainBranch: boolean;
  openingHours: OpeningHoursSchedule;
}

export interface BranchRequest {
  name: string;
  address: string;
  phoneNumber: string;
  email: string;
  latitude: number;
  longitude: number;
  isActive: boolean;
  isMainBranch: boolean;
  openingHours: OpeningHoursSchedule;
}

