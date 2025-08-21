import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Card, CardImg, Col, Container, Row } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import Studio from 'app/entities/studio/studio';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <>
      {account?.login ? (
        <Studio />
      ) : (
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
      )}
    </>
  );
};
