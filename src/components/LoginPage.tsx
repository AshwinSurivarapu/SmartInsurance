// smartinsure-react-app/src/pages/LoginPage.tsx

import React from 'react';
import { useNavigate } from 'react-router-dom';
import LoginForm from './LoginForm';


const LoginPage: React.FC = () => {
  const navigate = useNavigate();

  const handleLoginSuccess = () => {
    navigate('/dashboard'); // Redirect to a dashboard or home page after successful login
  };

  return (
    <div className="login-page">
      <LoginForm onLoginSuccess={handleLoginSuccess} />
    </div>
  );
};

export default LoginPage;