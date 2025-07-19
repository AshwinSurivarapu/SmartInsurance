// smartinsure-react-app/src/components/ApplicationPage.tsx

import React, { useState } from 'react';
import ApplicationForm from './ApplicationForm';
import {
  Box, Typography, Alert, Paper,
  TableContainer, Table, TableHead, TableRow, TableCell, TableBody
} from '@mui/material';

// Define the type for the data received from ApplicationForm's onApplicationSuccess
interface RecommendationResult {
  userId: string;
  customerAge: number;
  customerIncome: number;
  bestOption: string;
  betterOption: string;
  goodOption: string;
  aiMessage: string;
}

const ApplicationPage: React.FC = () => {
  const [recommendationResult, setRecommendationResult] = useState<RecommendationResult | null>(null);

  const handleApplicationSuccess = (data: RecommendationResult) => {
    setRecommendationResult(data);
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: 'center' }}>
        Insurance Application
      </Typography>

      <ApplicationForm onApplicationSuccess={handleApplicationSuccess} />

      {recommendationResult && (
        <Paper sx={{ p: 4, mt: 4, bgcolor: 'background.paper', borderRadius: 2, boxShadow: 3, maxWidth: 800, mx: 'auto' }}>
          <Typography variant="h5" component="h2" gutterBottom sx={{ color: 'primary.main', textAlign: 'center', mb: 3 }}>
            Your Personalized Insurance Recommendation
          </Typography>

          {/* Display User Details */}
          <Box sx={{ mb: 3, textAlign: 'center' }}>
            <Typography variant="h6">User Details:</Typography>
            <Typography variant="body1">
              **Username:** {recommendationResult.userId} | **Age:** {recommendationResult.customerAge} | **Income:** ${recommendationResult.customerIncome.toLocaleString()}
            </Typography>
          </Box>

          {/* Display general AI message */}
          <Alert severity="success" sx={{ mb: 3 }}>
            Recommendation Received! {recommendationResult.aiMessage}
          </Alert>

          {/* Display Recommendations in Tabular Format */}
          <TableContainer component={Paper} elevation={1}>
            <Table aria-label="recommendation table">
              <TableHead>
                <TableRow sx={{ bgcolor: 'primary.light' }}>
                  <TableCell sx={{ color: 'primary.contrastText', fontWeight: 'bold' }}>Rank</TableCell>
                  <TableCell sx={{ color: 'primary.contrastText', fontWeight: 'bold' }}>Recommended Policy</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                <TableRow>
                  <TableCell>Best Option</TableCell>
                  <TableCell>{recommendationResult.bestOption}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>Better Option</TableCell>
                  <TableCell>{recommendationResult.betterOption}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>Good Option</TableCell>
                  <TableCell>{recommendationResult.goodOption}</TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </TableContainer>

          <Typography variant="caption" sx={{ mt: 2, display: 'block', textAlign: 'center', color: 'text.secondary' }}>
            These recommendations are AI-generated based on your provided profile.
          </Typography>
        </Paper>
      )}
    </Box>
  );
};

export default ApplicationPage;