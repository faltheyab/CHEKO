// Menu Section interfaces

export interface MenuSection {
  id: number;
  name: string;
  description?: string;
  displayOrder: number;
  itemCount?: number; 
}

export interface MenuSectionWithItemCount extends MenuSection {
  itemCount: number;
}
