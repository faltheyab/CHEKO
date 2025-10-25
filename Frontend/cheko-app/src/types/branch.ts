// Branch interfaces
import { OpeningHoursSchedule } from '@/src/components/ui/Map';

export interface Branch {
  id: number;
  branchName: string;
  address: string;
  phone: string;
  email: string;
  latitude: number;
  longitude: number;
  isActive: boolean;
  isMainBranch: boolean;
  openingHours: OpeningHoursSchedule;
}


