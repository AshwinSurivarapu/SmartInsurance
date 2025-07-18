// smartinsure-react-app/src/App.tsx

import React from 'react';
import { Routes, Route, Link as RouterLink, useNavigate } from 'react-router-dom'; // Alias Link to avoid conflict

// Material-UI Imports
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box'; // For flexible layout containers
import Container from '@mui/material/Container'; // For content centering
import ApplicationPage from './components/ApplicationPage';
import HomePage from './components/HomePage';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';


const App: React.FC = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('jwtToken'); // Clear the token
    localStorage.removeItem('username'); // Clear username
    navigate('/login'); // Redirect to login page
    alert('You have been logged out.'); // Using native alert for now
  };

  const isAuthenticated = () => {
    return localStorage.getItem('jwtToken') !== null;
  };

  const username = localStorage.getItem('username');

  return (
    <Box sx={{ flexGrow: 1 }}> {/* Main container for the app, uses MUI's Box for styling */}
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            SmartInsure
          </Typography>

          <Box sx={{ display: 'flex', gap: 2 }}> {/* Use Box for spacing between buttons */}
            <Button color="inherit" component={RouterLink} to="/">Home</Button>

            {!isAuthenticated() && (
              <>
                <Button color="inherit" component={RouterLink} to="/register">Register</Button>
                <Button color="inherit" component={RouterLink} to="/login">Login</Button>
              </>
            )}
            {isAuthenticated() && (
              <>
                <Button color="inherit" component={RouterLink} to="/application">Apply for Insurance</Button>
              </>
            )}
          </Box>

          {isAuthenticated() && (
            <Box sx={{ ml: 4, display: 'flex', alignItems: 'center', gap: 1 }}> {/* Margin-left for separation */}
              <Typography variant="body1">Welcome, {username || 'User'}!</Typography>
              <Button color="inherit" onClick={handleLogout} variant="outlined" size="small">Logout</Button>
            </Box>
          )}
        </Toolbar>
      </AppBar>

      <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}> {/* Use Container to center content */}
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/application" element={<ApplicationPage />} />
          <Route path="/dashboard" element={
            <Box sx={{ textAlign: 'center', p: 4 }}>
              <Typography variant="h4" component="h2" color="primary.main" gutterBottom>
                Welcome to your Dashboard!
              </Typography>
              <Typography variant="body1" color="text.secondary">
                You are successfully logged in. This is where personalized content will go.
              </Typography>
              <Typography variant="caption" sx={{ mt: 2, display: 'block' }}>
                JWT stored in localStorage.
              </Typography>
            </Box>
          } />
        </Routes>
      </Container>
    </Box>
  );
};

export default App;