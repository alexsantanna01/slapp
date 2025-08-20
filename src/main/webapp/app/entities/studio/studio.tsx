import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './studio.reducer';

export const Studio = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const studioList = useAppSelector(state => state.studio.entities);
  const loading = useAppSelector(state => state.studio.loading);
  const totalItems = useAppSelector(state => state.studio.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="studio-heading" data-cy="StudioHeading">
        <Translate contentKey="slappApp.studio.home.title">Studios</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="slappApp.studio.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/studio/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="slappApp.studio.home.createLabel">Create new Studio</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {studioList && studioList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="slappApp.studio.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="slappApp.studio.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="slappApp.studio.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('address')}>
                  <Translate contentKey="slappApp.studio.address">Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('address')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="slappApp.studio.city">City</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('state')}>
                  <Translate contentKey="slappApp.studio.state">State</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('state')} />
                </th>
                <th className="hand" onClick={sort('zipCode')}>
                  <Translate contentKey="slappApp.studio.zipCode">Zip Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('zipCode')} />
                </th>
                <th className="hand" onClick={sort('latitude')}>
                  <Translate contentKey="slappApp.studio.latitude">Latitude</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('latitude')} />
                </th>
                <th className="hand" onClick={sort('longitude')}>
                  <Translate contentKey="slappApp.studio.longitude">Longitude</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('longitude')} />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  <Translate contentKey="slappApp.studio.phone">Phone</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('phone')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="slappApp.studio.email">Email</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('website')}>
                  <Translate contentKey="slappApp.studio.website">Website</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('website')} />
                </th>
                <th className="hand" onClick={sort('image')}>
                  <Translate contentKey="slappApp.studio.image">Image</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('image')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="slappApp.studio.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="slappApp.studio.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="slappApp.studio.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th>
                  <Translate contentKey="slappApp.studio.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="slappApp.studio.cancellationPolicy">Cancellation Policy</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {studioList.map((studio, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/studio/${studio.id}`} color="link" size="sm">
                      {studio.id}
                    </Button>
                  </td>
                  <td>{studio.name}</td>
                  <td>{studio.description}</td>
                  <td>{studio.address}</td>
                  <td>{studio.city}</td>
                  <td>{studio.state}</td>
                  <td>{studio.zipCode}</td>
                  <td>{studio.latitude}</td>
                  <td>{studio.longitude}</td>
                  <td>{studio.phone}</td>
                  <td>{studio.email}</td>
                  <td>{studio.website}</td>
                  <td>{studio.image}</td>
                  <td>{studio.active ? 'true' : 'false'}</td>
                  <td>{studio.createdAt ? <TextFormat type="date" value={studio.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{studio.updatedAt ? <TextFormat type="date" value={studio.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{studio.owner ? <Link to={`/user-profile/${studio.owner.id}`}>{studio.owner.id}</Link> : ''}</td>
                  <td>
                    {studio.cancellationPolicy ? (
                      <Link to={`/cancellation-policy/${studio.cancellationPolicy.id}`}>{studio.cancellationPolicy.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/studio/${studio.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/studio/${studio.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/studio/${studio.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="slappApp.studio.home.notFound">No Studios found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={studioList && studioList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Studio;
