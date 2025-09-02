import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  Button as MuiButton,
  Stepper,
  Step,
  StepLabel,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import { styled } from '@mui/system';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntities as getCancellationPolicies } from 'app/entities/cancellation-policy/cancellation-policy.reducer';
import { createEntity, getEntity, reset, updateEntity } from './studio.reducer';
import {
  getEntitiesByStudio as getRoomsByStudio,
  createEntity as createRoom,
  updateEntity as updateRoom,
  deleteEntity as deleteRoom,
} from 'app/entities/room/room.reducer';
import {
  getEntitiesByRoom as getRoomImagesByRoom,
  createEntity as createRoomImage,
  updateEntity as updateRoomImage,
  deleteEntity as deleteRoomImage,
} from 'app/entities/room-image/room-image.reducer';
import OperatingHoursManager from './components/OperatingHoursManager';
import StudioInfoForm from 'app/modules/studio/studio-info-form';
import RoomsForm from 'app/modules/studio/rooms-form';
import RoomImagesForm from 'app/modules/studio/room-images-form';
import { RoomType } from 'app/shared/model/enumerations/room-type.model';
import axios from 'axios';
import dayjs from 'dayjs';

interface OperatingHours {
  id?: number;
  dayOfWeek: string;
  startTime?: string;
  endTime?: string;
  isOpen: boolean;
  studioId?: number;
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
  id?: number;
  name: string;
  description: string;
  hourlyRate: number;
  capacity: number;
  roomType: string;
  soundproofed?: boolean;
  airConditioning?: boolean;
}

interface RoomImageData {
  id?: number;
  url: string;
  altText: string;
  displayOrder?: number;
}

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

const ActionButton = styled(MuiButton)(({ theme }) => ({
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

const steps = ['Informações do Estúdio', 'Gerenciar Salas', 'Imagens das Salas', 'Horários de Funcionamento'];

export const StudioUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const [activeStep, setActiveStep] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [operatingHours, setOperatingHours] = useState<OperatingHours[]>([]);
  const [operatingHoursSaving, setOperatingHoursSaving] = useState(false);

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
  const [originalRooms, setOriginalRooms] = useState<RoomFormData[]>([]);

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const cancellationPolicies = useAppSelector(state => state.cancellationPolicy.entities);
  const studioEntity = useAppSelector(state => state.studio.entity);
  const loading = useAppSelector(state => state.studio.loading);
  const updating = useAppSelector(state => state.studio.updating);
  const updateSuccess = useAppSelector(state => state.studio.updateSuccess);
  const rooms = useAppSelector(state => state.room.entities);
  const roomImages = useAppSelector(state => state.roomImage.entities);

  const handleClose = () => {
    navigate(`/studio${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getCancellationPolicies({}));
  }, []);

  useEffect(() => {
    if (!isNew && id) {
      dispatch(getRoomsByStudio(id));
    }
  }, [id, isNew]);

  useEffect(() => {
    if (studioEntity && !isNew) {
      setStudioData({
        name: studioEntity.name || '',
        description: studioEntity.description || '',
        address: studioEntity.address || '',
        city: studioEntity.city || '',
        state: studioEntity.state || '',
        zipCode: studioEntity.zipCode || '',
        phone: studioEntity.phone || '',
        email: studioEntity.email || '',
        website: studioEntity.website || '',
        image: studioEntity.image || '',
      });
    }
  }, [studioEntity, isNew]);

  useEffect(() => {
    if (rooms && !isNew) {
      const formattedRooms = rooms.map(room => ({
        id: room.id,
        name: room.name || '',
        description: room.description || '',
        hourlyRate: room.hourlyRate || 0,
        capacity: room.capacity || 1,
        roomType: room.roomType || '',
        soundproofed: room.soundproofed || false,
        airConditioning: room.airConditioning || false,
      }));
      setRoomsData(formattedRooms);
      setOriginalRooms([...formattedRooms]);
    }
  }, [rooms, isNew]);

  // Load room images when rooms are loaded
  useEffect(() => {
    if (roomsData.length > 0 && !isNew) {
      const loadRoomImages = async () => {
        const imagesByRoom: { [roomIndex: number]: RoomImageData[] } = {};

        for (let roomIndex = 0; roomIndex < roomsData.length; roomIndex++) {
          const room = roomsData[roomIndex];
          if (room.id) {
            try {
              const response = await dispatch(getRoomImagesByRoom(room.id)).unwrap();
              imagesByRoom[roomIndex] = response.data.map((img: any) => ({
                id: img.id,
                url: img.url || '',
                altText: img.altText || '',
                displayOrder: img.displayOrder || 1,
              }));
            } catch (error) {
              console.error(`Error loading images for room ${room.id}:`, error);
              imagesByRoom[roomIndex] = [];
            }
          }
        }

        setRoomImagesData(imagesByRoom);
      };

      loadRoomImages();
    }
  }, [roomsData, isNew, dispatch]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

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

  const isStepValid = (): boolean => {
    switch (activeStep) {
      case 0:
        return studioData.name.trim() !== '' && studioData.address.trim() !== '' && studioData.city.trim() !== '';
      case 1:
        return true; // Rooms are optional in edit mode
      case 2:
        return true; // Images are optional
      case 3:
        return true; // Operating hours are optional
      default:
        return false;
    }
  };

  const handleSubmit = async () => {
    setIsLoading(true);
    try {
      // 1. Update Studio
      const entity = {
        ...studioEntity,
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
        updatedAt: dayjs(),
      };

      let studioResult;
      if (isNew) {
        studioResult = await dispatch(createEntity(entity)).unwrap();
      } else {
        studioResult = await dispatch(updateEntity(entity)).unwrap();
      }

      const studioId = studioResult?.id || id;

      // 2. Handle Rooms
      await handleRoomsUpdate(studioId);

      // 3. Handle Room Images
      await handleRoomImagesUpdate();

      // 4. Save operating hours
      if (operatingHours.length > 0) {
        await saveOperatingHours(studioId);
      }

      toast.success('Estúdio atualizado com sucesso!');
      handleClose();
    } catch (error) {
      toast.error('Erro ao atualizar estúdio. Tente novamente.');
      console.error('Error updating studio:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRoomsUpdate = async (studioId: string | number) => {
    // Create/Update rooms
    const createdRooms = [];
    for (const [index, roomData] of roomsData.entries()) {
      const roomEntity: any = {
        id: roomData.id,
        name: roomData.name.trim(),
        description: roomData.description.trim() || null,
        hourlyRate: roomData.hourlyRate,
        capacity: roomData.capacity,
        roomType: roomData.roomType as keyof typeof RoomType,
        soundproofed: roomData.soundproofed || false,
        airConditioning: roomData.airConditioning || false,
        active: true,
        studio: { id: studioId },
        updatedAt: dayjs(),
      };

      let roomResult;
      if (roomData.id) {
        // Update existing room
        roomResult = await dispatch(updateRoom(roomEntity)).unwrap();
      } else {
        // Create new room
        roomEntity.createdAt = dayjs();
        roomResult = await dispatch(createRoom(roomEntity)).unwrap();
      }
      createdRooms.push(roomResult);
    }

    // Delete rooms that were removed
    const currentRoomIds = roomsData.map(room => room.id).filter(roomId => roomId);
    const originalRoomIds = originalRooms.map(room => room.id).filter(roomId => roomId);
    const roomsToDelete = originalRoomIds.filter(roomId => !currentRoomIds.includes(roomId));

    for (const roomId of roomsToDelete) {
      if (roomId) {
        await dispatch(deleteRoom(roomId));
      }
    }
  };

  const handleRoomImagesUpdate = async () => {
    // Handle room images for each room
    for (let roomIndex = 0; roomIndex < roomsData.length; roomIndex++) {
      const room = roomsData[roomIndex];
      const images = roomImagesData[roomIndex] || [];

      if (room.id) {
        // Create/Update images
        for (const imageData of images) {
          if (imageData.url.trim()) {
            const imageEntity = {
              id: imageData.id,
              url: imageData.url.trim(),
              altText: imageData.altText.trim() || null,
              displayOrder: imageData.displayOrder || 1,
              active: true,
              room: { id: room.id },
            };

            if (imageData.id) {
              // Update existing image
              await dispatch(updateRoomImage(imageEntity));
            } else {
              // Create new image
              await dispatch(createRoomImage(imageEntity));
            }
          }
        }
      }
    }
  };

  const saveOperatingHours = async (studioId: string | number) => {
    if (!studioId || operatingHours.length === 0) return;

    setOperatingHoursSaving(true);
    try {
      const hoursToSave = operatingHours.map(oh => ({
        ...oh,
        studioId: Number(studioId),
      }));

      await axios.put(`/api/studio-operating-hours/studio/${studioId}`, hoursToSave);
    } catch (error) {
      console.error('Error saving operating hours:', error);
    } finally {
      setOperatingHoursSaving(false);
    }
  };

  const handleCloseWizard = () => {
    navigate(-1);
  };

  const getStepContent = (step: number) => {
    switch (step) {
      case 0:
        return <StudioInfoForm data={studioData} onChange={setStudioData} />;
      case 1:
        return <RoomsForm data={roomsData} onChange={setRoomsData} />;
      case 2:
        return <RoomImagesForm roomsData={roomsData} imagesData={roomImagesData} onChange={setRoomImagesData} />;
      case 3:
        return (
          <Box>
            <Typography variant="h6" gutterBottom sx={{ color: 'var(--wood-brown)', mb: 3, fontWeight: 600 }}>
              Horários de Funcionamento
            </Typography>
            <OperatingHoursManager studioId={!isNew ? Number(id) : undefined} isNew={isNew} onOperatingHoursChange={setOperatingHours} />
          </Box>
        );
      default:
        return 'Etapa desconhecida';
    }
  };

  return (
    <Box>
      {/* Hero Section */}
      {!isNew && studioEntity && (
        <Box
          sx={{
            backgroundImage: `linear-gradient(rgba(77, 52, 36, 0.6), rgba(77, 52, 36, 0.6)), url(${studioEntity.image})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            height: '300px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: 'white',
            textAlign: 'center',
          }}
        >
          <Box>
            <Typography variant="h2" sx={{ fontWeight: 'bold', mb: 2, textShadow: '2px 2px 4px rgba(0,0,0,0.7)' }}>
              {studioEntity.name}
            </Typography>
            <Typography variant="h6" sx={{ maxWidth: '600px', textShadow: '1px 1px 2px rgba(0,0,0,0.7)' }}>
              {studioEntity.description}
            </Typography>
          </Box>
        </Box>
      )}

      {/* Main Content */}
      <Box sx={{ maxWidth: 1200, mx: 'auto', p: 4 }}>
        <Typography variant="h4" fontWeight="bold" color="var(--wood-brown)" align="center" sx={{ mb: 4 }}>
          {isNew ? 'Criar Novo Estúdio' : 'Editar Estúdio'}
        </Typography>

        {loading ? (
          <Box sx={{ textAlign: 'center', py: 4 }}>
            <Typography>Carregando...</Typography>
          </Box>
        ) : (
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

            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 4 }}>
              <MuiButton
                onClick={handleCloseWizard}
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
              </MuiButton>

              <Box sx={{ display: 'flex', gap: 2 }}>
                <MuiButton
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
                </MuiButton>

                <ActionButton variant="contained" onClick={handleNext} disabled={!isStepValid() || isLoading}>
                  {activeStep === steps.length - 1 ? (isNew ? 'Criar Estúdio' : 'Salvar Alterações') : 'Próximo'}
                </ActionButton>
              </Box>
            </Box>
          </WizardContainer>
        )}
      </Box>
    </Box>
  );
};

export default StudioUpdate;
