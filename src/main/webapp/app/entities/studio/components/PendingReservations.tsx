import React, { useState, useEffect } from 'react';
import { Card, CardBody, Button, Badge, Row, Col, Alert, Modal, ModalHeader, ModalBody, ModalFooter, Input } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TextFormat } from 'react-jhipster';
import axios from 'axios';
import { toast } from 'react-toastify';
import { APP_DATE_FORMAT } from 'app/config/constants';

interface PendingReservationsProps {
  studioId: number;
  isOwner: boolean;
}

interface PendingReservation {
  id: number;
  startDateTime: string;
  endDateTime: string;
  totalPrice: number;
  notes?: string;
  createdAt: string;
  customer: {
    id: number;
    user?: {
      firstName?: string;
      lastName?: string;
      login?: string;
    };
  };
  room: {
    id: number;
    name: string;
  };
}

export const PendingReservations: React.FC<PendingReservationsProps> = ({ studioId, isOwner }) => {
  const [pendingReservations, setPendingReservations] = useState<PendingReservation[]>([]);
  const [loading, setLoading] = useState(false);
  const [rejectModalOpen, setRejectModalOpen] = useState(false);
  const [selectedReservationId, setSelectedReservationId] = useState<number | null>(null);
  const [rejectReason, setRejectReason] = useState('');

  useEffect(() => {
    if (isOwner) {
      loadPendingReservations();
    }
  }, [studioId, isOwner]);

  const loadPendingReservations = async () => {
    setLoading(true);
    try {
      const response = await axios.get(`/api/reservations/studio/${studioId}/pending`);
      setPendingReservations(response.data);
    } catch (error) {
      console.error('Error loading pending reservations:', error);
      toast.error('Erro ao carregar reservas pendentes');
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (reservationId: number) => {
    try {
      await axios.post(`/api/reservations/${reservationId}/approve`);
      toast.success('Reserva aprovada com sucesso!');
      loadPendingReservations(); // Reload list
    } catch (error) {
      console.error('Error approving reservation:', error);
      toast.error('Erro ao aprovar reserva');
    }
  };

  const handleReject = async () => {
    if (!selectedReservationId) return;

    try {
      const params = rejectReason ? `?reason=${encodeURIComponent(rejectReason)}` : '';
      await axios.post(`/api/reservations/${selectedReservationId}/reject${params}`);
      toast.success('Reserva rejeitada com sucesso!');
      setRejectModalOpen(false);
      setSelectedReservationId(null);
      setRejectReason('');
      loadPendingReservations(); // Reload list
    } catch (error) {
      console.error('Error rejecting reservation:', error);
      toast.error('Erro ao rejeitar reserva');
    }
  };

  const openRejectModal = (reservationId: number) => {
    setSelectedReservationId(reservationId);
    setRejectModalOpen(true);
  };

  const closeRejectModal = () => {
    setRejectModalOpen(false);
    setSelectedReservationId(null);
    setRejectReason('');
  };

  const formatDateTime = (dateTimeString: string) => {
    const date = new Date(dateTimeString);
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const getTimeRemaining = (createdAt: string) => {
    const created = new Date(createdAt);
    const autoConfirmTime = new Date(created.getTime() + 30 * 60 * 1000); // 30 minutes
    const now = new Date();
    const remaining = autoConfirmTime.getTime() - now.getTime();

    if (remaining <= 0) {
      return 'Aprovação automática em breve';
    }

    const minutes = Math.floor(remaining / 60000);
    return `Auto-aprovação em ${minutes} min`;
  };

  if (!isOwner) {
    return null;
  }

  return (
    <Card className="mb-4" style={{ backgroundColor: 'var(--background-card)', border: '1px solid var(--border-input)' }}>
      <CardBody>
        <h3 className="mb-3" style={{ color: 'var(--gold-dark)' }}>
          <FontAwesomeIcon icon="clock" /> Reservas Pendentes
          {pendingReservations.length > 0 && (
            <Badge color="warning" className="ms-2">
              {pendingReservations.length}
            </Badge>
          )}
        </h3>

        {loading ? (
          <div className="text-center py-3">
            <FontAwesomeIcon icon="spinner" spin /> Carregando...
          </div>
        ) : pendingReservations.length === 0 ? (
          <Alert color="info">
            <FontAwesomeIcon icon="info-circle" /> Não há reservas pendentes no momento.
          </Alert>
        ) : (
          <div>
            {pendingReservations.map(reservation => (
              <Card key={reservation.id} className="mb-3 border">
                <CardBody>
                  <Row>
                    <Col md="8">
                      <h6 className="mb-2">
                        <FontAwesomeIcon icon="door-open" /> {reservation.room.name}
                      </h6>
                      <p className="mb-1">
                        <FontAwesomeIcon icon="user" /> <strong>Cliente:</strong> {reservation.customer.user?.firstName}{' '}
                        {reservation.customer.user?.lastName} ({reservation.customer.user?.login})
                      </p>
                      <p className="mb-1">
                        <FontAwesomeIcon icon="calendar" /> <strong>Data/Hora:</strong> {formatDateTime(reservation.startDateTime)} às{' '}
                        {formatDateTime(reservation.endDateTime)}
                      </p>
                      <p className="mb-1">
                        <FontAwesomeIcon icon="dollar-sign" /> <strong>Valor:</strong> R$ {reservation.totalPrice.toFixed(2)}
                      </p>
                      {reservation.notes && (
                        <p className="mb-1">
                          <FontAwesomeIcon icon="sticky-note" /> <strong>Observações:</strong> {reservation.notes}
                        </p>
                      )}
                      <small className="text-muted">
                        <FontAwesomeIcon icon="clock" /> {getTimeRemaining(reservation.createdAt)}
                      </small>
                    </Col>
                    <Col md="4" className="text-end">
                      <div className="d-flex flex-column gap-2">
                        <Button color="success" size="sm" onClick={() => handleApprove(reservation.id)} className="mb-1">
                          <FontAwesomeIcon icon="check" /> Aprovar
                        </Button>
                        <Button color="danger" size="sm" onClick={() => openRejectModal(reservation.id)}>
                          <FontAwesomeIcon icon="times" /> Rejeitar
                        </Button>
                      </div>
                    </Col>
                  </Row>
                </CardBody>
              </Card>
            ))}
          </div>
        )}

        {/* Modal de Rejeição */}
        <Modal isOpen={rejectModalOpen} toggle={closeRejectModal}>
          <ModalHeader toggle={closeRejectModal}>
            <FontAwesomeIcon icon="times-circle" /> Rejeitar Reserva
          </ModalHeader>
          <ModalBody>
            <p>Tem certeza que deseja rejeitar esta reserva?</p>
            <div className="form-group">
              <label htmlFor="rejectReason">Motivo da rejeição (opcional):</label>
              <Input
                type="textarea"
                id="rejectReason"
                value={rejectReason}
                onChange={e => setRejectReason(e.target.value)}
                placeholder="Ex: Horário não disponível, manutenção programada..."
                rows={3}
              />
            </div>
          </ModalBody>
          <ModalFooter>
            <Button color="secondary" onClick={closeRejectModal}>
              Cancelar
            </Button>
            <Button color="danger" onClick={handleReject}>
              <FontAwesomeIcon icon="times" /> Confirmar Rejeição
            </Button>
          </ModalFooter>
        </Modal>
      </CardBody>
    </Card>
  );
};

export default PendingReservations;
