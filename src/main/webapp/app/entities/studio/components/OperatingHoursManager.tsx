import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardBody, Row, Col, Button, FormGroup, Label, Input, Alert } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';

interface OperatingHours {
  id?: number;
  dayOfWeek: string;
  startTime?: string;
  endTime?: string;
  isOpen: boolean;
  studioId?: number;
}

interface OperatingHoursManagerProps {
  studioId?: number;
  isNew: boolean;
  onOperatingHoursChange: (operatingHours: OperatingHours[]) => void;
}

const dayLabels = {
  MONDAY: 'Segunda-feira',
  TUESDAY: 'Terça-feira',
  WEDNESDAY: 'Quarta-feira',
  THURSDAY: 'Quinta-feira',
  FRIDAY: 'Sexta-feira',
  SATURDAY: 'Sábado',
  SUNDAY: 'Domingo',
};

const daysOrder = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

export const OperatingHoursManager: React.FC<OperatingHoursManagerProps> = ({ studioId, isNew, onOperatingHoursChange }) => {
  const [operatingHours, setOperatingHours] = useState<OperatingHours[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    if (!isNew && studioId) {
      loadOperatingHours();
    } else {
      initializeDefaultHours();
    }
  }, [studioId, isNew]);

  useEffect(() => {
    onOperatingHoursChange(operatingHours);
  }, [operatingHours, onOperatingHoursChange]);

  const initializeDefaultHours = () => {
    const defaultHours: OperatingHours[] = daysOrder.map(day => ({
      dayOfWeek: day,
      startTime: day === 'SUNDAY' ? '' : '08:00',
      endTime: day === 'SUNDAY' ? '' : '22:00',
      isOpen: day !== 'SUNDAY',
    }));
    setOperatingHours(defaultHours);
  };

  const loadOperatingHours = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await axios.get(`/api/studio-operating-hours/studio/${studioId}`);
      if (response.data && response.data.length > 0) {
        // Organizar por ordem dos dias da semana
        const sortedHours = daysOrder.map(day => {
          const existing = response.data.find((oh: OperatingHours) => oh.dayOfWeek === day);
          return (
            existing || {
              dayOfWeek: day,
              startTime: '',
              endTime: '',
              isOpen: false,
            }
          );
        });
        setOperatingHours(sortedHours);
      } else {
        initializeDefaultHours();
      }
    } catch (err) {
      console.error('Error loading operating hours:', err);
      setError('Erro ao carregar horários de funcionamento');
      initializeDefaultHours();
    } finally {
      setLoading(false);
    }
  };

  const updateOperatingHour = (dayOfWeek: string, field: keyof OperatingHours, value: any) => {
    setOperatingHours(prev => prev.map(oh => (oh.dayOfWeek === dayOfWeek ? { ...oh, [field]: value } : oh)));
  };

  const toggleDayOpen = (dayOfWeek: string) => {
    setOperatingHours(prev =>
      prev.map(oh => {
        if (oh.dayOfWeek === dayOfWeek) {
          const isOpen = !oh.isOpen;
          return {
            ...oh,
            isOpen,
            startTime: isOpen && !oh.startTime ? '08:00' : oh.startTime,
            endTime: isOpen && !oh.endTime ? '22:00' : oh.endTime,
          };
        }
        return oh;
      }),
    );
  };

  const copyHours = (fromDay: string, toDay: string) => {
    const sourceDay = operatingHours.find(oh => oh.dayOfWeek === fromDay);
    if (sourceDay) {
      updateOperatingHour(toDay, 'startTime', sourceDay.startTime);
      updateOperatingHour(toDay, 'endTime', sourceDay.endTime);
      updateOperatingHour(toDay, 'isOpen', sourceDay.isOpen);
    }
  };

  const applyToAllDays = (sourceDay: string) => {
    const source = operatingHours.find(oh => oh.dayOfWeek === sourceDay);
    if (source) {
      setOperatingHours(prev =>
        prev.map(oh => ({
          ...oh,
          startTime: source.startTime,
          endTime: source.endTime,
          isOpen: source.isOpen,
        })),
      );
    }
  };

  if (loading) {
    return (
      <Card className="mb-4">
        <CardHeader>
          <FontAwesomeIcon icon="clock" /> Horários de Funcionamento
        </CardHeader>
        <CardBody>
          <div className="text-center">
            <FontAwesomeIcon icon="spinner" spin /> Carregando...
          </div>
        </CardBody>
      </Card>
    );
  }

  return (
    <Card className="mb-4">
      <CardHeader>
        <FontAwesomeIcon icon="clock" /> Horários de Funcionamento
      </CardHeader>
      <CardBody>
        {error && <Alert color="danger">{error}</Alert>}

        <div className="mb-3">
          <small className="text-muted">Configure os horários de funcionamento do estúdio para cada dia da semana.</small>
        </div>

        {operatingHours.map((oh, index) => (
          <Row key={oh.dayOfWeek} className="mb-3 align-items-center">
            <Col md="3">
              <FormGroup check>
                <Label check>
                  <Input type="checkbox" checked={oh.isOpen} onChange={() => toggleDayOpen(oh.dayOfWeek)} />
                  <strong>{dayLabels[oh.dayOfWeek]}</strong>
                </Label>
              </FormGroup>
            </Col>

            <Col md="3">
              <FormGroup>
                <Label for={`start-${oh.dayOfWeek}`} className="sr-only">
                  Abertura
                </Label>
                <Input
                  type="time"
                  id={`start-${oh.dayOfWeek}`}
                  value={oh.startTime || ''}
                  disabled={!oh.isOpen}
                  onChange={e => updateOperatingHour(oh.dayOfWeek, 'startTime', e.target.value)}
                  placeholder="Abertura"
                />
              </FormGroup>
            </Col>

            <Col md="3">
              <FormGroup>
                <Label for={`end-${oh.dayOfWeek}`} className="sr-only">
                  Fechamento
                </Label>
                <Input
                  type="time"
                  id={`end-${oh.dayOfWeek}`}
                  value={oh.endTime || ''}
                  disabled={!oh.isOpen}
                  onChange={e => updateOperatingHour(oh.dayOfWeek, 'endTime', e.target.value)}
                  placeholder="Fechamento"
                />
              </FormGroup>
            </Col>

            <Col md="3">
              {!oh.isOpen ? (
                <span className="text-muted">
                  <FontAwesomeIcon icon="times" /> Fechado
                </span>
              ) : (
                <div className="btn-group btn-group-sm">
                  <Button size="sm" color="outline-secondary" onClick={() => applyToAllDays(oh.dayOfWeek)} title="Aplicar a todos os dias">
                    <FontAwesomeIcon icon="copy" />
                  </Button>
                  {index > 0 && (
                    <Button
                      size="sm"
                      color="outline-secondary"
                      onClick={() => copyHours(daysOrder[index - 1], oh.dayOfWeek)}
                      title={`Copiar de ${dayLabels[daysOrder[index - 1]]}`}
                    >
                      <FontAwesomeIcon icon="arrow-up" />
                    </Button>
                  )}
                </div>
              )}
            </Col>
          </Row>
        ))}

        <Row className="mt-4">
          <Col>
            <Alert color="info" className="mb-0">
              <FontAwesomeIcon icon="info-circle" />
              <strong> Dicas:</strong>
              <ul className="mb-0 mt-2">
                <li>Marque a caixa para indicar que o estúdio funciona no dia</li>
                <li>
                  Use <FontAwesomeIcon icon="copy" /> para aplicar o horário a todos os dias
                </li>
                <li>
                  Use <FontAwesomeIcon icon="arrow-up" /> para copiar do dia anterior
                </li>
                <li>Deixe desmarcado para dias em que o estúdio não funciona</li>
              </ul>
            </Alert>
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};

export default OperatingHoursManager;
