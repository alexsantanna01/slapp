import React from 'react';
import {
  Box,
  TextField,
  Typography,
  Button,
  Card,
  CardContent,
  IconButton,
  InputAdornment,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Chip,
} from '@mui/material';
import {
  Add as AddIcon,
  Delete as DeleteIcon,
  Image as ImageIcon,
  ExpandMore as ExpandMoreIcon,
  PhotoLibrary as PhotoLibraryIcon,
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

interface RoomImageData {
  url: string;
  altText: string;
  displayOrder?: number;
}

interface RoomImagesFormProps {
  roomsData: RoomFormData[];
  imagesData: { [roomIndex: number]: RoomImageData[] };
  onChange: (imagesData: { [roomIndex: number]: RoomImageData[] }) => void;
}

const ImageCard = styled(Card)(({ theme }) => ({
  backgroundColor: 'var(--background-input)',
  borderRadius: '8px',
  boxShadow: '0 2px 8px rgba(0, 0, 0, 0.05)',
  border: '1px solid var(--border-input)',
  marginBottom: '1rem',
}));

const AddImageButton = styled(Button)(({ theme }) => ({
  backgroundColor: 'transparent',
  color: 'var(--gold-primary)',
  border: '1px dashed var(--gold-primary)',
  borderRadius: '8px',
  padding: '0.75rem',
  textTransform: 'none',
  fontWeight: 500,
  minHeight: '50px',
  '&:hover': {
    backgroundColor: 'rgba(212, 160, 55, 0.05)',
    border: '1px dashed var(--gold-dark)',
  },
}));

const roomTypes = [
  { value: 'RECORDING', label: 'Gravação' },
  { value: 'REHEARSAL', label: 'Ensaio' },
  { value: 'LIVE', label: 'Live/Show' },
  { value: 'MIXING', label: 'Mixagem' },
  { value: 'MASTERING', label: 'Masterização' },
];

const RoomImagesForm: React.FC<RoomImagesFormProps> = ({ roomsData, imagesData, onChange }) => {
  const addImage = (roomIndex: number) => {
    const newImage: RoomImageData = {
      url: '',
      altText: '',
      displayOrder: (imagesData[roomIndex]?.length || 0) + 1,
    };

    const newImagesData = {
      ...imagesData,
      [roomIndex]: [...(imagesData[roomIndex] || []), newImage],
    };
    onChange(newImagesData);
  };

  const removeImage = (roomIndex: number, imageIndex: number) => {
    const roomImages = imagesData[roomIndex] || [];
    const newImages = roomImages.filter((_, i) => i !== imageIndex);

    const newImagesData = {
      ...imagesData,
      [roomIndex]: newImages,
    };
    onChange(newImagesData);
  };

  const updateImage = (roomIndex: number, imageIndex: number, field: keyof RoomImageData, value: any) => {
    const roomImages = [...(imagesData[roomIndex] || [])];
    roomImages[imageIndex] = { ...roomImages[imageIndex], [field]: value };

    const newImagesData = {
      ...imagesData,
      [roomIndex]: roomImages,
    };
    onChange(newImagesData);
  };

  const textFieldStyle = {
    '& .MuiOutlinedInput-root': {
      backgroundColor: 'var(--background-card)',
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
      <Typography variant="h6" gutterBottom sx={{ color: 'var(--wood-brown)', mb: 2, fontWeight: 600 }}>
        Imagens das Salas
      </Typography>

      <Typography variant="body2" sx={{ color: 'var(--wood-light)', mb: 4 }}>
        Adicione imagens para cada sala do seu estúdio. As imagens ajudam os clientes a conhecer melhor o ambiente. Este passo é opcional,
        mas recomendado.
      </Typography>

      {roomsData.length === 0 ? (
        <Box sx={{ textAlign: 'center', py: 4 }}>
          <PhotoLibraryIcon sx={{ fontSize: 64, color: 'var(--disabled)', mb: 2 }} />
          <Typography variant="h6" color="var(--disabled)">
            Nenhuma sala cadastrada
          </Typography>
          <Typography variant="body2" color="var(--wood-light)">
            Volte ao passo anterior e cadastre pelo menos uma sala.
          </Typography>
        </Box>
      ) : (
        roomsData.map((room, roomIndex) => (
          <Accordion
            key={roomIndex}
            sx={{
              mb: 2,
              backgroundColor: 'var(--background-card)',
              border: '1px solid var(--border-input)',
              '&:before': {
                display: 'none',
              },
            }}
          >
            <AccordionSummary
              expandIcon={<ExpandMoreIcon sx={{ color: 'var(--gold-primary)' }} />}
              sx={{
                backgroundColor: 'var(--off-beige)',
                '& .MuiAccordionSummary-content': {
                  alignItems: 'center',
                },
              }}
            >
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, width: '100%' }}>
                <PhotoLibraryIcon sx={{ color: 'var(--gold-primary)' }} />
                <Typography variant="h6" sx={{ color: 'var(--wood-brown)', fontWeight: 600 }}>
                  {room.name || `Sala ${roomIndex + 1}`}
                </Typography>
                {room.roomType && (
                  <Chip
                    label={roomTypes.find(type => type.value === room.roomType)?.label}
                    size="small"
                    sx={{
                      backgroundColor: getRoomTypeChipColor(room.roomType),
                      color: 'white',
                      fontWeight: 500,
                    }}
                  />
                )}
                <Box sx={{ ml: 'auto' }}>
                  <Chip
                    label={`${(imagesData[roomIndex] || []).length} imagens`}
                    size="small"
                    variant="outlined"
                    sx={{
                      color: 'var(--wood-brown)',
                      borderColor: 'var(--border-input)',
                    }}
                  />
                </Box>
              </Box>
            </AccordionSummary>

            <AccordionDetails>
              <Box sx={{ p: 2 }}>
                {(imagesData[roomIndex] || []).map((image, imageIndex) => (
                  <ImageCard key={imageIndex}>
                    <CardContent>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                        <Typography variant="subtitle1" sx={{ color: 'var(--wood-brown)', fontWeight: 600 }}>
                          Imagem {imageIndex + 1}
                        </Typography>
                        <IconButton onClick={() => removeImage(roomIndex, imageIndex)} sx={{ color: 'var(--disabled)' }} size="small">
                          <DeleteIcon />
                        </IconButton>
                      </Box>

                      <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(12, 1fr)', gap: 2 }}>
                        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 8' } }}>
                          <TextField
                            fullWidth
                            label="URL da Imagem"
                            required
                            value={image.url}
                            onChange={e => updateImage(roomIndex, imageIndex, 'url', e.target.value)}
                            placeholder="https://exemplo.com/imagem-sala.jpg"
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

                        <Box sx={{ gridColumn: { xs: 'span 12', sm: 'span 4' } }}>
                          <TextField
                            fullWidth
                            label="Descrição da Imagem"
                            value={image.altText}
                            onChange={e => updateImage(roomIndex, imageIndex, 'altText', e.target.value)}
                            placeholder="Ex: Vista geral da sala"
                            sx={textFieldStyle}
                          />
                        </Box>
                      </Box>

                      {/* Preview da imagem */}
                      {image.url && (
                        <Box sx={{ mt: 2, textAlign: 'center' }}>
                          <Typography variant="caption" sx={{ color: 'var(--wood-light)', mb: 1, display: 'block' }}>
                            Preview:
                          </Typography>
                          <Box
                            component="img"
                            src={image.url}
                            alt={image.altText || 'Preview'}
                            sx={{
                              maxWidth: '100%',
                              maxHeight: '150px',
                              borderRadius: '6px',
                              boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
                              border: '1px solid var(--border-input)',
                            }}
                            onError={e => {
                              (e.target as HTMLImageElement).style.display = 'none';
                            }}
                          />
                        </Box>
                      )}
                    </CardContent>
                  </ImageCard>
                ))}

                {/* Botão para adicionar imagem */}
                <AddImageButton fullWidth variant="outlined" startIcon={<AddIcon />} onClick={() => addImage(roomIndex)}>
                  Adicionar Imagem
                </AddImageButton>
              </Box>
            </AccordionDetails>
          </Accordion>
        ))
      )}

      {roomsData.length > 0 && (
        <Box sx={{ mt: 4, p: 3, backgroundColor: 'var(--off-beige)', borderRadius: '8px', border: '1px solid var(--border-input)' }}>
          <Typography variant="body2" sx={{ color: 'var(--wood-brown)', fontWeight: 600, mb: 1 }}>
            💡 Dicas para as imagens:
          </Typography>
          <Typography variant="body2" sx={{ color: 'var(--wood-light)' }}>
            • Use imagens com boa iluminação e qualidade
            <br />
            • Mostre diferentes ângulos da sala (geral, equipamentos, detalhes)
            <br />
            • Inclua imagens dos equipamentos disponíveis
            <br />
            • Evite imagens muito escuras ou desfocadas
            <br />• Recomendamos pelo menos 2-3 imagens por sala
          </Typography>
        </Box>
      )}
    </Box>
  );
};

export default RoomImagesForm;
