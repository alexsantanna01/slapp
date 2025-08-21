import React, { useCallback, useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Col, Container, Label, Row, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Box, Typography, Grid } from '@mui/material';
import { styled } from '@mui/system';

import { getEntities, getStudioPagination, loadMoreStudios, resetPagination } from 'app/entities/studio/studio.reducer';
import StudioSearch from './studio-search';
import StudioList from './studio-list';
import { toast } from 'react-toastify';

const SearchSection = styled(Box)(({ theme }) => ({
  padding: '40px 0',
  background: 'var(--background-input)',
}));

// Interface para filtros (atualizada)
export interface ISearchFilters {
  page?: number;
  size?: number;
  sort?: string;
  name?: string;
  city?: string;
  roomType?: string; // 'RECORDING' | 'REHEARSAL' | 'BOTH'
  minPrice?: number;
  maxPrice?: number;
}

export const Studio = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  // Estados do Studio (com load more)
  const { loadingMore, hasMore, currentPage, errorMessage } = useAppSelector(state => state.studio);
  const estudioEntities = useAppSelector(state => state.studio.entities);
  const loading = useAppSelector(state => state.studio.loading);
  const totalItems = useAppSelector(state => state.studio.totalItems);
  const account = useAppSelector(state => state.authentication.account);
  // Estado para controlar se é primeira carga
  const [isInitialLoad, setIsInitialLoad] = useState(true);

  // Estado dummy para compatibilidade com StudioSearch
  const [page, setPage] = useState<number>(0);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [filters, setFilters] = useState<ISearchFilters>({
    name: '',
    city: '',
    roomType: '',
    minPrice: 0,
    maxPrice: 1000,
    page: 0,
    size: 6,
    sort: 'name,asc',
  });

  // Função para buscar estudios (primeira carga ou filtros)
  const fetchStudios = useCallback(() => {
    if (account !== undefined && account.login !== undefined) {
      dispatch(resetPagination());
      dispatch(
        getStudioPagination({
          filters,
          sort: 'name,asc',
        }),
      );
    }
  }, [dispatch, account, filters]);

  useEffect(() => {
    fetchStudios();
  }, [filters]);

  // Função para alterar filtros
  const handleInputChange = useCallback(
    (field: keyof ISearchFilters, value: any) => {
      const newFilters = {
        ...filters,
        [field]: value === '' ? undefined : value,
      };
      setFilters(newFilters);
    },
    [filters],
  );

  // Função para carregar mais estudios
  const handleLoadMore = useCallback(() => {
    if (!loadingMore && hasMore && account !== undefined) {
      dispatch(
        loadMoreStudios({
          filters,
          currentPage,
          sort: 'name,asc',
        }),
      );
    }
  }, [dispatch, filters, currentPage, loadingMore, hasMore, account]);

  // Effect para recarregar quando filtros mudam
  useEffect(() => {
    if (account !== undefined && !isInitialLoad) {
      // Debounce para evitar muitas requisições
      const timeoutId = setTimeout(() => {
        fetchStudios();
      }, 500);

      return () => clearTimeout(timeoutId);
    }
  }, [filters, account, isInitialLoad, fetchStudios]);

  useEffect(() => {
    fetchStudios();
  }, []);

  // useEffect(() => {
  //   const params = new URLSearchParams(pageLocation.search);
  //   const pages = params.get('pages');
  //   const sort = params.get(SORT);
  //   if (pages && sort) {
  //     const sortSplit = sort.split(',');
  //     setPaginationState({
  //       ...paginationState,
  //       activePage: +pages,
  //       sort: sortSplit[0],
  //       order: sortSplit[1],
  //     });
  //   }
  // }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  return (
    <Container className="studio-page">
      {/* Seção de Busca */}
      <SearchSection className="home-page">
        <Container className="studio-page">
          <Typography variant="h4" gutterBottom align="center">
            Encontre seu estúdio ideal.
          </Typography>
          <Grid container spacing={3}>
            <StudioSearch filters={filters} setFilters={setFilters} handleInputChange={handleInputChange} page={page} setPage={setPage} />
          </Grid>
        </Container>
      </SearchSection>

      {/* Lista de Estúdios */}
      <Container sx={{ py: 4 }} mb={4} mt={4}>
        <Row container spacing={4} mb={4} mt={4}>
          {/* Indicador de carregamento inicial */}
          {loading ? (
            <div className="w-100 text-center py-5">
              <div className="spinner-border text-primary" role="status">
                <span className="sr-only">Carregando estúdios...</span>
              </div>
              <div className="mt-2">
                <Label variant="body2" color="textSecondary">
                  Buscando os melhores estúdios para você...
                </Label>
              </div>
            </div>
          ) : (
            <StudioList
              estudioEntities={estudioEntities}
              totalItems={totalItems}
              hasMore={hasMore}
              loadingMore={loadingMore}
              onLoadMore={handleLoadMore}
            />
          )}

          {/* Mensagem de erro */}
          {errorMessage && (
            <div className="w-100">
              <div className="alert alert-warning text-center">
                <i className="fas fa-exclamation-triangle mr-2"></i>
                Ocorreu um erro ao carregar os estúdios. Tente novamente.
              </div>
            </div>
          )}
        </Row>
      </Container>

      {/* Seção de Features */}
      <Container sx={{ py: 4 }}>
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

export default Studio;
