// smartinsure-platform/ui/customer-portal-app/src/components/RegisterPage.tsx
import React from 'react';
import { TextField, Button, Typography, Container, Box } from '@mui/material';
import { useFormik } from 'formik'; // Import useFormik hook

// Define the shape of our form values
interface RegisterFormValues {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}

const RegisterPage: React.FC = () => {
  // Initialize Formik
  const formik = useFormik<RegisterFormValues>({
    initialValues: {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
    // Basic validation for demonstration. We'll enhance this later if needed.
    validate: (values) => {
      const errors: Partial<RegisterFormValues> = {}; // Partial allows some fields to be missing
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
    onSubmit: (values) => {
      // This is where our backend API call will go on Day 3
      console.log('Registration form submitted with Formik values:', values);
      alert(JSON.stringify(values, null, 2)); // Show submitted values for now
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
        {/* Link Formik's handleSubmit to the form's onSubmit */}
        <Box component="form" onSubmit={formik.handleSubmit} sx={{ mt: 3 }}>
          <TextField
            margin="normal"
            required
            fullWidth
            id="username"
            label="Username"
            name="username" // Formik uses the 'name' prop to match initialValues
            autoComplete="username"
            autoFocus
            value={formik.values.username}
            onChange={formik.handleChange} // Formik's onChange handles updates
            onBlur={formik.handleBlur} // Formik's onBlur tracks if field has been touched
            error={formik.touched.username && Boolean(formik.errors.username)} // Show error if touched and error exists
            helperText={formik.touched.username && formik.errors.username} // Display error message
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
            disabled={formik.isSubmitting} // Disable button while submitting
          >
            Sign Up
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default RegisterPage;