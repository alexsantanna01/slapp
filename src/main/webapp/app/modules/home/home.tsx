import './home.scss';

import React, { useEffect, useRef } from 'react';
import { Card, CardImg, Col, Container, Row } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { useCurrentUserProfile } from 'app/shared/util/user-profile-util';
import Studio from 'app/entities/studio/studio';
import StudioOwnerHome from './studio-owner-home';
import { IUser } from 'app/shared/model/user.model';
import { toast } from 'react-toastify';

export const Home = () => {
  const account: IUser = useAppSelector(state => state.authentication.account);
  const { userType, loading } = useCurrentUserProfile();

  if (loading) {
    return (
      <Container className="home-page d-flex justify-content-center align-items-center" style={{ minHeight: '50vh' }}>
        <div className="text-center">
          <div className="spinner-border text-primary" role="status">
            <span className="sr-only">Carregando...</span>
          </div>
          <div className="mt-2">Carregando perfil do usuário...</div>
        </div>
      </Container>
    );
  }

  if (account?.login) {
    if (userType === 'STUDIO_OWNER') {
      return <StudioOwnerHome />;
    } else {
      // Default to CUSTOMER view (current Studio component)
      return <Studio />;
    }
  }

  // Non-authenticated user landing page
  return (
    <Container className="home-page">
      <Card className="my-2">
        <CardImg
          alt="Banner Slapp"
          src="../../../content/images/banner.png"
          // style={{
          //   height: 180
          // }}
          top
          width="100%"
        />
      </Card>
      <Container sx={{ py: 4 }} className="home-page">
        <Row className="features-section">
          <Col md={12}>
            <h2 className="text-center mb-5">Por que escolher o SLAPP?</h2>
          </Col>
          <Col md={4} className="text-center mb-4">
            <div className="feature-icon mb-3">
              <i className="fas fa-search fa-3x text-primary"></i>
            </div>
            <h4>Busca Inteligente</h4>
            <p>Encontre estúdios por localização, equipamentos e preço.</p>
          </Col>
          <Col md={4} className="text-center mb-4">
            <div className="feature-icon mb-3">
              <i className="fas fa-calendar-alt fa-3x text-primary"></i>
            </div>
            <h4>Reserva Fácil</h4>
            <p>Agende seu horário de forma rápida e segura.</p>
          </Col>
          <Col md={4} className="text-center mb-4">
            <div className="feature-icon mb-3">
              <i className="fas fa-star fa-3x text-primary"></i>
            </div>
            <h4>Avaliações Reais</h4>
            <p>Veja comentários de outros músicos e bandas.</p>
          </Col>
        </Row>
      </Container>
    </Container>
  );
};
