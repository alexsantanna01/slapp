import React, { useState } from 'react';
import { Box, Stepper, Step, StepLabel, Button, Typography, Dialog, DialogTitle, DialogContent, DialogActions, Paper } from '@mui/material';
import { styled } from '@mui/system';
import { toast } from 'react-toastify';
import { useAppDispatch } from 'app/config/store';
import { createEntity as createStudio } from 'app/entities/studio/studio.reducer';
import { createEntity as createRoom } from 'app/entities/room/room.reducer';
import { createEntity as createRoomImage } from 'app/entities/room-image/room-image.reducer';
import dayjs from 'dayjs';

// Importar os componentes de cada step
import StudioInfoForm from './studio-info-form';
import RoomsForm from './rooms-form';
import RoomImagesForm from './room-images-form';

// Styled Components
const WizardContainer = styled(Paper)(({ theme }) => ({
  backgroundColor: 'var(--background-card)',
  borderRadius: '15px',
  padding: '2rem',
  boxShadow: '0 8px 32px rgba(77, 52, 36, 0.15)',
  border: '1px solid var(--border-input)',
}));

const StepContent = styled(Box)(({ theme }) => ({
  minHeight: '400px',
  padding: '2rem 0',
}));

const ActionButton = styled(Button)(({ theme }) => ({
  backgroundColor: 'var(--gold-primary)',
  color: 'var(--text-button-primary)',
  borderRadius: '8px',
  padding: '0.75rem 2rem',
  fontWeight: 600,
  '&:hover': {
    backgroundColor: 'var(--gold-dark)',
    transform: 'translateY(-1px)',
  },
  '&:disabled': {
    backgroundColor: 'var(--disabled)',
    color: 'white',
  },
}));

const steps = ['Informações do Estúdio', 'Cadastrar Salas', 'Imagens das Salas'];

interface CreateStudioWizardProps {
  open: boolean;
  onClose: () => void;
  userProfile: any;
}

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

export const CreateStudioWizard: React.FC<CreateStudioWizardProps> = ({ open, onClose, userProfile }) => {
  const dispatch = useAppDispatch();
  const [activeStep, setActiveStep] = useState(0);
  const [isLoading, setIsLoading] = useState(false);

  // Form Data States
  const [studioData, setStudioData] = useState<StudioFormData>({
    name: '',
    description: '',
    address: '',
    city: '',
    state: '',
    zipCode: '',
    phone: '',
    email: '',
    website: '',
    image: '',
  });

  const [roomsData, setRoomsData] = useState<RoomFormData[]>([]);
  const [roomImagesData, setRoomImagesData] = useState<{ [roomIndex: number]: RoomImageData[] }>({});

  const handleNext = () => {
    if (activeStep < steps.length - 1) {
      setActiveStep(prevActiveStep => prevActiveStep + 1);
    } else {
      handleSubmit();
    }
  };

  const handleBack = () => {
    setActiveStep(prevActiveStep => prevActiveStep - 1);
  };

  const handleReset = () => {
    setActiveStep(0);
    setStudioData({
      name: '',
      description: '',
      address: '',
      city: '',
      state: '',
      zipCode: '',
      phone: '',
      email: '',
      website: '',
      image: '',
    });
    setRoomsData([]);
    setRoomImagesData({});
  };

  const isStepValid = (): boolean => {
    switch (activeStep) {
      case 0:
        return studioData.name.trim() !== '' && studioData.address.trim() !== '' && studioData.city.trim() !== '';
      case 1:
        return (
          roomsData.length > 0 &&
          roomsData.every(room => room.name.trim() !== '' && room.hourlyRate > 0 && room.capacity > 0 && room.roomType !== '')
        );
      case 2:
        return true; // Imagens são opcionais
      default:
        return false;
    }
  };

  const handleSubmit = async () => {
    setIsLoading(true);
    try {
      // 1. Criar Studio
      const studioEntity = {
        name: studioData.name.trim(),
        description: studioData.description.trim() || null,
        address: studioData.address.trim(),
        city: studioData.city.trim(),
        state: studioData.state.trim() || null,
        zipCode: studioData.zipCode.trim() || null,
        phone: studioData.phone.trim() || null,
        email: studioData.email.trim() || null,
        website: studioData.website.trim() || null,
        image: studioData.image.trim() || null,
        active: true,
        owner: userProfile,
        createdAt: dayjs(),
        updatedAt: dayjs(),
      };

      // Criar Studio e aguardar resposta
      const studioResponse = await dispatch(createStudio(studioEntity));
      const createdStudio = studioResponse.payload;

      // 2. Para cada sala, criar Room
      const createdRooms = [];
      for (const roomData of roomsData) {
        // Extrair ID do studio de diferentes estruturas possíveis
        const studioId = (createdStudio as any)?.id || (createdStudio as any)?.data?.id;

        if (!studioId) {
          throw new Error('Studio ID não encontrado após criação');
        }

        const roomEntity = {
          name: roomData.name.trim(),
          description: roomData.description.trim() || null,
          hourlyRate: roomData.hourlyRate,
          capacity: roomData.capacity,
          roomType: roomData.roomType as 'RECORDING' | 'REHEARSAL' | 'LIVE' | 'MIXING' | 'MASTERING',
          soundproofed: roomData.soundproofed || false,
          airConditioning: roomData.airConditioning || false,
          active: true,
          studio: { id: studioId },
          createdAt: dayjs(),
          updatedAt: dayjs(),
        };

        const roomResponse = await dispatch(createRoom(roomEntity));
        const createdRoom = roomResponse.payload;
        createdRooms.push(createdRoom);
      }

      // 3. Para cada imagem de cada sala, criar RoomImage
      for (let roomIndex = 0; roomIndex < createdRooms.length; roomIndex++) {
        const room = createdRooms[roomIndex];
        const roomImages = roomImagesData[roomIndex] || [];

        for (const imageData of roomImages) {
          if (imageData.url.trim()) {
            // Extrair ID da room de diferentes estruturas possíveis
            const roomId = room?.id || room?.data?.id;

            if (!roomId) {
              continue;
            }

            const imageEntity = {
              url: imageData.url.trim(),
              altText: imageData.altText.trim() || null,
              displayOrder: imageData.displayOrder || 1,
              active: true,
              room: { id: roomId },
            };

            await dispatch(createRoomImage(imageEntity));
          }
        }
      }

      toast.success('Estúdio criado com sucesso!');
      handleReset();
      onClose();
    } catch (error) {
      toast.error('Erro ao criar estúdio. Tente novamente.');
      console.error('Error creating studio:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const getStepContent = (step: number) => {
    switch (step) {
      case 0:
        return <StudioInfoForm data={studioData} onChange={setStudioData} />;
      case 1:
        return <RoomsForm data={roomsData} onChange={setRoomsData} />;
      case 2:
        return <RoomImagesForm roomsData={roomsData} imagesData={roomImagesData} onChange={setRoomImagesData} />;
      default:
        return 'Etapa desconhecida';
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth disableEscapeKeyDown>
      <DialogTitle>
        <Typography variant="h4" fontWeight="bold" color="var(--wood-brown)" align="center">
          Criar Novo Estúdio
        </Typography>
      </DialogTitle>
      <DialogContent>
        <WizardContainer elevation={0}>
          <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
            {steps.map(label => (
              <Step key={label}>
                <StepLabel
                  sx={{
                    '& .MuiStepLabel-label.Mui-active': {
                      color: 'var(--gold-primary)',
                      fontWeight: 600,
                    },
                    '& .MuiStepLabel-label.Mui-completed': {
                      color: 'var(--wood-brown)',
                    },
                    '& .MuiStepIcon-root.Mui-active': {
                      color: 'var(--gold-primary)',
                    },
                    '& .MuiStepIcon-root.Mui-completed': {
                      color: 'var(--gold-primary)',
                    },
                  }}
                >
                  {label}
                </StepLabel>
              </Step>
            ))}
          </Stepper>

          <StepContent>{getStepContent(activeStep)}</StepContent>
        </WizardContainer>
      </DialogContent>
      <DialogActions sx={{ p: 3, justifyContent: 'space-between' }}>
        <Button
          onClick={onClose}
          color="inherit"
          sx={{
            color: 'var(--wood-light)',
            '&:hover': {
              backgroundColor: 'transparent',
              color: 'var(--wood-brown)',
            },
          }}
        >
          Cancelar
        </Button>

        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            disabled={activeStep === 0}
            onClick={handleBack}
            sx={{
              color: 'var(--wood-brown)',
              borderColor: 'var(--border-input)',
              '&:hover': {
                backgroundColor: 'var(--off-beige)',
              },
            }}
            variant="outlined"
          >
            Voltar
          </Button>

          <ActionButton variant="contained" onClick={handleNext} disabled={!isStepValid() || isLoading}>
            {activeStep === steps.length - 1 ? 'Finalizar' : 'Próximo'}
          </ActionButton>
        </Box>
      </DialogActions>
    </Dialog>
  );
};

export default CreateStudioWizard;
