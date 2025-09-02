import React from 'react';
import { Box, TextField, Typography, InputAdornment } from '@mui/material';
import {
  Business as BusinessIcon,
  Description as DescriptionIcon,
  LocationOn as LocationIcon,
  Phone as PhoneIcon,
  Email as EmailIcon,
  Language as WebsiteIcon,
  Image as ImageIcon,
} from '@mui/icons-material';

interface StudioFormData {
  name: string;
  description: string;
  address: string;
  city: string;
  state: string;
  zipCode: string;
  phone: string;
  email: string;
  website: string;
  image: string;
}

interface StudioInfoFormProps {
  data: StudioFormData;
  onChange: (data: StudioFormData) => void;
}

const StudioInfoForm: React.FC<StudioInfoFormProps> = ({ data, onChange }) => {
  const handleChange = (field: keyof StudioFormData) => (event: React.ChangeEvent<HTMLInputElement>) => {
    onChange({
      ...data,
      [field]: event.target.value,
    });
  };

  const textFieldStyle = {
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
  };

  return (
    <Box>
      <Typography variant="h6" gutterBottom sx={{ color: 'var(--wood-brown)', mb: 3, fontWeight: 600 }}>
        Informações Básicas do Estúdio
      </Typography>

      <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(12, 1fr)', gap: 3 }}>
        {/* Nome do Estúdio */}
        <Box sx={{ gridColumn: 'span 12' }}>
          <TextField
            fullWidth
            label="Nome do Estúdio"
            required
            value={data.name}
            onChange={handleChange('name')}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <BusinessIcon sx={{ color: 'var(--gold-primary)' }} />
                </InputAdornment>
              ),
            }}
            sx={textFieldStyle}
          />
        </Box>

        {/* Descrição */}
        <Box sx={{ gridColumn: 'span 12' }}>
          <TextField
            fullWidth
            label="Descrição"
            multiline
            rows={4}
            value={data.description}
            onChange={handleChange('description')}
            placeholder="Descreva seu estúdio, equipamentos disponíveis, ambiente..."
            InputProps={{
              startAdornment: (
                <InputAdornment position="start" sx={{ alignSelf: 'flex-start', mt: 1 }}>
                  <DescriptionIcon sx={{ color: 'var(--gold-primary)' }} />
                </InputAdornment>
              ),
            }}
            sx={textFieldStyle}
          />
        </Box>

        {/* Endereço */}
        <Box sx={{ gridColumn: 'span 12' }}>
          <TextField
            fullWidth
            label="Endereço Completo"
            required
            value={data.address}
            onChange={handleChange('address')}
            placeholder="Rua, número, bairro"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <LocationIcon sx={{ color: 'var(--gold-primary)' }} />
                </InputAdornment>
              ),
            }}
            sx={textFieldStyle}
          />
        </Box>

        {/* Cidade, Estado e CEP */}
        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 5' } }}>
          <TextField fullWidth label="Cidade" required value={data.city} onChange={handleChange('city')} sx={textFieldStyle} />
        </Box>

        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 4' } }}>
          <TextField
            fullWidth
            label="Estado"
            value={data.state}
            onChange={handleChange('state')}
            placeholder="Ex: SP, RJ, MG..."
            sx={textFieldStyle}
          />
        </Box>

        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 3' } }}>
          <TextField
            fullWidth
            label="CEP"
            value={data.zipCode}
            onChange={handleChange('zipCode')}
            placeholder="00000-000"
            sx={textFieldStyle}
          />
        </Box>

        {/* Telefone e Email */}
        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
          <TextField
            fullWidth
            label="Telefone"
            value={data.phone}
            onChange={handleChange('phone')}
            placeholder="(11) 99999-9999"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <PhoneIcon sx={{ color: 'var(--gold-primary)' }} />
                </InputAdornment>
              ),
            }}
            sx={textFieldStyle}
          />
        </Box>

        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
          <TextField
            fullWidth
            label="Email"
            type="email"
            value={data.email}
            onChange={handleChange('email')}
            placeholder="contato@estudioxyz.com"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <EmailIcon sx={{ color: 'var(--gold-primary)' }} />
                </InputAdornment>
              ),
            }}
            sx={textFieldStyle}
          />
        </Box>

        {/* Website e Imagem */}
        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
          <TextField
            fullWidth
            label="Website"
            value={data.website}
            onChange={handleChange('website')}
            placeholder="https://www.estudioxyz.com"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <WebsiteIcon sx={{ color: 'var(--gold-primary)' }} />
                </InputAdornment>
              ),
            }}
            sx={textFieldStyle}
          />
        </Box>

        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
          <TextField
            fullWidth
            label="Link da Imagem Principal"
            value={data.image}
            onChange={handleChange('image')}
            placeholder="https://exemplo.com/imagem.jpg"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <ImageIcon sx={{ color: 'var(--gold-primary)' }} />
                </InputAdornment>
              ),
            }}
            sx={textFieldStyle}
          />
        </Box>
      </Box>

      {/* Preview da imagem se fornecida */}
      {data.image && (
        <Box sx={{ mt: 3, textAlign: 'center' }}>
          <Typography variant="body2" sx={{ mb: 2, color: 'var(--wood-light)' }}>
            Preview da Imagem:
          </Typography>
          <Box
            component="img"
            src={data.image}
            alt="Preview do estúdio"
            sx={{
              maxWidth: '100%',
              maxHeight: '200px',
              borderRadius: '8px',
              boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
              border: '1px solid var(--border-input)',
            }}
            onError={e => {
              (e.target as HTMLImageElement).style.display = 'none';
            }}
          />
        </Box>
      )}
    </Box>
  );
};

export default StudioInfoForm;
