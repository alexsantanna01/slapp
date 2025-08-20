import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './room-image.reducer';

export const RoomImage = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const roomImageList = useAppSelector(state => state.roomImage.entities);
  const loading = useAppSelector(state => state.roomImage.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="room-image-heading" data-cy="RoomImageHeading">
        <Translate contentKey="slappApp.roomImage.home.title">Room Images</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="slappApp.roomImage.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/room-image/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="slappApp.roomImage.home.createLabel">Create new Room Image</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {roomImageList && roomImageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="slappApp.roomImage.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('url')}>
                  <Translate contentKey="slappApp.roomImage.url">Url</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('url')} />
                </th>
                <th className="hand" onClick={sort('altText')}>
                  <Translate contentKey="slappApp.roomImage.altText">Alt Text</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('altText')} />
                </th>
                <th className="hand" onClick={sort('displayOrder')}>
                  <Translate contentKey="slappApp.roomImage.displayOrder">Display Order</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('displayOrder')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="slappApp.roomImage.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th>
                  <Translate contentKey="slappApp.roomImage.room">Room</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {roomImageList.map((roomImage, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/room-image/${roomImage.id}`} color="link" size="sm">
                      {roomImage.id}
                    </Button>
                  </td>
                  <td>{roomImage.url}</td>
                  <td>{roomImage.altText}</td>
                  <td>{roomImage.displayOrder}</td>
                  <td>{roomImage.active ? 'true' : 'false'}</td>
                  <td>{roomImage.room ? <Link to={`/room/${roomImage.room.id}`}>{roomImage.room.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/room-image/${roomImage.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/room-image/${roomImage.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/room-image/${roomImage.id}/delete`)}
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
              <Translate contentKey="slappApp.roomImage.home.notFound">No Room Images found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default RoomImage;
