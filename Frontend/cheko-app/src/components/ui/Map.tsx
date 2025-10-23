'use client';

import React, { useRef, useEffect, useState } from 'react';
import mapboxgl from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css';

// Define opening hours interface
interface OpeningHours {
  open: string;
  close: string;
}

interface OpeningHoursSchedule {
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
}

const Map: React.FC<MapProps> = ({
  initialLng = 45.0792, // Default to Saudi Arabia
  initialLat = 23.8859,
  initialZoom = 5,
  markers = [],
  onMarkerClick,
  width = '100%',
  height = '500px',
  mapboxToken = 'pk.your_mapbox_token_here', // Default token, should be replaced with actual token
}) => {
  const mapContainer = useRef<HTMLDivElement>(null);
  const map = useRef<mapboxgl.Map | null>(null);

  // Format opening hours for display
  const formatOpeningHours = (schedule: OpeningHoursSchedule): string => {
    const days = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'];
    const today = new Date().getDay(); // 0 = Sunday, 1 = Monday, etc.
    const todayName = days[today].toLowerCase();
    
    // Get today's hours
    const todayHours = schedule[todayName as keyof OpeningHoursSchedule];
    
    return `Today: ${todayHours.open} - ${todayHours.close}`;
  };

  // Initialize map when component mounts
  useEffect(() => {
    if (!mapContainer.current) return;

    // Initialize map
    map.current = new mapboxgl.Map({
      container: mapContainer.current,
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [initialLng, initialLat],
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
  }, [initialLat, initialLng, initialZoom, mapboxToken]);

  // Add markers when markers prop changes or map is initialized
  useEffect(() => {
    if (!map.current || !markers.length) return;

    // Remove existing markers
    const existingMarkers = document.querySelectorAll('.mapboxgl-marker');
    existingMarkers.forEach(marker => marker.remove());

    // Add new markers
    markers.forEach(marker => {
      // Create marker element
      const el = document.createElement('div');
      el.className = 'marker';
      const markerColor = marker.isActive ? '#4CAF50' : '#F44336'; // Green for active, Red for inactive
      
      // Create a custom marker element
      el.style.width = '24px';
      el.style.height = '24px';
      el.style.borderRadius = '50%';
      el.style.backgroundColor = markerColor;
      el.style.border = '2px solid white';
      el.style.boxShadow = '0 0 10px rgba(0,0,0,0.3)';
      el.style.cursor = 'pointer';

      // Create popup content with status indicator
      const statusLabel = marker.isActive 
        ? '<span style="color: #4CAF50; font-weight: bold;">Open</span>' 
        : '<span style="color: #F44336; font-weight: bold;">Closed</span>';
      
      // Format opening hours if available
      let hoursDisplay = '';
      if (marker.openingHours) {
        hoursDisplay = `<p style="margin-bottom: 4px;">Hours: ${formatOpeningHours(marker.openingHours)}</p>`;
      }
      
      const popupContent = `
        <div style="padding: 8px;">
          <h3 style="font-weight: bold; margin-bottom: 4px;">${marker.title}</h3>
          ${marker.description ? `<p style="margin-bottom: 4px;">${marker.description}</p>` : ''}
          ${hoursDisplay}
          <p>Status: ${statusLabel}</p>
        </div>
      `;

      // Create popup
      const popup = new mapboxgl.Popup({ offset: 15 }).setHTML(popupContent);

      // Add marker to map with null check
      if (map.current) {
        new mapboxgl.Marker(el)
          .setLngLat([marker.lng, marker.lat])
          .setPopup(popup)
          .addTo(map.current);
      }

      if (onMarkerClick) {
        el.addEventListener('click', () => {
          onMarkerClick(marker.id);
        });
      }
    });

    // Fit bounds to markers if there are multiple markers
    if (markers.length > 1 && map.current) {
      const bounds = new mapboxgl.LngLatBounds();
      markers.forEach(marker => {
        bounds.extend([marker.lng, marker.lat]);
      });
      map.current.fitBounds(bounds, { padding: 50 });
    }
  }, [markers, onMarkerClick]);

  return (
    <div 
      ref={mapContainer} 
      style={{ width, height }} 
      className="map-container rounded-lg shadow-lg"
    />
  );
};

export default Map;
