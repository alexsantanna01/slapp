import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Box, Button, Card, CardContent, Container, Typography, IconButton, Chip, Avatar } from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as ViewIcon,
  Dashboard as DashboardIcon,
  MusicNote as MusicNoteIcon,
  AttachMoney as MoneyIcon,
  EventNote as EventIcon,
} from '@mui/icons-material';
import { styled } from '@mui/system';
import { Link } from 'react-router-dom';
import { getEntities as getStudios, createEntity as createStudio } from 'app/entities/studio/studio.reducer';
import CreateStudioWizard from 'app/modules/studio/create-studio-wizard';

const StyledContainer = styled(Container)(({ theme }) => ({
  minHeight: '100vh',
  backgroundColor: 'var(--off-beige)',
  padding: '2rem 0',
}));

const HeaderSection = styled(Box)(({ theme }) => ({
  background: `linear-gradient(135deg, var(--gold-primary) 0%, var(--gold-dark) 100%)`,
  borderRadius: '15px',
  padding: '2rem',
  marginBottom: '2rem',
  color: 'var(--text-button-primary)',
  boxShadow: '0 10px 30px rgba(0, 0, 0, 0.1)',
}));

const StatsCard = styled(Card)(({ theme }) => ({
  background: 'var(--background-card)',
  borderRadius: '12px',
  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.05)',
  border: '1px solid var(--border-input)',
  transition: 'all 0.3s ease',
  '&:hover': {
    transform: 'translateY(-2px)',
    boxShadow: '0 8px 25px rgba(0, 0, 0, 0.1)',
  },
}));

const StudioCard = styled(Card)(({ theme }) => ({
  background: 'var(--background-card)',
  borderRadius: '15px',
  boxShadow: '0 4px 15px rgba(0, 0, 0, 0.08)',
  border: '1px solid var(--border-input)',
  transition: 'all 0.3s ease',
  '&:hover': {
    transform: 'translateY(-2px)',
    boxShadow: '0 8px 25px rgba(0, 0, 0, 0.1)',
  },
}));

const ActionButton = styled(Button)(({ theme }) => ({
  backgroundColor: 'var(--gold-primary)',
  color: 'var(--text-button-primary)',
  borderRadius: '8px',
  padding: '0.5rem 1rem',
  fontWeight: 600,
  '&:hover': {
    backgroundColor: 'var(--gold-dark)',
    transform: 'translateY(-1px)',
  },
}));

export const StudioOwnerHome = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const studios = useAppSelector(state => state.studio.entities);
  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const loading = useAppSelector(state => state.studio.loading);

  const [userProfile, setUserProfile] = useState(null);
  const [ownedStudios, setOwnedStudios] = useState([]);
  const [createWizardOpen, setCreateWizardOpen] = useState(false);

  useEffect(() => {
    if (account?.id) {
      dispatch(getStudios({}));
    }
  }, [dispatch, account]);

  useEffect(() => {
    if (account?.id && userProfiles.length > 0) {
      const profile = userProfiles.find(p => p.user?.id === account.id);
      setUserProfile(profile);
    }
  }, [account, userProfiles]);

  useEffect(() => {
    if (userProfile?.id && studios.length > 0) {
      const owned = studios.filter(studio => studio.owner?.id === userProfile.id);
      setOwnedStudios(owned);
    }
  }, [userProfile, studios]);

  const handleWizardComplete = () => {
    // Refresh the studios list after wizard completion
    dispatch(getStudios({}));
  };

  const mockStats = {
    totalStudios: ownedStudios.length,
    totalReservations: Math.floor(Math.random() * 100) + 50,
    monthlyRevenue: (Math.random() * 5000 + 2000).toFixed(2),
    occupancyRate: Math.floor(Math.random() * 40) + 60,
  };

  return (
    <StyledContainer maxWidth="lg">
      {/* Header Section */}
      <HeaderSection>
        <Box display="flex" alignItems="center" justifyContent="space-between" flexWrap="wrap" gap={2}>
          <Box flex={1} minWidth="300px">
            <Typography variant="h3" fontWeight="bold" gutterBottom>
              Bem-vindo, {account?.firstName || 'Proprietário'}!
            </Typography>
            <Typography variant="h6" sx={{ opacity: 0.9 }}>
              Gerencie seus estúdios e acompanhe o desempenho do seu negócio
            </Typography>
          </Box>
          <Box>
            <ActionButton variant="contained" size="large" startIcon={<AddIcon />} onClick={() => setCreateWizardOpen(true)}>
              Novo Estúdio
            </ActionButton>
          </Box>
        </Box>
      </HeaderSection>

      {/* Dashboard Stats */}
      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' }, gap: 3, mb: 4 }}>
        <Box>
          <StatsCard>
            <CardContent sx={{ textAlign: 'center' }}>
              <Avatar
                sx={{
                  bgcolor: 'var(--gold-primary)',
                  width: 56,
                  height: 56,
                  margin: '0 auto 1rem',
                }}
              >
                <MusicNoteIcon fontSize="large" />
              </Avatar>
              <Typography variant="h4" fontWeight="bold" color="var(--wood-brown)">
                {mockStats.totalStudios}
              </Typography>
              <Typography variant="body2" color="var(--wood-light)">
                Estúdios Ativos
              </Typography>
            </CardContent>
          </StatsCard>
        </Box>

        <Box>
          <StatsCard>
            <CardContent sx={{ textAlign: 'center' }}>
              <Avatar
                sx={{
                  bgcolor: 'var(--wood-brown)',
                  width: 56,
                  height: 56,
                  margin: '0 auto 1rem',
                }}
              >
                <EventIcon fontSize="large" />
              </Avatar>
              <Typography variant="h4" fontWeight="bold" color="var(--wood-brown)">
                {mockStats.totalReservations}
              </Typography>
              <Typography variant="body2" color="var(--wood-light)">
                Reservas Este Mês
              </Typography>
            </CardContent>
          </StatsCard>
        </Box>

        <Box>
          <StatsCard>
            <CardContent sx={{ textAlign: 'center' }}>
              <Avatar
                sx={{
                  bgcolor: 'var(--gold-dark)',
                  width: 56,
                  height: 56,
                  margin: '0 auto 1rem',
                }}
              >
                <MoneyIcon fontSize="large" />
              </Avatar>
              <Typography variant="h4" fontWeight="bold" color="var(--wood-brown)">
                R$ {mockStats.monthlyRevenue}
              </Typography>
              <Typography variant="body2" color="var(--wood-light)">
                Receita Mensal
              </Typography>
            </CardContent>
          </StatsCard>
        </Box>

        <Box>
          <StatsCard>
            <CardContent sx={{ textAlign: 'center' }}>
              <Avatar
                sx={{
                  bgcolor: 'var(--wood-light)',
                  width: 56,
                  height: 56,
                  margin: '0 auto 1rem',
                }}
              >
                <DashboardIcon fontSize="large" />
              </Avatar>
              <Typography variant="h4" fontWeight="bold" color="var(--wood-brown)">
                {mockStats.occupancyRate}%
              </Typography>
              <Typography variant="body2" color="var(--wood-light)">
                Taxa de Ocupação
              </Typography>
            </CardContent>
          </StatsCard>
        </Box>
      </Box>

      {/* Studios Section */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" fontWeight="bold" sx={{ mb: 3, color: 'var(--wood-brown)' }}>
          Meus Estúdios
        </Typography>

        {loading ? (
          <Box display="flex" justifyContent="center" p={4}>
            <Typography>Carregando estúdios...</Typography>
          </Box>
        ) : ownedStudios.length === 0 ? (
          <StudioCard>
            <CardContent sx={{ textAlign: 'center', py: 6 }}>
              <MusicNoteIcon sx={{ fontSize: 64, color: 'var(--disabled)', mb: 2 }} />
              <Typography variant="h6" gutterBottom color="var(--wood-light)">
                Você ainda não possui estúdios cadastrados
              </Typography>
              <Typography variant="body2" color="var(--disabled)" sx={{ mb: 3 }}>
                Comece criando seu primeiro estúdio e comece a receber reservas!
              </Typography>
              <ActionButton variant="contained" startIcon={<AddIcon />} onClick={() => setCreateWizardOpen(true)}>
                Criar Primeiro Estúdio
              </ActionButton>
            </CardContent>
          </StudioCard>
        ) : (
          <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'repeat(2, 1fr)', lg: 'repeat(3, 1fr)' }, gap: 3 }}>
            {ownedStudios.map(studio => (
              <Box key={studio.id}>
                <StudioCard>
                  <CardContent>
                    <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={2}>
                      <Typography variant="h6" fontWeight="bold" color="var(--wood-brown)">
                        {studio.name}
                      </Typography>
                      <Chip
                        label="Ativo"
                        size="small"
                        sx={{
                          backgroundColor: 'var(--gold-primary)',
                          color: 'var(--text-button-primary)',
                        }}
                      />
                    </Box>

                    <Typography variant="body2" color="var(--wood-light)" sx={{ mb: 2 }}>
                      {studio.description || 'Sem descrição disponível'}
                    </Typography>

                    <Typography variant="caption" color="var(--disabled)" sx={{ mb: 3, display: 'block' }}>
                      📍 {studio.address}, {studio.city}
                    </Typography>

                    <Box display="flex" gap={1}>
                      <IconButton component={Link} to={`/studio/${studio.id}`} size="small" sx={{ color: 'var(--gold-primary)' }}>
                        <ViewIcon />
                      </IconButton>
                      <IconButton component={Link} to={`/studio/${studio.id}/edit`} size="small" sx={{ color: 'var(--wood-brown)' }}>
                        <EditIcon />
                      </IconButton>
                      <IconButton
                        size="small"
                        sx={{ color: 'var(--disabled)' }}
                        onClick={() => {
                          // TODO: Implement delete functionality
                          // eslint-disable-next-line no-console
                          console.log('Delete studio:', studio.id);
                        }}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Box>
                  </CardContent>
                </StudioCard>
              </Box>
            ))}
          </Box>
        )}
      </Box>

      {/* Create Studio Wizard */}
      <CreateStudioWizard
        open={createWizardOpen}
        onClose={() => {
          setCreateWizardOpen(false);
          handleWizardComplete();
        }}
        userProfile={userProfile}
      />
    </StyledContainer>
  );
};

export default StudioOwnerHome;
