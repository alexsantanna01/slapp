import React from 'react';
import { Card, CardImg, CardBody, CardTitle, CardText, Badge, Button, Row, Col, Spinner, Label } from 'reactstrap';
import { useNavigate } from 'react-router';
// import { IStudioList } from 'app/shared/model/studioList.model';
import { RoomType } from 'app/shared/model/enumerations/room-type.model';
import { IStudio } from 'app/shared/model/studio.model';

interface StudioListProps {
  estudioEntities: IStudio[];
  totalItems: number;
  hasMore: boolean;
  loadingMore: boolean;
  onLoadMore: () => void;
}

const StudioList = (props: StudioListProps) => {
  const { estudioEntities, totalItems, hasMore, loadingMore, onLoadMore } = props;
  const navigate = useNavigate();

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(price);
  };

  const getRoomTypeBadge = (roomType: RoomType) => {
    const configs = {
      [RoomType.RECORDING]: { color: 'primary', label: 'Gravação' },
      [RoomType.REHEARSAL]: { color: 'success', label: 'Ensaio' },
      [RoomType.BOTH]: { color: 'info', label: 'Gravação & Ensaio' },
    };

    const config = configs[roomType];
    return (
      <Badge color={config.color} pill className="mr-1">
        {config.label}
      </Badge>
    );
  };

  const handleStudioClick = (studioId: string) => {
    navigate(`/studio/${studioId}`);
  };

  return (
    <div className="studio-list">
      {/* Header com contador */}
      <div className="mb-4">
        <h4 className="text-muted">
          {estudioEntities.length > 0 ? `Mostrando ${estudioEntities.length} de ${totalItems} estúdios` : 'Nenhum estúdio encontrado'}
        </h4>
      </div>

      {/* Lista de estúdios */}
      <Row>
        {estudioEntities?.map((studio, index) => (
          <Col key={`${studio.id}-${index}`} md={6} lg={4} className="mb-4">
            <Card className="studio-card h-100 shadow-sm">
              {/* Imagem do estúdio */}
              <div className="studio-image-container position-relative">
                <CardImg
                  top
                  src={studio.image || 'https://via.placeholder.com/300x200?text=Sem+Imagem'}
                  alt={studio.name}
                  className="studio-image"
                  style={{ height: '200px', objectFit: 'cover' }}
                />
                <div className="position-absolute" style={{ top: '10px', right: '10px' }}>
                  <Button
                    color="light"
                    size="sm"
                    className="shadow-sm"
                    onClick={e => {
                      e.preventDefault();
                      alert('Funcionalidade de favoritos será implementada!');
                    }}
                  >
                    <i className="far fa-heart"></i>
                  </Button>
                </div>
              </div>

              <CardBody className="d-flex flex-column">
                {/* Header com nome e avaliação */}
                <div className="studio-header mb-2">
                  <CardTitle tag="h5" className="studio-name mb-1">
                    {studio?.name}
                  </CardTitle>
                  {/* <div className="studio-rating">
                    <Label className="mr-1" color="primary">
                      {studio?.averageRating || 0} precision={0.1} 
                    </Label>
                    <span className="rating-text ml-2 small text-muted">
                      {studio.averageRating?.toFixed(1) || '0.0'} ({studio?.reviewCount || 0} avaliações)
                    </span>
                  </div> */}
                </div>

                {/* Localização */}
                <div className="studio-location mb-2">
                  <i className="fas fa-map-marker-alt text-muted mr-1"></i>
                  <small className="text-muted">
                    {studio?.city}, {studio?.state}
                  </small>
                </div>

                {/* Descrição */}
                <CardText className="studio-description flex-grow-1">
                  {studio.description?.length > 120
                    ? `${studio.description.substring(0, 120)}...`
                    : studio.description || 'Sem descrição disponível'}
                </CardText>

                {/* Footer com preço e ação */}
                <div className="studio-footer mt-auto">
                  <div className="d-flex justify-content-between align-items-center">
                    <div className="studio-price">
                      <small className="price-unit text-muted d-block">a partir de</small>
                      <strong className="price-value">{formatPrice(studio?.minPrice || 0)}</strong>
                      <small className="price-unit text-muted d-block">por hora</small>
                    </div>
                    <Button color="primary" size="sm" onClick={() => handleStudioClick(studio?.id?.toString())} className="button-slapp">
                      Detalhes
                    </Button>
                  </div>
                </div>
              </CardBody>
            </Card>
          </Col>
        ))}
      </Row>

      {/* Botão de carregar mais */}
      {estudioEntities.length > 0 && (
        <div className="studio-pagination mt-4">
          <div className="text-center">
            {hasMore ? (
              <Button className="button-slapp-straped" onClick={onLoadMore} disabled={loadingMore} size="lg">
                {loadingMore ? (
                  <>
                    <Spinner size="sm" className="mr-2" />
                    Carregando...
                  </>
                ) : (
                  'Carregar mais estúdios'
                )}
              </Button>
            ) : (
              <div className="alert alert-info">
                <i className="fas fa-check-circle mr-2"></i>
                Todos os estúdios foram carregados!
              </div>
            )}

            <div className="mt-2">
              <small className="text-muted">
                {estudioEntities.length} de {totalItems} estúdios exibidos
              </small>
            </div>
          </div>
        </div>
      )}

      {/* Estado vazio */}
      {estudioEntities.length === 0 && (
        <div className="text-center py-5">
          <i className="fas fa-music fa-3x text-muted mb-3"></i>
          <h4 className="text-muted">Nenhum estúdio encontrado</h4>
          <p className="text-muted">Tente ajustar os filtros de busca ou remover alguns critérios.</p>
        </div>
      )}
    </div>
  );
};
export default StudioList;
