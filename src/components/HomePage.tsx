// smartinsure-react-app/src/pages/HomePage.tsx

import React from 'react';

const HomePage: React.FC = () => {
  return (
    <div className="text-center p-10">
      <h1 className="text-4xl font-bold text-blue-700 mb-4">Welcome to SmartInsure Platform!</h1>
      <p className="text-lg text-gray-700 mb-6">
        Your one-stop solution for smart insurance recommendations.
      </p>
      <div className="space-y-4">
        <p className="text-md text-gray-600">
          Already a user? <a href="/login" className="text-blue-500 hover:underline font-semibold">Login here</a>.
        </p>
        <p className="text-md text-gray-600">
          New to SmartInsure? <a href="/register" className="text-blue-500 hover:underline font-semibold">Register for an account</a>.
        </p>
      </div>
      <p className="mt-8 text-sm text-gray-500">
        Explore intelligent plan recommendations tailored just for you.
      </p>
    </div>
  );
};

export default HomePage;