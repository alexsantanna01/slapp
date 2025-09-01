import React, { useEffect, useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  MenuItem,
  Avatar,
  IconButton,
  Alert,
  Container,
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  FormHelperText,
  InputAdornment,
  Paper,
} from '@mui/material';
import { PhotoCamera, Visibility, VisibilityOff } from '@mui/icons-material';
import { toast } from 'react-toastify';
import { Link, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handleRegister, reset } from './register.reducer';
import { translate } from 'react-jhipster';

// Custom CSS Variables
const customStyles = `
:root {
  --gold-primary: #d4a037;
  --gold-dark: #9b6c1f;
  --wood-brown: #4d3424;
  --wood-light: #7a573f;
  --off-beige: #f5e9d5;
  --text-base: #1a1a1a;
  --border-input: #c1b59e;
  --background-input: #fdf9f1;
  --background-card: #fdfcf8;
  --disabled: #8b8b8b;
  --text-button-primary: #ffffff;
}
`;

// Apply custom styles
if (typeof document !== 'undefined') {
  const styleElement = document.createElement('style');
  styleElement.textContent = customStyles;
  document.head.appendChild(styleElement);
}

interface FormData {
  email: string;
  firstName: string;
  lastName: string;
  password: string;
  confirmPassword: string;
  userType: 'CUSTOMER' | 'STUDIO_OWNER' | '';
  phoneNumber: string;
  profileImage?: File;
}

interface FormErrors {
  email?: string;
  firstName?: string;
  lastName?: string;
  password?: string;
  confirmPassword?: string;
  userType?: string;
  phoneNumber?: string;
}

export const RegisterPage = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [profileImagePreview, setProfileImagePreview] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const [formData, setFormData] = useState<FormData>({
    email: '',
    firstName: '',
    lastName: '',
    password: '',
    confirmPassword: '',
    userType: '',
    phoneNumber: '',
  });

  const [errors, setErrors] = useState<FormErrors>({});

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const successMessage = useAppSelector(state => state.register.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
      navigate('/login');
    }
  }, [successMessage]);

  const validateEmail = (email: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validateForm = (): boolean => {
    const newErrors: FormErrors = {};

    if (!formData.email) {
      newErrors.email = 'Email é obrigatório';
    } else if (!validateEmail(formData.email)) {
      newErrors.email = 'Email deve ter um formato válido';
    }

    if (!formData.firstName) {
      newErrors.firstName = 'Nome é obrigatório';
    }

    if (!formData.lastName) {
      newErrors.lastName = 'Sobrenome é obrigatório';
    }

    if (!formData.password) {
      newErrors.password = 'Senha é obrigatória';
    } else if (formData.password.length < 4) {
      newErrors.password = 'Senha deve ter pelo menos 4 caracteres';
    }

    if (!formData.confirmPassword) {
      newErrors.confirmPassword = 'Confirmação de senha é obrigatória';
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Senhas não coincidem';
    }

    if (!formData.userType) {
      newErrors.userType = 'Tipo de usuário é obrigatório';
    }

    if (!formData.phoneNumber) {
      newErrors.phoneNumber = 'Número de telefone é obrigatório';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleInputChange = (field: keyof FormData) => (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData(prev => ({ ...prev, [field]: event.target.value }));
    // Clear error when user starts typing
    if (errors[field]) {
      setErrors(prev => ({ ...prev, [field]: undefined }));
    }
  };

  const handleSelectChange = (field: keyof FormData) => (event: any) => {
    setFormData(prev => ({ ...prev, [field]: event.target.value }));
    if (errors[field]) {
      setErrors(prev => ({ ...prev, [field]: undefined }));
    }
  };

  const handleImageUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      setFormData(prev => ({ ...prev, profileImage: file }));
      const reader = new FileReader();
      reader.onload = e => {
        setProfileImagePreview(e.target?.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();

    if (!validateForm()) {
      return;
    }

    setIsLoading(true);

    try {
      // Por enquanto, enviamos apenas os campos aceitos pelo handleRegister atual
      // Os campos adicionais (firstName, lastName, userType, phoneNumber) podem
      // ser enviados em uma chamada posterior ou o reducer pode ser expandido
      dispatch(
        handleRegister({
          login: formData.email,
          email: formData.email,
          password: formData.password,
          langKey: currentLocale,
          firstName: formData.firstName,
          lastName: formData.lastName,
          activated: true,
        }),
      );

      // TODO: Implementar envio dos campos adicionais:
      // - userType: formData.userType
      // - phoneNumber: formData.phoneNumber
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        backgroundColor: 'var(--off-beige)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        padding: 2,
      }}
    >
      <Container maxWidth="sm">
        <Card
          sx={{
            backgroundColor: 'var(--background-card)',
            borderRadius: 3,
            boxShadow: '0 8px 32px rgba(77, 52, 36, 0.15)',
            border: '1px solid var(--border-input)',
          }}
        >
          <CardContent sx={{ p: 4 }}>
            <Typography
              variant="h4"
              component="h1"
              align="center"
              gutterBottom
              sx={{
                color: 'var(--wood-brown)',
                fontWeight: 600,
                mb: 3,
              }}
            >
              Registro
            </Typography>

            <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2.5 }}>
              {/* Profile Image Upload */}
              <Box sx={{ display: 'flex', justifyContent: 'center', mb: 2 }}>
                <Box sx={{ position: 'relative' }}>
                  <Avatar
                    src={profileImagePreview || undefined}
                    sx={{
                      width: 100,
                      height: 100,
                      backgroundColor: 'var(--gold-primary)',
                      border: '3px solid var(--border-input)',
                    }}
                  />
                  <IconButton
                    sx={{
                      position: 'absolute',
                      bottom: -5,
                      right: -5,
                      backgroundColor: 'var(--gold-primary)',
                      color: 'white',
                      '&:hover': {
                        backgroundColor: 'var(--gold-dark)',
                      },
                      width: 35,
                      height: 35,
                    }}
                    component="label"
                  >
                    <PhotoCamera fontSize="small" />
                    <input type="file" hidden accept="image/*" onChange={handleImageUpload} />
                  </IconButton>
                </Box>
              </Box>

              {/* Email Field */}
              <TextField
                fullWidth
                label="Email"
                type="email"
                value={formData.email}
                onChange={handleInputChange('email')}
                error={!!errors.email}
                helperText={errors.email}
                required
                sx={{
                  '& .MuiOutlinedInput-root': {
                    backgroundColor: 'var(--background-input)',
                    '& fieldset': {
                      borderColor: 'var(--border-input)',
                    },
                    '&:hover fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                    '&.Mui-focused fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                  },
                  '& .MuiInputLabel-root.Mui-focused': {
                    color: 'var(--wood-brown)',
                  },
                }}
              />

              {/* Name Fields */}
              <Box sx={{ display: 'flex', gap: 2, flexWrap: { xs: 'wrap', sm: 'nowrap' } }}>
                <TextField
                  fullWidth
                  label="Nome"
                  value={formData.firstName}
                  onChange={handleInputChange('firstName')}
                  error={!!errors.firstName}
                  helperText={errors.firstName}
                  required
                  sx={{
                    '& .MuiOutlinedInput-root': {
                      backgroundColor: 'var(--background-input)',
                      '& fieldset': {
                        borderColor: 'var(--border-input)',
                      },
                      '&:hover fieldset': {
                        borderColor: 'var(--gold-primary)',
                      },
                      '&.Mui-focused fieldset': {
                        borderColor: 'var(--gold-primary)',
                      },
                    },
                    '& .MuiInputLabel-root.Mui-focused': {
                      color: 'var(--wood-brown)',
                    },
                  }}
                />
                <TextField
                  fullWidth
                  label="Sobrenome"
                  value={formData.lastName}
                  onChange={handleInputChange('lastName')}
                  error={!!errors.lastName}
                  helperText={errors.lastName}
                  required
                  sx={{
                    '& .MuiOutlinedInput-root': {
                      backgroundColor: 'var(--background-input)',
                      '& fieldset': {
                        borderColor: 'var(--border-input)',
                      },
                      '&:hover fieldset': {
                        borderColor: 'var(--gold-primary)',
                      },
                      '&.Mui-focused fieldset': {
                        borderColor: 'var(--gold-primary)',
                      },
                    },
                    '& .MuiInputLabel-root.Mui-focused': {
                      color: 'var(--wood-brown)',
                    },
                  }}
                />
              </Box>

              {/* Password Fields */}
              <TextField
                fullWidth
                label="Senha"
                type={showPassword ? 'text' : 'password'}
                value={formData.password}
                onChange={handleInputChange('password')}
                error={!!errors.password}
                helperText={errors.password}
                required
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton onClick={() => setShowPassword(!showPassword)} edge="end">
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
                sx={{
                  '& .MuiOutlinedInput-root': {
                    backgroundColor: 'var(--background-input)',
                    '& fieldset': {
                      borderColor: 'var(--border-input)',
                    },
                    '&:hover fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                    '&.Mui-focused fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                  },
                  '& .MuiInputLabel-root.Mui-focused': {
                    color: 'var(--wood-brown)',
                  },
                }}
              />

              <TextField
                fullWidth
                label="Confirmar Senha"
                type={showConfirmPassword ? 'text' : 'password'}
                value={formData.confirmPassword}
                onChange={handleInputChange('confirmPassword')}
                error={!!errors.confirmPassword}
                helperText={errors.confirmPassword}
                required
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton onClick={() => setShowConfirmPassword(!showConfirmPassword)} edge="end">
                        {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
                sx={{
                  '& .MuiOutlinedInput-root': {
                    backgroundColor: 'var(--background-input)',
                    '& fieldset': {
                      borderColor: 'var(--border-input)',
                    },
                    '&:hover fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                    '&.Mui-focused fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                  },
                  '& .MuiInputLabel-root.Mui-focused': {
                    color: 'var(--wood-brown)',
                  },
                }}
              />

              {/* User Type Select */}
              <FormControl
                fullWidth
                error={!!errors.userType}
                required
                sx={{
                  '& .MuiOutlinedInput-root': {
                    backgroundColor: 'var(--background-input)',
                    '& fieldset': {
                      borderColor: 'var(--border-input)',
                    },
                    '&:hover fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                    '&.Mui-focused fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                  },
                  '& .MuiInputLabel-root.Mui-focused': {
                    color: 'var(--wood-brown)',
                  },
                }}
              >
                <InputLabel>Tipo de Usuário</InputLabel>
                <Select value={formData.userType} label="Tipo de Usuário" onChange={handleSelectChange('userType')}>
                  <MenuItem value="CUSTOMER">Cliente</MenuItem>
                  <MenuItem value="STUDIO_OWNER">Proprietário de Estúdio</MenuItem>
                </Select>
                {errors.userType && <FormHelperText>{errors.userType}</FormHelperText>}
              </FormControl>

              {/* Phone Number */}
              <TextField
                fullWidth
                label="Número de Telefone"
                value={formData.phoneNumber}
                onChange={handleInputChange('phoneNumber')}
                error={!!errors.phoneNumber}
                helperText={errors.phoneNumber}
                required
                sx={{
                  '& .MuiOutlinedInput-root': {
                    backgroundColor: 'var(--background-input)',
                    '& fieldset': {
                      borderColor: 'var(--border-input)',
                    },
                    '&:hover fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                    '&.Mui-focused fieldset': {
                      borderColor: 'var(--gold-primary)',
                    },
                  },
                  '& .MuiInputLabel-root.Mui-focused': {
                    color: 'var(--wood-brown)',
                  },
                }}
              />

              {/* Submit Button */}
              <Button
                type="submit"
                fullWidth
                variant="contained"
                disabled={isLoading}
                sx={{
                  mt: 2,
                  py: 1.5,
                  backgroundColor: 'var(--gold-primary)',
                  color: 'var(--text-button-primary)',
                  fontSize: '1.1rem',
                  fontWeight: 600,
                  textTransform: 'none',
                  borderRadius: 2,
                  '&:hover': {
                    backgroundColor: 'var(--gold-dark)',
                  },
                  '&:disabled': {
                    backgroundColor: 'var(--disabled)',
                    color: 'white',
                  },
                }}
              >
                {isLoading ? <CircularProgress size={24} sx={{ color: 'white' }} /> : 'Registrar'}
              </Button>
            </Box>

            {/* Login Link */}
            <Box sx={{ mt: 3, textAlign: 'center' }}>
              <Typography variant="body2" sx={{ color: 'var(--text-base)' }}>
                Já tem uma conta?{' '}
                <Link
                  to="/login"
                  style={{
                    color: 'var(--gold-primary)',
                    textDecoration: 'none',
                    fontWeight: 600,
                  }}
                >
                  Faça login
                </Link>
              </Typography>
            </Box>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default RegisterPage;
