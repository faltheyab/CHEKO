import { AppProps } from 'next/app';
import { useEffect } from 'react';
import { initializeApiClient } from '@/src/services/apiClient';
import '../app/globals.css';
import { ThemeProvider } from '../providers/ThemeProvider';

function MyApp({ Component, pageProps }: AppProps) {
  // Initialize API client on app startup
  useEffect(() => {
    const initApi = async () => {
      try {
        await initializeApiClient();
        console.log('API client initialized successfully');
      } catch (error) {
        console.error('Failed to initialize API client:', error);
      }
    };
    
    initApi();
  }, []);

  return <ThemeProvider>
            <Component {...pageProps} />
        </ThemeProvider>;
}

export default MyApp;
