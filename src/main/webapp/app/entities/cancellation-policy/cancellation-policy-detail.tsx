import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cancellation-policy.reducer';

export const CancellationPolicyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cancellationPolicyEntity = useAppSelector(state => state.cancellationPolicy.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cancellationPolicyDetailsHeading">
          <Translate contentKey="slappApp.cancellationPolicy.detail.title">CancellationPolicy</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cancellationPolicyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="slappApp.cancellationPolicy.name">Name</Translate>
            </span>
          </dt>
          <dd>{cancellationPolicyEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="slappApp.cancellationPolicy.description">Description</Translate>
            </span>
          </dt>
          <dd>{cancellationPolicyEntity.description}</dd>
          <dt>
            <span id="hoursBeforeEvent">
              <Translate contentKey="slappApp.cancellationPolicy.hoursBeforeEvent">Hours Before Event</Translate>
            </span>
          </dt>
          <dd>{cancellationPolicyEntity.hoursBeforeEvent}</dd>
          <dt>
            <span id="refundPercentage">
              <Translate contentKey="slappApp.cancellationPolicy.refundPercentage">Refund Percentage</Translate>
            </span>
          </dt>
          <dd>{cancellationPolicyEntity.refundPercentage}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="slappApp.cancellationPolicy.active">Active</Translate>
            </span>
          </dt>
          <dd>{cancellationPolicyEntity.active ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/cancellation-policy" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cancellation-policy/${cancellationPolicyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CancellationPolicyDetail;
