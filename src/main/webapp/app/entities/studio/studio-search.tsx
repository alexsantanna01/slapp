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
  roomType?: string; // 'RECORDING' | 'REHEARSAL' | 'BOTH'
  minPrice?: number;
  maxPrice?: number;
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
  });

  useEffect(() => {
    if (pesquisa.name === '' && pesquisa.city === '' && pesquisa.roomType === '' && pesquisa.minPrice === 0 && pesquisa.maxPrice === 1000) {
      setFilters({
        name: pesquisa.name,
        city: pesquisa.city,
        roomType: pesquisa.roomType as RoomType,
        minPrice: pesquisa.minPrice,
        maxPrice: pesquisa.maxPrice,
      });
    }
  }, [pesquisa]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setFilters(prev => ({
      ...prev,
      name: pesquisa?.name || '',
      city: pesquisa?.city || '',
      roomType: pesquisa?.roomType || '',
      minPrice: pesquisa?.minPrice || 0,
      maxPrice: pesquisa?.maxPrice || 1000,
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
    });
  };

  return (
    <Card className="studio-search-card">
      <CardBody>
        <Form onSubmit={handleSearch}>
          <Row>
            {/* Busca básica */}
            <Col md={3}>
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
            <Col md={3}>
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
                  <option value={RoomType.BOTH}>Ambos</option>
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
          </Row>
        </Form>
      </CardBody>
    </Card>
  );
};

export default StudioSearch;
