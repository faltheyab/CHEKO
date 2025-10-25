'use client';

import React, { useRef, useEffect, useState, useMemo } from 'react';
import mapboxgl from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css';

// Define opening hours interface
interface OpeningHours {
  open: string;
  close: string;
}

export interface OpeningHoursSchedule {
  sunday: OpeningHours;
  monday: OpeningHours;
  tuesday: OpeningHours;
  wednesday: OpeningHours;
  thursday: OpeningHours;
  friday: OpeningHours;
  saturday: OpeningHours;
}

// Define marker interface
export interface MapMarker {
  id: number;
  lng: number;
  lat: number;
  title: string;
  description?: string;
  isActive: boolean;
  openingHours?: OpeningHoursSchedule;
  phone?: string;
  email?: string;
  isMainBranch?: boolean;
}

// Define props interface
interface MapProps {
  initialLng?: number;
  initialLat?: number;
  initialZoom?: number;
  markers?: MapMarker[];
  onMarkerClick?: (id: number) => void;
  width?: string;
  height?: string;
  mapboxToken?: string;
  selectedMarkers?: number[];
}

const Map: React.FC<MapProps> = ({
  initialLng,
  initialLat,
  initialZoom = 10,
  markers = [],
  onMarkerClick,
  width = '100%',
  height = '100%',
  mapboxToken = 'pk.eyJ1IjoiZmFpc2Fsc2FsZW0xMjM0NTYiLCJhIjoiY21oNmZ2b3NnMGhvcTJrc2R0MmlmcDh6MyJ9._vJolqT0e30CQ1Lklq3iuA',
  selectedMarkers = [],
}) => {
  const mapContainer = useRef<HTMLDivElement>(null);
  const map = useRef<mapboxgl.Map | null>(null);
  // Calculate map center coordinates - prioritize main branch
  const { mapLng, mapLat } = useMemo(() => {
    // If coordinates are explicitly provided, use them
    if (initialLng !== undefined && initialLat !== undefined) {
      return { mapLng: initialLng, mapLat: initialLat };
    }
    
    // First priority: Find the main branch
    const mainBranch = markers.find(marker => marker.isMainBranch === true);
    if (mainBranch) {
      console.log("Centering map on main branch:", mainBranch.title);
      return { mapLng: mainBranch.lng, mapLat: mainBranch.lat };
    }
    
    // Default to Saudi Arabia coordinates if no suitable branch found
    console.log("No suitable branch found, using default coordinates");
    return { mapLng: 45.0792, mapLat: 23.8859 };
  }, [initialLng, initialLat, markers]);

  const formatOpeningHours = (schedule: OpeningHoursSchedule): string => {
    const days = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'];
    const today = new Date().getDay(); // 0 = Sunday, 1 = Monday, etc.
    
    let hoursHTML = '<div class="opening-hours-schedule">';
    
    days.forEach((day, index) => {
      const dayHours = schedule[day as keyof OpeningHoursSchedule];
      const isToday = index === today;
      
      const formattedDay = day.charAt(0).toUpperCase() + day.slice(1);
      
      if (isToday) {
        hoursHTML += `<div class="day-hours current-day">
          <span style="font-weight: bold; color: #ff6b00;">${formattedDay}:</span>
          <span style="font-weight: bold;">${dayHours.open} - ${dayHours.close}</span>
        </div>`;
      } else {
        hoursHTML += `<div class="day-hours">
          <span>${formattedDay}:</span>
          <span>${dayHours.open} - ${dayHours.close}</span>
        </div>`;
      }
    });
    
    hoursHTML += '</div>';
    return hoursHTML;
  };

  // Initialize map when component mounts
  useEffect(() => {
    if (!mapContainer.current) return;

    // Initialize map
    map.current = new mapboxgl.Map({
      container: mapContainer.current,
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [mapLng, mapLat],
      zoom: initialZoom,
      accessToken: mapboxToken,
    });

    // Add navigation controls
    map.current.addControl(new mapboxgl.NavigationControl(), 'top-right');

    // Clean up on unmount
    return () => {
      if (map.current) {
        map.current.remove();
      }
    };
  }, [mapLat, mapLng, initialZoom, mapboxToken]);

  // Store marker references to access them later for showing popups
  const markerRefs = useRef<{ [key: number]: mapboxgl.Marker }>({});

  // Show popups for selected markers
  useEffect(() => {
    
    // Close all popups first
    Object.values(markerRefs.current).forEach(marker => {
      console.log("marker",marker.getPopup())
      const popup = marker.getPopup();
      if (popup) {
         popup.remove(); 
      }
    });
    
    // Open popups for selected markers
    if (selectedMarkers.length > 0) {
      selectedMarkers.forEach(id => {
        if (markerRefs.current[id]) {
          console.log(`Showing popup for marker ${id}`);
          markerRefs.current[id].togglePopup();
        }
      });
    }
  }, [selectedMarkers]);

  useEffect(() => {
    if (!map.current || !markers.length) return;

    const existingMarkers = document.querySelectorAll('.mapboxgl-marker');
    existingMarkers.forEach(marker => marker.remove());
    
    // Clear marker references when recreating markers
    markerRefs.current = {};

    // Add new markers
    markers.forEach(marker => {
      // Create marker element
      const el = document.createElement('div');
      el.className = 'marker';
      const markerColor = marker.isActive ? '#f4cadf' : '#F44336'; 
      
      // Create a custom marker element
      el.style.width = '30px';
      el.style.height = '30px';
      el.style.borderRadius = '50%';
      el.style.backgroundColor = markerColor;
      el.style.border = '2px solid black';
      el.style.boxShadow = '0 0 10px rgba(0,0,0,0.3)';
      el.style.cursor = 'pointer';

      // Create popup content with status indicator
      const statusLabel = marker.isActive
        ? '<span style="color: #52b86b; font-weight: bold;">Open</span>'
        : '<span style="color: #F44336; font-weight: bold;">Closed Permanently</span>';
      
      // Format opening hours if available
      let hoursDisplay = '';
      if (marker.openingHours) {
        hoursDisplay = `
          <div style="margin-bottom: 8px;">
            <h4 style="font-weight: bold; margin-bottom: 4px;">Opening Hours:</h4>
            ${formatOpeningHours(marker.openingHours)}
          </div>
        `;
      }
      
      // Add additional contact information if available
      let contactInfo = '';
      if (marker.phone || marker.email) {
        contactInfo = `
          <div style="margin-top: 8px; margin-bottom: 8px;">
            ${marker.phone ? `<p style="margin-bottom: 4px;"><strong>Phone:</strong> ${marker.phone}</p>` : ''}
            ${marker.email ? `<p style="margin-bottom: 4px;"><strong>Email:</strong> ${marker.email}</p>` : ''}
          </div>
        `;
      }

      // Add main branch badge if applicable
      const mainBranchBadge = marker.isMainBranch
        ? `<span style="background-color: #f4cadf; color: #333; font-size: 0.85rem; padding: 2px 6px; border-radius: 4px; margin-left: 8px;">Main Branch</span>`
        : '';

      const popupContent = `
        <div style="padding: 8px;">
          <h3 style="font-weight: bold; margin-bottom: 4px;">
            ${marker.title}
            ${mainBranchBadge}
          </h3>
          ${marker.description ? `<p style="margin-bottom: 8px;">${marker.description}</p>` : ''}
          ${contactInfo}
          ${hoursDisplay}
          <p style="margin-top: 8px;">Status: ${statusLabel}</p>
        </div>
      `;

      // Create popup
      const popup = new mapboxgl.Popup({ offset: 15 }).setHTML(popupContent);

      if (map.current) {
        // Create the marker and store a reference to it
        const mapMarker = new mapboxgl.Marker(el)
          .setLngLat([marker.lng, marker.lat])
          .setPopup(popup)
          .addTo(map.current);
        
        // Store reference to the marker
        markerRefs.current[marker.id] = mapMarker;
        
        // If this is one of the selected markers, show its popup
        if (selectedMarkers.includes(marker.id)) {
          console.log(`Auto-showing popup for marker ${marker.id}`);
          mapMarker.togglePopup();
        }
      }

      if (onMarkerClick) {
        el.addEventListener('click', () => {
          onMarkerClick(marker.id);
        });
      }
    });

    // Always fit bounds to show all markers
    if (markers.length > 0 && map.current) {
      const bounds = new mapboxgl.LngLatBounds();
      markers.forEach(marker => {
        bounds.extend([marker.lng, marker.lat]);
      });
      map.current.fitBounds(bounds, { padding: 100 });
    }
  }, [markers, onMarkerClick]);

  return (
    <div 
      ref={mapContainer} 
      style={{ width, height }} 
      className="absolute inset-0 z-0"
    />
  );
};

export default Map;
