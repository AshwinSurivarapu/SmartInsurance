// smartinsure-react-app/src/components/ApplicationForm.tsx

import React, { useState } from 'react';
import { Box, TextField, Button, Typography, Alert, CircularProgress } from '@mui/material';
import { gql, useMutation } from '@apollo/client';

// Define the type for the data that will be passed to ApplicationPage on success
interface ApplicationSuccessData {
  userId: string;
  customerAge: number;
  customerIncome: number;
  bestOption: string;
  betterOption: string;
  goodOption: string;
  aiMessage: string; // This is the 'recommendation' field from GraphQL
}

interface ApplicationFormProps {
  onApplicationSuccess: (data: ApplicationSuccessData) => void;
}

// Define your GraphQL mutation - Request the new structured fields
const CREATE_APPLICATION_MUTATION = gql`
  mutation CreateApplication($userId: String!, $customerAge: Int!, $customerIncome: Float!) {
    createApplication(input: { userId: $userId, customerAge: $customerAge, customerIncome: $customerIncome }) {
      id
      userId
      policyType # Still present in GraphQL output (holds AI's best option)
      recommendation # Full combined AI message
      status
      bestOption # Request the individual recommendation fields
      betterOption
      goodOption
    }
  }
`;

const ApplicationForm: React.FC<ApplicationFormProps> = ({ onApplicationSuccess }) => {
  const [customerAge, setCustomerAge] = useState('');
  const [customerIncome, setCustomerIncome] = useState('');
  const [error, setError] = useState<string | null>(null);
  const username = localStorage.getItem('username'); // Get username from localStorage

  const [createApplication, { loading }] = useMutation(CREATE_APPLICATION_MUTATION, {
    onCompleted: (data) => {
      // Pass the structured data to the success handler in ApplicationPage
      onApplicationSuccess({
        userId: data.createApplication.userId,
        customerAge: parseInt(customerAge, 10), // Pass original input age
        customerIncome: parseFloat(customerIncome), // Pass original input income
        bestOption: data.createApplication.bestOption,
        betterOption: data.createApplication.betterOption,
        goodOption: data.createApplication.goodOption,
        aiMessage: data.createApplication.recommendation, // Use 'recommendation' for the general AI message
      });
    },
    onError: (err) => {
      console.error('GraphQL Application Error:', err);
      setError(err.message || 'Failed to get recommendation.');
    },
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (!username || !customerAge || !customerIncome) {
      setError("Please fill all required fields.");
      return;
    }

    try {
      await createApplication({
        variables: {
          userId: username,
          customerAge: parseInt(customerAge, 10), // Convert to Int
          customerIncome: parseFloat(customerIncome), // Convert to Float
        },
      });
    } catch (err) {
      // Error handling is primarily done in the onError callback above
    }
  };

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      sx={{
        p: 4, bgcolor: 'background.paper', borderRadius: 2, boxShadow: 3,
        maxWidth: 500, mx: 'auto', my: 4, display: 'flex', flexDirection: 'column', gap: 3,
      }}
    >
      <Typography variant="h5" component="h2" sx={{ textAlign: 'center', color: 'text.primary' }}>
        Get Your Policy Recommendation
      </Typography>

      {error && <Alert severity="error">{error}</Alert>}

      {/* Removed: Policy Type TextField */}

      <TextField
        fullWidth
        label="Your Age"
        variant="outlined"
        value={customerAge}
        onChange={(e) => setCustomerAge(e.target.value)}
        required
        type="number" // Ensure numeric input
        margin="normal"
      />
      <TextField
        fullWidth
        label="Your Annual Income"
        variant="outlined"
        value={customerIncome}
        onChange={(e) => setCustomerIncome(e.target.value)}
        required
        type="number" // Ensure numeric input
        margin="normal"
        helperText="e.g., 50000.00"
      />

      <Button
        type="submit"
        variant="contained"
        color="primary"
        fullWidth
        disabled={loading}
        sx={{ mt: 2 }}
        startIcon={loading ? <CircularProgress size={20} color="inherit" /> : null}
      >
        {loading ? 'Getting Recommendation...' : 'Get Recommendation'}
      </Button>
    </Box>
  );
};

export default ApplicationForm;