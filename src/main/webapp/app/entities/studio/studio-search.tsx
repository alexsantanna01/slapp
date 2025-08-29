// src/main/webapp/app/modules/home/studio-search.tsx
import React, { useEffect, useState } from 'react';
import { Card, CardBody, Form, FormGroup, Label, Input, Row, Col } from 'reactstrap';
import Button from '@mui/material/Button';
import SearchIcon from '@mui/icons-material/Search';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import CleaningServicesIcon from '@mui/icons-material/CleaningServices';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import FavoriteIcon from '@mui/icons-material/Favorite';
import './studio.scss';
import { RoomType } from 'app/shared/model/enumerations/room-type.model';
import { FormControl, InputLabel, OutlinedInput } from '@mui/material';

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
  onlyFavorites?: boolean; // filtro para mostrar apenas favoritos
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
    onlyFavorites: false,
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
      pesquisa.availabilityEndDateTime === '' &&
      pesquisa.onlyFavorites === false
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
      onlyFavorites: false,
    });
  };

  const addFiltros = () => {
    setShowExtraFilters(!showExtraFilters);
  };

  const handleFavoritesFilter = (e: React.FormEvent) => {
    e.preventDefault();
    const newFilters = { ...pesquisa, onlyFavorites: true };
    setPesquisa(newFilters);
    setFilters(prev => ({
      ...prev,
      ...newFilters,
    }));
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
                  inline
                  id="availabilityDate"
                  type="date"
                  value={pesquisa.availabilityStartDateTime?.split('T')[0] || ''}
                  onChange={e => handleDateChange(e.target.value)}
                  className="input-slapp"
                />
              </FormGroup>
            </Col>
            <Col md={1}>
              <FormGroup>
                <Label for="availabilityStartTime">Início</Label>
                <Input
                  type="time"
                  id="availabilityStartTime"
                  value={pesquisa.availabilityStartDateTime?.split('T')[1]?.slice(0, 5) || ''}
                  onChange={e => handleStartTimeChange(e.target.value)}
                  className="input-slapp"
                  style={{ width: '100%' }}
                />
              </FormGroup>
            </Col>
            <Col md={1}>
              <FormGroup>
                <Label for="availabilityEndTime">Fim</Label>
                <Input
                  type="time"
                  id="availabilityEndTime"
                  value={pesquisa.availabilityEndDateTime?.split('T')[1]?.slice(0, 5) || ''}
                  onChange={e => handleEndTimeChange(e.target.value)}
                  style={{ width: '100%' }}
                  className="input-slapp"
                />
              </FormGroup>
            </Col>
            <Col md={2} style={{ marginTop: '2rem' }}>
              <Button
                variant="contained"
                type="submit"
                startIcon={<SearchIcon />}
                className="button-slapp search-button"
                style={{ width: '100%' }}
              >
                Buscar
              </Button>
            </Col>
            <Col md={2} style={{ marginTop: '2rem' }}>
              <Button
                variant="contained"
                onClick={handleFavoritesFilter}
                startIcon={<FavoriteIcon />}
                className={`button-slapp search-button ${pesquisa.onlyFavorites ? 'active' : ''}`}
                style={{ width: '100%' }}
              >
                Favoritos
              </Button>
            </Col>
            <Col md={2} style={{ marginTop: '2rem' }}>
              {!showExtraFilters ? (
                <Button
                  variant="contained"
                  onClick={addFiltros}
                  className="button-slapp search-button"
                  style={{ width: '100%' }}
                  startIcon={<AddCircleOutlineIcon />}
                >
                  FILTROS
                </Button>
              ) : (
                <Button
                  variant="contained"
                  onClick={addFiltros}
                  className="button-slapp search-button"
                  style={{ width: '100%' }}
                  startIcon={<RemoveCircleOutlineIcon />}
                >
                  FILTROS
                </Button>
              )}
            </Col>
            <Col md={2} style={{ marginTop: '2rem' }}>
              <Button
                onClick={clearFilters}
                className="button-slapp search-button"
                startIcon={<CleaningServicesIcon />}
                style={{ width: '100%' }}
              >
                Limpar
              </Button>
            </Col>
          </Row>
          {/* Filtros avançados - opcional */}
          {showExtraFilters && (
            <Row>
              <Col md={2}>
                <FormGroup>
                  <Label for="name">Estúdio</Label>
                  <Input
                    type="text"
                    id="name"
                    placeholder="Digite o nome do studio..."
                    value={pesquisa.name}
                    onChange={e => setPesquisa({ ...pesquisa, name: e.target.value })}
                    className="input-slapp"
                  />
                </FormGroup>
              </Col>
              <Col md={2}>
                <FormGroup>
                  <Label for="city">Cidade</Label>
                  <Input
                    type="text"
                    id="city"
                    placeholder="Digite sua cidade..."
                    value={pesquisa.city}
                    onChange={e => setPesquisa({ ...pesquisa, city: e.target.value })}
                    className="input-slapp"
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
                    className="input-slapp"
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
                        className="input-slapp"
                      />
                    </Col>
                    <Col>
                      <Input
                        type="number"
                        placeholder="Max"
                        value={pesquisa.maxPrice}
                        onChange={e => setPesquisa({ ...pesquisa, maxPrice: Number(e.target.value) })}
                        className="input-slapp"
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
