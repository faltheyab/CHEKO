/**
 * API Configuration
 * This module loads configuration from a public JSON file that can be modified after deployment
 */

// Define the configuration interface
export interface ApiConfig {
  apiBaseUrl: string;
  timeout: number;
}

const defaultConfig: ApiConfig = {
  apiBaseUrl: '',
  timeout: 10000
};

let loadedConfig: ApiConfig | null = null;

export async function loadConfig(): Promise<ApiConfig> {
  if (loadedConfig) {
    return loadedConfig;
  }
  
  try {
    // Fetch the config file from the public directory
    const response = await fetch('/config.json');
    if (!response.ok) {
      console.error('Failed to load config.json, using default config');
      return defaultConfig;
    }
    
    const config = await response.json() as ApiConfig;
    loadedConfig = config;
    return config;
  } catch (error) {
    console.error('Error loading config:', error);
    return defaultConfig;
  }
}

export function getConfig(): ApiConfig {
  return loadedConfig || defaultConfig;
}

export default {
  loadConfig,
  getConfig
};
