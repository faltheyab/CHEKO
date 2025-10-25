import React from "react";

interface LoaderProps {
  isLoading: boolean;
  children: React.ReactNode;
  size?: "small" | "medium" | "large";
  fullScreen?: boolean;
  text?: string;
}

const Loader: React.FC<LoaderProps> = ({
  isLoading,
  children,
  size = "large",
  fullScreen = true,
  text = "",
}) => {
  // Spinner sizing
  const sizeMap = {
    small: { spinner: "h-6 w-6", text: "text-sm" },
    medium: { spinner: "h-12 w-12", text: "text-base" },
    large: { spinner: "h-16 w-16", text: "text-lg" },
  };

  return (
    <div className="relative">
      {/* Content with smooth blur */}
      <div
        className="transition-[filter,opacity] duration-300 ease-in-out"
        style={{
          filter: isLoading ? "blur(4px)" : "blur(0px)",
          opacity: isLoading ? 0.6 : 1,
        }}
      >
        {children}
      </div>

      {/* Overlay */}
      <div
        className={`
          absolute inset-0 flex flex-col items-center justify-center
          bg-white/50 dark:bg-gray-900/50 backdrop-blur-sm z-50
          transition-opacity duration-300 ease-in-out
          ${isLoading ? "opacity-100 visible" : "opacity-0 invisible"}
          ${fullScreen ? "fixed" : ""}
        `}
      >
        {/* Spinner with smooth scale transition */}
        <div
          className={`
            rounded-full border-4 border-gray-300 border-t-blue-500
            ${sizeMap[size].spinner}
            transition-transform duration-300 ease-in-out
            ${isLoading ? "scale-100 animate-spin" : "scale-90"}
          `}
        ></div>

        {text && (
          <p
            className={`mt-4 text-gray-700 dark:text-gray-300 font-medium ${sizeMap[size].text} transition-opacity duration-300`}
            style={{ opacity: isLoading ? 1 : 0 }}
          >
            {text}
          </p>
        )}
      </div>
    </div>
  );
};

export default Loader;
