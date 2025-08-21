import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, Card, CardBody, Badge } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './studio.reducer';

export const StudioDetail = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studioEntity = useAppSelector(state => state.studio.entity);

  if (!studioEntity) {
    return <div>Loading...</div>;
  }

  return (
    <div style={{ backgroundColor: 'var(--off-beige)', minHeight: '100vh' }}>
      {/* Banner do Studio */}
      <div
        style={{
          backgroundImage: `linear-gradient(rgba(77, 52, 36, 0.6), rgba(77, 52, 36, 0.6)), url(${studioEntity.image})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          height: '400px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: 'white',
          textAlign: 'center',
        }}
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
                        <Card
                          style={{
                            height: '100%',
                            border: '1px solid var(--border-input)',
                            transition: 'transform 0.2s, box-shadow 0.2s',
                            cursor: 'pointer',
                          }}
                          onMouseEnter={e => {
                            e.currentTarget.style.transform = 'translateY(-5px)';
                            e.currentTarget.style.boxShadow = '0 6px 12px rgba(0,0,0,0.15)';
                          }}
                          onMouseLeave={e => {
                            e.currentTarget.style.transform = 'translateY(0)';
                            e.currentTarget.style.boxShadow = '0 2px 4px rgba(0,0,0,0.1)';
                          }}
                        >
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

                          <CardBody style={{ padding: '1.2rem' }}>
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

                            <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
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
                          </CardBody>
                        </Card>
                      </Col>
                    ))}
                  </Row>
                </CardBody>
              </Card>
            )}

            {/* Botões de Ação */}
            <div style={{ textAlign: 'center', marginTop: '2rem', paddingBottom: '2rem' }}>
              <Button
                tag={Link}
                to="/studio"
                replace
                style={{
                  backgroundColor: 'var(--wood-brown)',
                  borderColor: 'var(--wood-brown)',
                  marginRight: '1rem',
                  padding: '0.75rem 2rem',
                }}
                onMouseEnter={e => {
                  (e.target as HTMLElement).style.backgroundColor = 'var(--wood-light)';
                  (e.target as HTMLElement).style.borderColor = 'var(--wood-light)';
                }}
                onMouseLeave={e => {
                  (e.target as HTMLElement).style.backgroundColor = 'var(--wood-brown)';
                  (e.target as HTMLElement).style.borderColor = 'var(--wood-brown)';
                }}
              >
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Voltar</Translate>
                </span>
              </Button>

              <Button
                tag={Link}
                to={`/studio/${studioEntity.id}/edit`}
                replace
                style={{
                  backgroundColor: 'var(--gold-primary)',
                  borderColor: 'var(--gold-primary)',
                  padding: '0.75rem 2rem',
                }}
                onMouseEnter={e => {
                  (e.target as HTMLElement).style.backgroundColor = 'var(--gold-dark)';
                  (e.target as HTMLElement).style.borderColor = 'var(--gold-dark)';
                }}
                onMouseLeave={e => {
                  (e.target as HTMLElement).style.backgroundColor = 'var(--gold-primary)';
                  (e.target as HTMLElement).style.borderColor = 'var(--gold-primary)';
                }}
              >
                <FontAwesomeIcon icon="pencil-alt" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.edit">Editar</Translate>
                </span>
              </Button>
            </div>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default StudioDetail;
