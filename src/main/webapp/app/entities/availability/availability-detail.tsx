import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './availability.reducer';

export const AvailabilityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const availabilityEntity = useAppSelector(state => state.availability.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="availabilityDetailsHeading">
          <Translate contentKey="slappApp.availability.detail.title">Availability</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{availabilityEntity.id}</dd>
          <dt>
            <span id="startDateTime">
              <Translate contentKey="slappApp.availability.startDateTime">Start Date Time</Translate>
            </span>
          </dt>
          <dd>
            {availabilityEntity.startDateTime ? (
              <TextFormat value={availabilityEntity.startDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDateTime">
              <Translate contentKey="slappApp.availability.endDateTime">End Date Time</Translate>
            </span>
          </dt>
          <dd>
            {availabilityEntity.endDateTime ? (
              <TextFormat value={availabilityEntity.endDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="available">
              <Translate contentKey="slappApp.availability.available">Available</Translate>
            </span>
          </dt>
          <dd>{availabilityEntity.available ? 'true' : 'false'}</dd>
          <dt>
            <span id="reason">
              <Translate contentKey="slappApp.availability.reason">Reason</Translate>
            </span>
          </dt>
          <dd>{availabilityEntity.reason}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="slappApp.availability.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {availabilityEntity.createdAt ? <TextFormat value={availabilityEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="slappApp.availability.room">Room</Translate>
          </dt>
          <dd>{availabilityEntity.room ? availabilityEntity.room.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/availability" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/availability/${availabilityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AvailabilityDetail;
