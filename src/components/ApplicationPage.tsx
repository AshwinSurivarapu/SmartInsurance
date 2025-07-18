// smartinsure-react-app/src/pages/ApplicationPage.tsx

import React, { useState } from 'react';

const ApplicationPage: React.FC = () => {
  const [customerUsername, setCustomerUsername] = useState('');
  const [customerAge, setCustomerAge] = useState('');
  const [customerIncome, setCustomerIncome] = useState('');
  const [responseMessage, setResponseMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [recommendations, setRecommendations] = useState<any>(null); // To store AI recommendations

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setResponseMessage(null);
    setError(null);
    setRecommendations(null);
    setLoading(true);

    // Get JWT token from localStorage
    const jwtToken = localStorage.getItem('jwtToken');
    if (!jwtToken) {
      setError('You must be logged in to apply for insurance.');
      setLoading(false);
      return;
    }

    try {
      const response = await fetch('http://localhost:8081/api/applications', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${jwtToken}` // Include JWT token for authentication
        },
        body: JSON.stringify({
          customerUsername,
          customerAge: parseInt(customerAge), // Convert age to number
          customerIncome: parseFloat(customerIncome), // Convert income to number
        }),
      });

      const data = await response.json();

      if (response.ok) {
        setResponseMessage(data.message || 'Application submitted successfully!');
        setRecommendations(data.recommendedPlans); // Store the structured recommendations
      } else {
        setError(data.message || 'Failed to submit application.');
      }
    } catch (err) {
      setError('Network error or server unavailable. Please try again later.');
      console.error('Application submission error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md max-w-lg mx-auto my-10">
      <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Apply for Insurance</h2>

      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="username">
            Your Username:
          </label>
          <input
            type="text"
            id="username"
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            value={customerUsername}
            onChange={(e) => setCustomerUsername(e.target.value)}
            required
            readOnly // Often, username might be pre-filled from logged-in user
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="age">
            Age:
          </label>
          <input
            type="number"
            id="age"
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            value={customerAge}
            onChange={(e) => setCustomerAge(e.target.value)}
            required
            min="18"
          />
        </div>
        <div className="mb-6">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="income">
            Annual Income:
          </label>
          <input
            type="number"
            id="income"
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
            value={customerIncome}
            onChange={(e) => setCustomerIncome(e.target.value)}
            required
            min="0"
          />
        </div>
        <div className="flex items-center justify-between">
          <button
            type="submit"
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
            disabled={loading}
          >
            {loading ? 'Submitting...' : 'Get Recommendations'}
          </button>
        </div>
      </form>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mt-4" role="alert">
          {error}
        </div>
      )}

      {responseMessage && (
        <div className="bg-blue-100 border border-blue-400 text-blue-700 px-4 py-3 rounded relative mt-4">
          <p className="font-bold">{responseMessage}</p>
          {recommendations && (
            <div className="mt-2 text-left">
              <h3 className="font-semibold text-gray-800">AI Recommended Plans:</h3>
              <ul className="list-disc list-inside">
                <li><span className="font-medium">Best Option:</span> {recommendations.bestOption}</li>
                <li><span className="font-medium">Better Option:</span> {recommendations.betterOption}</li>
                <li><span className="font-medium">Good Option:</span> {recommendations.goodOption}</li>
              </ul>
              <p className="mt-2 text-sm text-gray-600">{recommendations.aiFeedback}</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default ApplicationPage;