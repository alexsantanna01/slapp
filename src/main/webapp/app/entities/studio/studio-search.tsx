// src/main/webapp/app/modules/home/studio-search.tsx
import React, { useEffect, useState } from 'react';
import { Card, CardBody, Form, FormGroup, Label, Input, Button, Row, Col } from 'reactstrap';
import './studio.scss';
import { RoomType } from 'app/shared/model/enumerations/room-type.model';
import { toast } from 'react-toastify';

interface ISearchFilters {
  page?: number;
  size?: number;
  sort?: string;
  name?: string;
  city?: string;
  roomType?: string;
  minPrice?: number;
  maxPrice?: number;
  availabilityStartDateTime?: string; // novo
  availabilityEndDateTime?: string; // novo
}

interface StudioSearchProps {
  filters: ISearchFilters | null;
  setFilters: React.Dispatch<React.SetStateAction<ISearchFilters | null>>;
  page: number;
  setPage: (page: number) => void;
  handleInputChange: (field: keyof ISearchFilters, value: any) => void;
}

const StudioSearch = (props: StudioSearchProps) => {
  const { setFilters, filters } = props;
  const [pesquisa, setPesquisa] = useState<ISearchFilters>({
    name: '',
    city: '',
    roomType: '',
    minPrice: 0,
    maxPrice: 1000,
    page: 0,
    size: 6,
    sort: 'name,asc',
    availabilityStartDateTime: '',
    availabilityEndDateTime: '',
  });
  const [showExtraFilters, setShowExtraFilters] = useState(false);

  useEffect(() => {
    if (
      pesquisa.name === '' &&
      pesquisa.city === '' &&
      pesquisa.roomType === '' &&
      pesquisa.minPrice === 0 &&
      pesquisa.maxPrice === 1000 &&
      pesquisa.page === 0 &&
      pesquisa.size === 6 &&
      pesquisa.sort === 'name,asc' &&
      pesquisa.availabilityStartDateTime === '' &&
      pesquisa.availabilityEndDateTime === ''
    ) {
      setFilters({ ...pesquisa });
    }
  }, [pesquisa]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setFilters(prev => ({
      ...prev,
      ...pesquisa,
    }));
  };

  const clearFilters = () => {
    setPesquisa({
      name: '',
      city: '',
      roomType: '',
      minPrice: 0,
      maxPrice: 1000,
      page: 0,
      size: 6,
      sort: 'name,asc',
      availabilityStartDateTime: '',
      availabilityEndDateTime: '',
    });
  };

  const addFiltros = () => {
    setShowExtraFilters(!showExtraFilters);
  };

  // 🔹 Handlers para juntar data e hora em availabilityStartDateTime e availabilityEndDateTime
  const handleDateChange = (date: string) => {
    const startTime = pesquisa.availabilityStartDateTime?.split('T')[1] || '00:00:00';
    const endTime = pesquisa.availabilityEndDateTime?.split('T')[1] || '23:59:59';
    setPesquisa({
      ...pesquisa,
      availabilityStartDateTime: date ? `${date}T${startTime}` : '',
      availabilityEndDateTime: date ? `${date}T${endTime}` : '',
    });
  };

  const handleStartTimeChange = (time: string) => {
    const date = pesquisa.availabilityStartDateTime?.split('T')[0] || '';
    if (date) {
      setPesquisa({
        ...pesquisa,
        availabilityStartDateTime: `${date}T${time || '00:00'}:00`,
      });
    }
  };

  const handleEndTimeChange = (time: string) => {
    const date = pesquisa.availabilityEndDateTime?.split('T')[0] || '';
    if (date) {
      setPesquisa({
        ...pesquisa,
        availabilityEndDateTime: `${date}T${time || '23:59'}:00`,
      });
    }
  };

  return (
    <Card className="studio-search-card">
      <CardBody>
        <Form onSubmit={handleSearch}>
          <Row>
            {/* Busca básica */}
            <Col md={2}>
              <FormGroup>
                <Label for="availabilityDate">Data Desejada</Label>
                <Input
                  type="date"
                  id="availabilityDate"
                  value={pesquisa.availabilityStartDateTime?.split('T')[0] || ''}
                  onChange={e => handleDateChange(e.target.value)}
                />
              </FormGroup>
            </Col>
            <Col md={2}>
              <FormGroup>
                <Label for="availabilityStartTime">Horário Início</Label>
                <Input
                  type="time"
                  id="availabilityStartTime"
                  value={pesquisa.availabilityStartDateTime?.split('T')[1]?.slice(0, 5) || ''}
                  onChange={e => handleStartTimeChange(e.target.value)}
                />
              </FormGroup>
            </Col>
            <Col md={2}>
              <FormGroup>
                <Label for="availabilityEndTime">Horário Fim</Label>
                <Input
                  type="time"
                  id="availabilityEndTime"
                  value={pesquisa.availabilityEndDateTime?.split('T')[1]?.slice(0, 5) || ''}
                  onChange={e => handleEndTimeChange(e.target.value)}
                />
              </FormGroup>
            </Col>
            <Col md={2}>
              <FormGroup>
                <Label for="name">Estudio</Label>
                <Input
                  type="text"
                  id="name"
                  placeholder="Digite o nome do studio..."
                  value={pesquisa.name}
                  onChange={e => setPesquisa({ ...pesquisa, name: e.target.value })}
                />
              </FormGroup>
            </Col>

            <Col md={1}>
              <FormGroup>
                <Label>&nbsp;</Label>
                <div>
                  <Button type="submit" className="button-slapp search-button" block>
                    <i className="fas fa-search"></i> Buscar
                  </Button>
                </div>
              </FormGroup>
            </Col>
            <Col md={1}>
              <FormGroup>
                <Label>&nbsp;</Label>
                <div>
                  <Button onClick={clearFilters} className="button-slapp search-button" block>
                    <i className="fas fa-search"></i> Limpar
                  </Button>
                </div>
              </FormGroup>
            </Col>
            <Col md={2}>
              <FormGroup>
                <Label>&nbsp;</Label>
                <div>
                  <Button onClick={addFiltros} className="button-slapp search-button" block>
                    <i className={showExtraFilters ? 'fas fa-minus' : 'fas fa-plus'}></i>
                    {showExtraFilters ? '- Filtros' : '+ Filtros'}
                  </Button>
                </div>
              </FormGroup>
            </Col>
          </Row>
          {/* Filtros avançados - opcional */}
          {showExtraFilters && (
            <Row>
              <Col md={2}>
                <FormGroup>
                  <Label for="city">Cidade</Label>
                  <Input
                    type="text"
                    id="city"
                    placeholder="Digite sua cidade..."
                    value={pesquisa.city}
                    onChange={e => setPesquisa({ ...pesquisa, city: e.target.value })}
                  />
                </FormGroup>
              </Col>

              <Col md={2}>
                <FormGroup>
                  <Label for="roomType">Tipo de Sala</Label>
                  <Input
                    type="select"
                    id="roomType"
                    value={pesquisa.roomType}
                    onChange={e => setPesquisa({ ...pesquisa, roomType: e.target.value as RoomType })}
                  >
                    <option value="">Todos os tipos</option>
                    <option value={RoomType.RECORDING}>Gravação</option>
                    <option value={RoomType.REHEARSAL}>Ensaio</option>
                    <option value={RoomType.MIXING}>Mixagem</option>
                    <option value={RoomType.LIVE}>Live</option>
                  </Input>
                </FormGroup>
              </Col>

              <Col md={2}>
                <FormGroup>
                  <Label for="priceRange">Faixa de Preço (R$/hora)</Label>
                  <Row>
                    <Col>
                      <Input
                        type="number"
                        placeholder="Min"
                        value={pesquisa.minPrice}
                        onChange={e => setPesquisa({ ...pesquisa, minPrice: Number(e.target.value) })}
                      />
                    </Col>
                    <Col>
                      <Input
                        type="number"
                        placeholder="Max"
                        value={pesquisa.maxPrice}
                        onChange={e => setPesquisa({ ...pesquisa, maxPrice: Number(e.target.value) })}
                      />
                    </Col>
                  </Row>
                </FormGroup>
              </Col>
            </Row>
          )}
        </Form>
      </CardBody>
    </Card>
  );
};

export default StudioSearch;
