import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';

// Material-UI Imports
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline'; // Optional: for consistent baseline CSS
import { createHttpLink, ApolloClient, from, InMemoryCache, ApolloProvider } from '@apollo/client';
import { setContext } from '@apollo/client/link/context';

// 1. Create an HTTP link to your GraphQL endpoint
const httpLink = createHttpLink({
  uri: 'http://localhost:8081/graphql', // Your Spring Boot GraphQL endpoint
});

// 2. Create an auth link to attach the JWT token
const authLink = setContext((_, { headers }) => {
  // Get the authentication token from local storage if it exists
  const token = localStorage.getItem('jwtToken');
  // Return the headers to the context so httpLink can read them
  return {
    headers: {
      ...headers,
      authorization: token ? `Bearer ${token}` : "",
    }
  }
});

// 3. Create the Apollo Client instance
const client = new ApolloClient({
  link: from([authLink, httpLink]), // Chain the auth link before the http link
  cache: new InMemoryCache(), // Caches query results
});
// Create a basic default theme (you can customize this later)
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2', // Example blue
    },
    secondary: {
      main: '#dc004e', // Example pink
    },
  },
  typography: {
    h1: {
      fontSize: '2.5rem',
      fontWeight: 700,
    },
    h2: {
      fontSize: '2rem',
      fontWeight: 600,
    },
    // Add more typography variants as needed
  },
});

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
<React.StrictMode>
    {/* BrowserRouter MUST wrap everything that uses React Router */}
    <BrowserRouter>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <ApolloProvider client={client}>
          <App />
        </ApolloProvider>
      </ThemeProvider>
    </BrowserRouter>
  </React.StrictMode>
);
 
// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
