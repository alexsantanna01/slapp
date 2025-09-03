import React, { useEffect } from 'react';
import { Card, CardImg, CardBody, CardTitle, CardText, Badge, Row, Col, Spinner, Label } from 'reactstrap';
import { useNavigate } from 'react-router';
// import { IStudioList } from 'app/shared/model/studioList.model';
import { RoomType } from 'app/shared/model/enumerations/room-type.model';
import { IStudio } from 'app/shared/model/studio.model';
import { BottomNavigationAction, Button, CardActions, IconButton } from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { toggleStudioFavorite, getUserFavoriteStudioIds } from 'app/entities/favorite/favorite.reducer';
import VisibilityIcon from '@mui/icons-material/Visibility';

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
  const dispatch = useAppDispatch();

  // Estados do Redux para favoritos
  const { userFavoriteStudioIds, toggleLoading, loadingFavorites } = useAppSelector(state => state.favorite);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

  // Carrega os favoritos do usuário quando o componente monta
  useEffect(() => {
    if (isAuthenticated) {
      dispatch(getUserFavoriteStudioIds());
    }
  }, [dispatch, isAuthenticated]);

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

  const handleFavoriteClick = (studioId: number, event: React.MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    if (!isAuthenticated) {
      alert('Você precisa estar logado para favoritar um estúdio');
      return;
    }

    dispatch(toggleStudioFavorite(studioId));
  };

  const isStudioFavorite = (studioId: number): boolean => {
    return userFavoriteStudioIds.includes(studioId);
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
                <div className="position-absolute" style={{ top: '5px', right: '5px' }}>
                  <BottomNavigationAction
                    label={isStudioFavorite(studio.id) ? 'Remover dos favoritos' : 'Adicionar aos favoritos'}
                    value="favorites"
                    icon={isStudioFavorite(studio.id) ? <FavoriteIcon style={{ color: '#ff4444' }} /> : <FavoriteBorderIcon />}
                    className="shadow-sm"
                    onClick={e => handleFavoriteClick(studio.id, e)}
                    disabled={toggleLoading}
                  />
                </div>
              </div>

              <CardBody className="d-flex flex-column">
                {/* Header com nome e avaliação */}
                <div className="studio-header mb-2">
                  <CardTitle tag="h5" className="studio-name mb-1">
                    {studio?.name}
                  </CardTitle>
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
                    <Button
                      startIcon={<VisibilityIcon />}
                      onClick={() => handleStudioClick(studio?.id?.toString())}
                      className="button-slapp"
                      variant="contained"
                    >
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
              <Button variant="outlined" className="button-slapp-straped" onClick={onLoadMore} disabled={loadingMore}>
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
