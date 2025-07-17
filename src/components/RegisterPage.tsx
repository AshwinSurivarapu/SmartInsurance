// smartinsure-platform/ui/customer-portal-app/src/components/RegisterPage.tsx
import React, { useState } from 'react'; // Import useState
import { TextField, Button, Typography, Container, Box, Alert, CircularProgress } from '@mui/material'; // Import Alert, CircularProgress
import { useFormik } from 'formik';

interface RegisterFormValues {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}

const RegisterPage: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [message, setMessage] = useState<string | null>(null);
  const [isError, setIsError] = useState<boolean>(false);

  const formik = useFormik<RegisterFormValues>({
    initialValues: {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
    validate: (values) => {
      const errors: Partial<RegisterFormValues> = {};
      if (!values.username) {
        errors.username = 'Required';
      }
      if (!values.email) {
        errors.email = 'Required';
      } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)) {
        errors.email = 'Invalid email address';
      }
      if (!values.password) {
        errors.password = 'Required';
      } else if (values.password.length < 6) {
        errors.password = 'Password must be at least 6 characters';
      }
      if (!values.confirmPassword) {
        errors.confirmPassword = 'Required';
      } else if (values.confirmPassword !== values.password) {
        errors.confirmPassword = 'Passwords do not match';
      }
      return errors;
    },
    onSubmit: async (values) => {
      setLoading(true); // Start loading
      setMessage(null); // Clear previous messages
      setIsError(false);

      // Define the backend URL
      const REGISTER_URL = 'http://localhost:8080/api/auth/register'; // User Auth Service is on 8080

      try {
        const response = await fetch(REGISTER_URL, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          // Send only username, email, and password as required by the backend DTO
          body: JSON.stringify({
            username: values.username,
            email: values.email,
            password: values.password,
          }),
        });

        const data = await response.json();

        if (response.ok) { // Check for 2xx status codes
          setMessage(data.message || 'Registration successful!');
          setIsError(false);
          // Optionally, clear form or redirect
          formik.resetForm(); // Clear the form after success
        } else { // Handle non-2xx status codes (e.g., 400 Bad Request)
          setMessage(data.message || 'Registration failed. Please try again.');
          setIsError(true);
        }
      } catch (error: any) {
        setMessage(`Network error or server unreachable: ${error.message}`);
        setIsError(true);
      } finally {
        setLoading(false); // End loading
      }
    },
  });

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Typography component="h1" variant="h5">
          Register
        </Typography>

        {message && ( // Display message if present
          <Alert severity={isError ? "error" : "success"} sx={{ width: '100%', mt: 2 }}>
            {message}
          </Alert>
        )}

        <Box component="form" onSubmit={formik.handleSubmit} sx={{ mt: 3 }}>
          <TextField
            margin="normal"
            required
            fullWidth
            id="username"
            label="Username"
            name="username"
            autoComplete="username"
            autoFocus
            value={formik.values.username}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.username && Boolean(formik.errors.username)}
            helperText={formik.touched.username && formik.errors.username}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"
            value={formik.values.email}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.email && Boolean(formik.errors.email)}
            helperText={formik.touched.email && formik.errors.email}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="new-password"
            value={formik.values.password}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.password && Boolean(formik.errors.password)}
            helperText={formik.touched.password && formik.errors.password}
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="confirmPassword"
            label="Confirm Password"
            type="password"
            id="confirmPassword"
            autoComplete="new-password"
            value={formik.values.confirmPassword}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            error={formik.touched.confirmPassword && Boolean(formik.errors.confirmPassword)}
            helperText={formik.touched.confirmPassword && formik.errors.confirmPassword}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3, mb: 2 }}
            disabled={formik.isSubmitting || loading} // Disable if Formik is submitting or our custom loading state is true
          >
            {loading ? <CircularProgress size={24} color="inherit" /> : 'Sign Up'}
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default RegisterPage;