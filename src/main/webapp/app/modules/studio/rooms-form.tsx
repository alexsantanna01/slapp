import React, { useState } from 'react';
import {
  Box,
  TextField,
  Typography,
  Button,
  Card,
  CardContent,
  IconButton,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormControlLabel,
  Checkbox,
  InputAdornment,
  Chip,
} from '@mui/material';
import {
  Add as AddIcon,
  Delete as DeleteIcon,
  MusicNote as MusicNoteIcon,
  AttachMoney as MoneyIcon,
  People as PeopleIcon,
  VolumeUp as SoundIcon,
  AcUnit as AcIcon,
} from '@mui/icons-material';
import { styled } from '@mui/system';

interface RoomFormData {
  name: string;
  description: string;
  hourlyRate: number;
  capacity: number;
  roomType: string;
  soundproofed?: boolean;
  airConditioning?: boolean;
}

interface RoomsFormProps {
  data: RoomFormData[];
  onChange: (data: RoomFormData[]) => void;
}

const RoomCard = styled(Card)(({ theme }) => ({
  backgroundColor: 'var(--background-card)',
  borderRadius: '12px',
  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.05)',
  border: '1px solid var(--border-input)',
  marginBottom: '1.5rem',
}));

const AddRoomButton = styled(Button)(({ theme }) => ({
  backgroundColor: 'transparent',
  color: 'var(--gold-primary)',
  border: '2px dashed var(--gold-primary)',
  borderRadius: '12px',
  padding: '1rem',
  textTransform: 'none',
  fontWeight: 600,
  minHeight: '80px',
  '&:hover': {
    backgroundColor: 'rgba(212, 160, 55, 0.05)',
    border: '2px dashed var(--gold-dark)',
  },
}));

const roomTypes = [
  { value: 'RECORDING', label: 'Gravação' },
  { value: 'REHEARSAL', label: 'Ensaio' },
  { value: 'LIVE', label: 'Live/Show' },
  { value: 'MIXING', label: 'Mixagem' },
  { value: 'MASTERING', label: 'Masterização' },
];

const RoomsForm: React.FC<RoomsFormProps> = ({ data, onChange }) => {
  const addRoom = () => {
    const newRoom: RoomFormData = {
      name: '',
      description: '',
      hourlyRate: 0,
      capacity: 1,
      roomType: '',
      soundproofed: false,
      airConditioning: false,
    };
    onChange([...data, newRoom]);
  };

  const removeRoom = (index: number) => {
    const newData = data.filter((_, i) => i !== index);
    onChange(newData);
  };

  const updateRoom = (index: number, field: keyof RoomFormData, value: any) => {
    const newData = [...data];
    newData[index] = { ...newData[index], [field]: value };
    onChange(newData);
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

  const getRoomTypeChipColor = (roomType: string) => {
    const colors: { [key: string]: string } = {
      RECORDING: '#d32f2f',
      REHEARSAL: '#1976d2',
      LIVE: '#388e3c',
      MIXING: '#f57c00',
      MASTERING: '#7b1fa2',
    };
    return colors[roomType] || 'var(--gold-primary)';
  };

  return (
    <Box>
      <Typography variant="h6" gutterBottom sx={{ color: 'var(--wood-brown)', mb: 3, fontWeight: 600 }}>
        Salas do Estúdio
      </Typography>

      <Typography variant="body2" sx={{ color: 'var(--wood-light)', mb: 3 }}>
        Cadastre as salas do seu estúdio com suas características e valores. É necessário cadastrar pelo menos uma sala.
      </Typography>

      {data.map((room, index) => (
        <RoomCard key={index}>
          <CardContent>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
              <Typography variant="h6" sx={{ color: 'var(--wood-brown)', fontWeight: 600 }}>
                Sala {index + 1}
                {room.roomType && (
                  <Chip
                    label={roomTypes.find(type => type.value === room.roomType)?.label}
                    size="small"
                    sx={{
                      ml: 2,
                      backgroundColor: getRoomTypeChipColor(room.roomType),
                      color: 'white',
                      fontWeight: 500,
                    }}
                  />
                )}
              </Typography>
              <IconButton onClick={() => removeRoom(index)} sx={{ color: 'var(--disabled)' }}>
                <DeleteIcon />
              </IconButton>
            </Box>

            <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(12, 1fr)', gap: 3 }}>
              {/* Nome da Sala */}
              <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                <TextField
                  fullWidth
                  label="Nome da Sala"
                  required
                  value={room.name}
                  onChange={e => updateRoom(index, 'name', e.target.value)}
                  placeholder="Ex: Sala A, Estúdio Principal..."
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <MusicNoteIcon sx={{ color: 'var(--gold-primary)' }} />
                      </InputAdornment>
                    ),
                  }}
                  sx={textFieldStyle}
                />
              </Box>

              {/* Tipo da Sala */}
              <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                <FormControl fullWidth required sx={textFieldStyle}>
                  <InputLabel>Tipo da Sala</InputLabel>
                  <Select value={room.roomType} label="Tipo da Sala" onChange={e => updateRoom(index, 'roomType', e.target.value)}>
                    {roomTypes.map(type => (
                      <MenuItem key={type.value} value={type.value}>
                        {type.label}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Box>

              {/* Valor por Hora */}
              <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                <TextField
                  fullWidth
                  label="Valor por Hora (R$)"
                  required
                  type="number"
                  value={room.hourlyRate || ''}
                  onChange={e => updateRoom(index, 'hourlyRate', parseFloat(e.target.value) || 0)}
                  inputProps={{ min: 0, step: 0.01 }}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <MoneyIcon sx={{ color: 'var(--gold-primary)' }} />
                      </InputAdornment>
                    ),
                  }}
                  sx={textFieldStyle}
                />
              </Box>

              {/* Capacidade */}
              <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 6' } }}>
                <TextField
                  fullWidth
                  label="Capacidade (pessoas)"
                  required
                  type="number"
                  value={room.capacity || ''}
                  onChange={e => updateRoom(index, 'capacity', parseInt(e.target.value, 10) || 1)}
                  inputProps={{ min: 1, max: 100 }}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <PeopleIcon sx={{ color: 'var(--gold-primary)' }} />
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
                  rows={3}
                  value={room.description}
                  onChange={e => updateRoom(index, 'description', e.target.value)}
                  placeholder="Descreva os equipamentos, características e diferenciais da sala..."
                  sx={textFieldStyle}
                />
              </Box>

              {/* Características Especiais */}
              <Box sx={{ gridColumn: 'span 12' }}>
                <Typography variant="body2" sx={{ color: 'var(--wood-brown)', mb: 1, fontWeight: 600 }}>
                  Características:
                </Typography>
                <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={room.soundproofed || false}
                        onChange={e => updateRoom(index, 'soundproofed', e.target.checked)}
                        sx={{
                          color: 'var(--gold-primary)',
                          '&.Mui-checked': {
                            color: 'var(--gold-primary)',
                          },
                        }}
                      />
                    }
                    label={
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <SoundIcon sx={{ color: 'var(--wood-brown)' }} />
                        Isolamento Acústico
                      </Box>
                    }
                  />
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={room.airConditioning || false}
                        onChange={e => updateRoom(index, 'airConditioning', e.target.checked)}
                        sx={{
                          color: 'var(--gold-primary)',
                          '&.Mui-checked': {
                            color: 'var(--gold-primary)',
                          },
                        }}
                      />
                    }
                    label={
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <AcIcon sx={{ color: 'var(--wood-brown)' }} />
                        Ar Condicionado
                      </Box>
                    }
                  />
                </Box>
              </Box>
            </Box>
          </CardContent>
        </RoomCard>
      ))}

      {/* Botão para Adicionar Nova Sala */}
      <AddRoomButton fullWidth variant="outlined" startIcon={<AddIcon />} onClick={addRoom}>
        Adicionar Nova Sala
      </AddRoomButton>

      {data.length === 0 && (
        <Typography variant="body2" sx={{ textAlign: 'center', color: 'var(--wood-light)', mt: 2 }}>
          Adicione pelo menos uma sala para continuar
        </Typography>
      )}
    </Box>
  );
};

export default RoomsForm;
