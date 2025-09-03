import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Col, Row, Card, CardBody, Badge, Button as ButtonBooststrap } from 'reactstrap';
import { TextFormat, Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { toast } from 'react-toastify';

import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useCurrentUserProfile } from 'app/shared/util/user-profile-util';

import { getEntity } from './studio.reducer';
import { createEntity as createReservation, reset as resetReservation } from '../reservation/reservation.reducer';
import { ReservationStatus } from 'app/shared/model/enumerations/reservation-status.model';
import ReservationCalendar from './components/ReservationCalendar';
import PendingReservations from './components/PendingReservations';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import styled from '@emotion/styled';
import { Button } from '@mui/material';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import EditIcon from '@mui/icons-material/Edit';

const ReservationButton = styled(Button)(({ theme }) => ({
  backgroundColor: 'var(--gold-primary) !important',
  color: 'var(--text-button-primary) !important',
  borderRadius: '8px',
  border: 'none',
  padding: '0.5rem 1rem',
  fontWeight: 600,
  '&:hover': {
    backgroundColor: 'var(--gold-dark) !important',
    transform: 'translateY(-1px)',
  },
}));

export const StudioDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const studioEntity = useAppSelector(state => state.studio.entity);
  const accoutntEntity = useAppSelector(state => state.authentication.account);
  const reservationUpdateSuccess = useAppSelector(state => state.reservation.updateSuccess);
  const { userProfile } = useCurrentUserProfile();

  const isStudioOwner = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.STUDIO_OWNER]));
  const isOwnerOfThisStudio = userProfile && studioEntity?.owner?.id === userProfile.id;
  const [reservationModalOpen, setReservationModalOpen] = useState(false);
  const [selectedRoom, setSelectedRoom] = useState<any>(null);

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  useEffect(() => {
    if (reservationUpdateSuccess) {
      setReservationModalOpen(false);
      setSelectedRoom(null);
      // dispatch(resetReservation());
    }
  }, [reservationUpdateSuccess]);

  const handleReserveRoom = (room: any) => {
    if (!accoutntEntity) {
      toast.error('Você precisa estar logado para fazer uma reserva');
      return;
    }
    setSelectedRoom(room);
    setReservationModalOpen(true);
  };

  const handleReservationConfirm = (reservationData: any) => {
    try {
      const reservation = {
        startDateTime: reservationData.startDateTime,
        endDateTime: reservationData.endDateTime,
        totalPrice: reservationData.totalPrice,
        status: ReservationStatus.PENDING,
        notes: reservationData.notes,
        artistName: reservationData.artistName,
        instruments: reservationData.instruments,
        customer: { id: accoutntEntity.id },
        room: { id: reservationData.roomId },
      };

      dispatch(createReservation(reservation));
    } catch (error) {
      toast.error('Erro ao criar reserva');
      console.error('Error creating reservation:', error);
    }
  };

  const toggleReservationModal = () => {
    setReservationModalOpen(!reservationModalOpen);
    if (reservationModalOpen) {
      setSelectedRoom(null);
    }
  };

  if (!studioEntity) {
    return <div>Loading...</div>;
  }

  function handleGoBack(): void {
    navigate(-1);
  }

  // eslint-disable-next-line @typescript-eslint/no-shadow
  function handleEdit(id: any): void {
    navigate(`/studio/${id}/edit`);
  }

  return (
    <div style={{ backgroundColor: 'var(--off-beige)', minHeight: '100vh' }}>
      {/* Banner do Studio */}
      <div
        className="banner-detail"
        style={{ backgroundImage: `linear-gradient(rgba(77, 52, 36, 0.6), rgba(77, 52, 36, 0.6)), url(${studioEntity.image})` }}
      >
        <div>
          <h1 style={{ fontSize: '3.5rem', fontWeight: 'bold', marginBottom: '1rem', textShadow: '2px 2px 4px rgba(0,0,0,0.7)' }}>
            {studioEntity.name}
          </h1>
          <p style={{ fontSize: '1.3rem', maxWidth: '600px', textShadow: '1px 1px 2px rgba(0,0,0,0.7)' }}>{studioEntity.description}</p>
        </div>
      </div>

      <div style={{ padding: '2rem 0' }}>
        <Row>
          <Col md="12">
            {/* Informações do Studio */}
            <Card
              style={{
                backgroundColor: 'var(--background-card)',
                border: '1px solid var(--border-input)',
                marginBottom: '2rem',
                boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
              }}
            >
              <CardBody>
                <h3
                  style={{
                    color: 'var(--gold-dark)',
                    marginBottom: '1.5rem',
                    borderBottom: '2px solid var(--gold-primary)',
                    paddingBottom: '0.5rem',
                  }}
                >
                  <FontAwesomeIcon icon="info-circle" /> Informações do Estúdio
                </h3>

                <Row>
                  <Col md="6">
                    <div style={{ marginBottom: '1rem' }}>
                      <strong style={{ color: 'var(--wood-brown)' }}>
                        <FontAwesomeIcon icon="map-marker-alt" /> Endereço:
                      </strong>
                      <p style={{ margin: '0.25rem 0', color: 'var(--text-base)' }}>
                        {studioEntity.address}
                        <br />
                        {studioEntity.city}, {studioEntity.state} - {studioEntity.zipCode}
                      </p>
                    </div>

                    <div style={{ marginBottom: '1rem' }}>
                      <strong style={{ color: 'var(--wood-brown)' }}>
                        <FontAwesomeIcon icon="phone" /> Telefone:
                      </strong>
                      <p style={{ margin: '0.25rem 0', color: 'var(--text-base)' }}>{studioEntity.phone}</p>
                    </div>
                  </Col>

                  <Col md="6">
                    <div style={{ marginBottom: '1rem' }}>
                      <strong style={{ color: 'var(--wood-brown)' }}>
                        <FontAwesomeIcon icon="envelope" /> Email:
                      </strong>
                      <p style={{ margin: '0.25rem 0', color: 'var(--text-base)' }}>
                        <a href={`mailto:${studioEntity.email}`} style={{ color: 'var(--gold-primary)', textDecoration: 'none' }}>
                          {studioEntity.email}
                        </a>
                      </p>
                    </div>

                    <div style={{ marginBottom: '1rem' }}>
                      <strong style={{ color: 'var(--wood-brown)' }}>
                        <FontAwesomeIcon icon="globe" /> Website:
                      </strong>
                      <p style={{ margin: '0.25rem 0', color: 'var(--text-base)' }}>
                        <a
                          href={`http://${studioEntity.website}`}
                          target="_blank"
                          rel="noopener noreferrer"
                          style={{ color: 'var(--gold-primary)', textDecoration: 'none' }}
                        >
                          {studioEntity.website}
                        </a>
                      </p>
                    </div>
                  </Col>
                </Row>
              </CardBody>
            </Card>

            {/* Salas do Studio */}
            {studioEntity.rooms && studioEntity.rooms.length > 0 && (
              <Card
                style={{
                  backgroundColor: 'var(--background-card)',
                  border: '1px solid var(--border-input)',
                  boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
                }}
              >
                <CardBody>
                  <h3
                    style={{
                      color: 'var(--gold-dark)',
                      marginBottom: '1.5rem',
                      borderBottom: '2px solid var(--gold-primary)',
                      paddingBottom: '0.5rem',
                    }}
                  >
                    <FontAwesomeIcon icon="door-open" /> Salas Disponíveis ({studioEntity.rooms.length})
                  </h3>

                  <Row>
                    {studioEntity.rooms.map((room, index) => (
                      <Col md="6" lg="4" key={room.id} style={{ marginBottom: '2rem' }}>
                        <Card className="studio-card h-100 shadow-sm">
                          {/* Carousel de Imagens da Sala */}
                          {room.roomImages && room.roomImages.length > 0 && (
                            <div
                              style={{
                                height: '200px',
                                overflow: 'hidden',
                                position: 'relative',
                                backgroundColor: 'var(--wood-light)',
                              }}
                            >
                              <img
                                src={room.roomImages[0].url}
                                alt={room.roomImages[0].altText || room.name}
                                style={{
                                  width: '100%',
                                  height: '100%',
                                  objectFit: 'cover',
                                }}
                              />
                              {room.roomImages.length > 1 && (
                                <Badge
                                  color="dark"
                                  style={{
                                    position: 'absolute',
                                    top: '10px',
                                    right: '10px',
                                    backgroundColor: 'rgba(0,0,0,0.7)',
                                  }}
                                >
                                  +{room.roomImages.length - 1} fotos
                                </Badge>
                              )}
                            </div>
                          )}

                          <CardBody className="d-flex flex-column">
                            <h5 style={{ color: 'var(--wood-brown)', fontWeight: 'bold', marginBottom: '0.8rem' }}>{room.name}</h5>

                            <p style={{ color: 'var(--text-base)', fontSize: '0.9rem', marginBottom: '1rem' }}>{room.description}</p>

                            <div style={{ marginBottom: '1rem' }}>
                              <div
                                style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}
                              >
                                <span style={{ fontWeight: 'bold', color: 'var(--gold-dark)', fontSize: '1.1rem' }}>
                                  R$ {room.hourlyRate?.toFixed(2)}/hora
                                </span>
                                <Badge
                                  color={room.roomType === 'REHEARSAL' ? 'info' : 'secondary'}
                                  style={{ backgroundColor: room.roomType === 'REHEARSAL' ? 'var(--gold-primary)' : 'var(--wood-light)' }}
                                >
                                  {room.roomType === 'REHEARSAL' ? 'Ensaio' : room.roomType}
                                </Badge>
                              </div>

                              <small style={{ color: 'var(--wood-light)' }}>
                                <FontAwesomeIcon icon="users" /> Capacidade: {room.capacity} pessoas
                              </small>
                            </div>

                            <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', marginBottom: '1rem' }}>
                              {room.soundproofed && (
                                <Badge style={{ backgroundColor: 'var(--wood-light)', color: 'white', fontSize: '0.75rem' }}>
                                  <FontAwesomeIcon icon="volume-mute" /> Isolada
                                </Badge>
                              )}
                              {room.airConditioning && (
                                <Badge style={{ backgroundColor: 'var(--wood-light)', color: 'white', fontSize: '0.75rem' }}>
                                  <FontAwesomeIcon icon="snowflake" /> Ar Cond.
                                </Badge>
                              )}
                              {room.active && (
                                <Badge style={{ backgroundColor: 'var(--gold-primary)', color: 'white', fontSize: '0.75rem' }}>
                                  <FontAwesomeIcon icon="check" /> Disponível
                                </Badge>
                              )}
                            </div>

                            {/* Botão de Reserva */}
                            {room.active && !isOwnerOfThisStudio && accoutntEntity && (
                              <div className="mt-auto">
                                <ReservationButton onClick={() => handleReserveRoom(room)} startIcon={<CalendarMonthIcon />}>
                                  Reservar
                                </ReservationButton>
                              </div>
                            )}
                          </CardBody>
                        </Card>
                      </Col>
                    ))}
                  </Row>
                  {/* Botões de Ação */}
                  <Row className="d-flex justify-content-between" style={{ marginTop: '1rem' }}>
                    <Button variant="contained" onClick={handleGoBack} startIcon={<ArrowBackIcon />} className="button-slapp-voltar">
                      <span className="d-none d-md-inline">
                        <Translate contentKey="entity.action.back">Voltar</Translate>
                      </span>
                    </Button>
                    {isOwnerOfThisStudio && (
                      <Button
                        variant="contained"
                        onClick={e => handleEdit(studioEntity.id)}
                        startIcon={<EditIcon />}
                        className="button-slapp-editar"
                      >
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Editar</Translate>
                        </span>
                      </Button>
                    )}
                  </Row>
                </CardBody>
              </Card>
            )}

            {/* Reservas Pendentes - Apenas para proprietários do estúdio */}
            {isOwnerOfThisStudio && <PendingReservations studioId={id} isOwner={isOwnerOfThisStudio} />}
          </Col>
        </Row>
      </div>

      {/* Modal de Reserva */}
      {selectedRoom && (
        <ReservationCalendar
          isOpen={reservationModalOpen}
          toggle={toggleReservationModal}
          room={selectedRoom}
          onReservationConfirm={handleReservationConfirm}
        />
      )}
    </div>
  );
};

export default StudioDetail;
