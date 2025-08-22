import React, { useState, useEffect } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Row, Col, Badge, Alert, FormGroup, Label, Input } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';

interface TimeSlot {
  hour: number;
  available: boolean;
  reservation?: any;
}

interface ReservationCalendarProps {
  isOpen: boolean;
  toggle: () => void;
  room: any;
  onReservationConfirm: (reservationData: any) => void;
}

export const ReservationCalendar: React.FC<ReservationCalendarProps> = ({ isOpen, toggle, room, onReservationConfirm }) => {
  const [selectedDate, setSelectedDate] = useState<string>(new Date().toISOString().split('T')[0]);
  const [timeSlots, setTimeSlots] = useState<TimeSlot[]>([]);
  const [selectedTimeSlots, setSelectedTimeSlots] = useState<number[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>('');
  const [artistName, setArtistName] = useState<string>('');
  const [instruments, setInstruments] = useState<string>('');

  const operatingHours = { start: 8, end: 22 };

  useEffect(() => {
    if (isOpen && room && selectedDate) {
      loadAvailability();
    }
  }, [isOpen, room, selectedDate]);

  useEffect(() => {
    if (!isOpen) {
      // Reset form when modal is closed
      setSelectedTimeSlots([]);
      setArtistName('');
      setInstruments('');
      setError('');
    }
  }, [isOpen]);

  const loadAvailability = async () => {
    setLoading(true);
    setError('');
    try {
      // Get all reservations for the room and filter manually
      // TODO: Fix backend query to properly filter by date
      const allReservationsResponse = await axios.get(`/api/reservations/room/${room.id}/all`);

      // Filter reservations for the selected date
      const selectedDateObj = new Date(selectedDate);
      const reservations = allReservationsResponse.data.filter((res: any) => {
        const reservationDate = new Date(res.startDateTime);
        const isSameDate =
          reservationDate.getFullYear() === selectedDateObj.getFullYear() &&
          reservationDate.getMonth() === selectedDateObj.getMonth() &&
          reservationDate.getDate() === selectedDateObj.getDate();

        // Only include active reservations
        const isActive = ['PENDING', 'CONFIRMED', 'IN_PROGRESS'].includes(res.status);

        return isSameDate && isActive;
      });

      const slots: TimeSlot[] = [];
      for (let hour = operatingHours.start; hour < operatingHours.end; hour++) {
        // Create start and end time for current slot
        const slotStart = new Date(selectedDate);
        slotStart.setHours(hour, 0, 0, 0);

        const slotEnd = new Date(selectedDate);
        slotEnd.setHours(hour + 1, 0, 0, 0);

        // Check if there's a conflict with any reservation
        const conflictingReservation = reservations.find((res: any) => {
          const reservationStart = new Date(res.startDateTime);
          const reservationEnd = new Date(res.endDateTime);

          // Check overlap: (slotStart < reservationEnd) && (slotEnd > reservationStart)
          return slotStart < reservationEnd && slotEnd > reservationStart;
        });

        slots.push({
          hour,
          available: !conflictingReservation,
          reservation: conflictingReservation,
        });
      }

      setTimeSlots(slots);
    } catch (err) {
      setError('Erro ao carregar disponibilidade');
      console.error('Error loading availability:', err);
    } finally {
      setLoading(false);
    }
  };

  const toggleTimeSlot = (hour: number) => {
    const slot = timeSlots.find(s => s.hour === hour);
    if (!slot?.available) return;

    setSelectedTimeSlots(prev => {
      if (prev.includes(hour)) {
        return prev.filter(h => h !== hour);
      } else {
        return [...prev, hour].sort((a, b) => a - b);
      }
    });
  };

  const handleConfirmReservation = () => {
    if (selectedTimeSlots.length === 0) {
      setError('Selecione pelo menos um horário');
      return;
    }

    const startHour = Math.min(...selectedTimeSlots);
    const endHour = Math.max(...selectedTimeSlots) + 1;

    const startDateTime = new Date(selectedDate);
    startDateTime.setHours(startHour, 0, 0, 0);

    const endDateTime = new Date(selectedDate);
    endDateTime.setHours(endHour, 0, 0, 0);

    const duration = selectedTimeSlots.length;
    const totalPrice = room.hourlyRate * duration;

    const reservationData = {
      roomId: room.id,
      startDateTime: startDateTime.toISOString(),
      endDateTime: endDateTime.toISOString(),
      totalPrice,
      notes: `Reserva para ${duration} hora(s)`,
      artistName: artistName.trim() || undefined,
      instruments: instruments.trim() || undefined,
    };

    onReservationConfirm(reservationData);
  };

  const formatHour = (hour: number) => {
    return `${hour.toString().padStart(2, '0')}:00`;
  };

  const getMinDate = () => {
    return new Date().toISOString().split('T')[0];
  };

  const getMaxDate = () => {
    const maxDate = new Date();
    maxDate.setMonth(maxDate.getMonth() + 3);
    return maxDate.toISOString().split('T')[0];
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle} size="lg">
      <ModalHeader toggle={toggle}>
        <FontAwesomeIcon icon="calendar-alt" /> Reservar Sala - {room?.name}
      </ModalHeader>
      <ModalBody>
        {error && <Alert color="danger">{error}</Alert>}

        <Row className="mb-4">
          <Col md="6">
            <label className="form-label">
              <strong>Selecione a Data:</strong>
            </label>
            <input
              type="date"
              className="form-control"
              value={selectedDate}
              min={getMinDate()}
              max={getMaxDate()}
              onChange={e => setSelectedDate(e.target.value)}
            />
          </Col>
          <Col md="6" className="d-flex align-items-end">
            <div>
              <div className="mb-2">
                <Badge color="success" className="me-2">
                  Disponível
                </Badge>
                <Badge color="danger" className="me-2">
                  Ocupado
                </Badge>
                <Badge color="primary">Selecionado</Badge>
              </div>
              <small className="text-muted">Valor por hora: R$ {room?.hourlyRate?.toFixed(2)}</small>
            </div>
          </Col>
        </Row>

        {loading ? (
          <div className="text-center py-4">
            <FontAwesomeIcon icon="spinner" spin /> Carregando horários...
          </div>
        ) : (
          <div>
            <h6 className="mb-3">
              Horários Disponíveis ({formatHour(operatingHours.start)} às {formatHour(operatingHours.end)}):
            </h6>
            <Row>
              {timeSlots.map(slot => (
                <Col xs="6" sm="4" md="3" key={slot.hour} className="mb-2">
                  <Button
                    color={selectedTimeSlots.includes(slot.hour) ? 'primary' : slot.available ? 'outline-success' : 'outline-danger'}
                    size="sm"
                    block
                    disabled={!slot.available}
                    onClick={() => toggleTimeSlot(slot.hour)}
                    style={{ cursor: slot.available ? 'pointer' : 'not-allowed' }}
                  >
                    {formatHour(slot.hour)}
                  </Button>
                </Col>
              ))}
            </Row>

            {selectedTimeSlots.length > 0 && (
              <>
                <Alert color="info" className="mt-3">
                  <strong>Resumo da Reserva:</strong>
                  <br />
                  Horários selecionados: {selectedTimeSlots.map(h => formatHour(h)).join(', ')}
                  <br />
                  Duração: {selectedTimeSlots.length} hora(s)
                  <br />
                  Valor total: R$ {(room?.hourlyRate * selectedTimeSlots.length)?.toFixed(2)}
                </Alert>

                <div className="mt-4 p-3 border rounded">
                  <h6 className="mb-3">
                    <FontAwesomeIcon icon="music" /> Informações Adicionais
                  </h6>

                  <Row>
                    <Col md="6">
                      <FormGroup>
                        <Label for="artistName">Nome da Banda/Artista/Produtor</Label>
                        <Input
                          type="text"
                          id="artistName"
                          value={artistName}
                          onChange={e => setArtistName(e.target.value)}
                          placeholder="Ex: Banda XYZ, João Silva, etc."
                          maxLength={255}
                        />
                        <small className="text-muted">Opcional - Quem irá usar a sala</small>
                      </FormGroup>
                    </Col>

                    <Col md="6">
                      <FormGroup>
                        <Label for="instruments">Instrumentos a serem utilizados</Label>
                        <Input
                          type="textarea"
                          id="instruments"
                          value={instruments}
                          onChange={e => setInstruments(e.target.value)}
                          placeholder="Ex: Guitarra, Bateria, Baixo, Vocal..."
                          rows={3}
                        />
                        <small className="text-muted">Opcional - Liste os instrumentos que serão usados</small>
                      </FormGroup>
                    </Col>
                  </Row>
                </div>
              </>
            )}
          </div>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>
          Cancelar
        </Button>
        <Button color="primary" onClick={handleConfirmReservation} disabled={selectedTimeSlots.length === 0 || loading}>
          <FontAwesomeIcon icon="check" /> Confirmar Reserva
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ReservationCalendar;
