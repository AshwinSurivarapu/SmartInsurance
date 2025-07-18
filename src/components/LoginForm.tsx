// smartinsure-react-app/src/components/LoginForm.tsx

import React, { useState } from 'react';

// Material-UI Imports
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Alert from '@mui/material/Alert'; // For error messages

interface LoginFormProps {
  onLoginSuccess: () => void; // Callback for successful login
}

const LoginForm: React.FC<LoginFormProps> = ({ onLoginSuccess }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      const data = await response.json(); // Attempt to parse JSON even on error

      if (response.ok) {
        localStorage.setItem('jwtToken', data.token);
        localStorage.setItem('username', data.username);
        onLoginSuccess();
      } else {
        setError(data.message || 'Login failed. Please check your credentials.');
      }
    } catch (err: any) { // Type err as any to access message safely
      setError('Network error. Could not connect to the server or server returned unexpected response. ' + err.message);
      console.error('Login Error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      component="form" // Render as a form element
      onSubmit={handleSubmit}
      sx={{
        p: 4, // Padding
        bgcolor: 'background.paper', // White background
        borderRadius: 2, // Rounded corners
        boxShadow: 3, // Shadow effect
        maxWidth: 400, // Max width
        mx: 'auto', // Center horizontally
        my: 4, // Margin top/bottom
        display: 'flex',
        flexDirection: 'column',
        gap: 3, // Spacing between form elements
      }}
    >
      <Typography variant="h5" component="h2" sx={{ textAlign: 'center', color: 'text.primary' }}>
        Login
      </Typography>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      <TextField
        fullWidth // Takes full width
        label="Username" // Label for the input
        variant="outlined" // Standard Material-UI style
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
        margin="normal" // Adds top and bottom margin
      />

      <TextField
        fullWidth
        label="Password"
        type="password" // For password input
        variant="outlined"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
        margin="normal"
      />

      <Button
        type="submit"
        variant="contained" // Filled button
        color="primary"
        fullWidth // Takes full width
        disabled={loading}
        sx={{ mt: 2 }} // Margin top
      >
        {loading ? 'Logging in...' : 'Login'}
      </Button>
    </Box>
  );
};

export default LoginForm;