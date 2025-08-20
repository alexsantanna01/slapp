import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './cancellation-policy.reducer';

export const CancellationPolicy = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const cancellationPolicyList = useAppSelector(state => state.cancellationPolicy.entities);
  const loading = useAppSelector(state => state.cancellationPolicy.loading);

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
      <h2 id="cancellation-policy-heading" data-cy="CancellationPolicyHeading">
        <Translate contentKey="slappApp.cancellationPolicy.home.title">Cancellation Policies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="slappApp.cancellationPolicy.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/cancellation-policy/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="slappApp.cancellationPolicy.home.createLabel">Create new Cancellation Policy</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cancellationPolicyList && cancellationPolicyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="slappApp.cancellationPolicy.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="slappApp.cancellationPolicy.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="slappApp.cancellationPolicy.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('hoursBeforeEvent')}>
                  <Translate contentKey="slappApp.cancellationPolicy.hoursBeforeEvent">Hours Before Event</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('hoursBeforeEvent')} />
                </th>
                <th className="hand" onClick={sort('refundPercentage')}>
                  <Translate contentKey="slappApp.cancellationPolicy.refundPercentage">Refund Percentage</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('refundPercentage')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="slappApp.cancellationPolicy.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cancellationPolicyList.map((cancellationPolicy, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/cancellation-policy/${cancellationPolicy.id}`} color="link" size="sm">
                      {cancellationPolicy.id}
                    </Button>
                  </td>
                  <td>{cancellationPolicy.name}</td>
                  <td>{cancellationPolicy.description}</td>
                  <td>{cancellationPolicy.hoursBeforeEvent}</td>
                  <td>{cancellationPolicy.refundPercentage}</td>
                  <td>{cancellationPolicy.active ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/cancellation-policy/${cancellationPolicy.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/cancellation-policy/${cancellationPolicy.id}/edit`}
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
                        onClick={() => (window.location.href = `/cancellation-policy/${cancellationPolicy.id}/delete`)}
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
              <Translate contentKey="slappApp.cancellationPolicy.home.notFound">No Cancellation Policies found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CancellationPolicy;
