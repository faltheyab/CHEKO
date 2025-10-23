// Define types for logger
type LogParams = unknown[];

// Simple logger
export const logger = {
  info: (message: string, ...args: LogParams) => {
    console.log(`[INFO] ${message}`, ...args);
  },
  error: (message: string, ...args: LogParams) => {
    console.error(`[ERROR] ${message}`, ...args);
  },
  warn: (message: string, ...args: LogParams) => {
    console.warn(`[WARN] ${message}`, ...args);
  },
  debug: (message: string, ...args: LogParams) => {
    console.debug(`[DEBUG] ${message}`, ...args);
  }
};

export default logger;