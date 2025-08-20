import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './room.reducer';

export const RoomDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roomEntity = useAppSelector(state => state.room.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roomDetailsHeading">
          <Translate contentKey="slappApp.room.detail.title">Room</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{roomEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="slappApp.room.name">Name</Translate>
            </span>
          </dt>
          <dd>{roomEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="slappApp.room.description">Description</Translate>
            </span>
          </dt>
          <dd>{roomEntity.description}</dd>
          <dt>
            <span id="hourlyRate">
              <Translate contentKey="slappApp.room.hourlyRate">Hourly Rate</Translate>
            </span>
          </dt>
          <dd>{roomEntity.hourlyRate}</dd>
          <dt>
            <span id="capacity">
              <Translate contentKey="slappApp.room.capacity">Capacity</Translate>
            </span>
          </dt>
          <dd>{roomEntity.capacity}</dd>
          <dt>
            <span id="soundproofed">
              <Translate contentKey="slappApp.room.soundproofed">Soundproofed</Translate>
            </span>
          </dt>
          <dd>{roomEntity.soundproofed ? 'true' : 'false'}</dd>
          <dt>
            <span id="airConditioning">
              <Translate contentKey="slappApp.room.airConditioning">Air Conditioning</Translate>
            </span>
          </dt>
          <dd>{roomEntity.airConditioning ? 'true' : 'false'}</dd>
          <dt>
            <span id="roomType">
              <Translate contentKey="slappApp.room.roomType">Room Type</Translate>
            </span>
          </dt>
          <dd>{roomEntity.roomType}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="slappApp.room.active">Active</Translate>
            </span>
          </dt>
          <dd>{roomEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="slappApp.room.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{roomEntity.createdAt ? <TextFormat value={roomEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="slappApp.room.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{roomEntity.updatedAt ? <TextFormat value={roomEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="slappApp.room.studio">Studio</Translate>
          </dt>
          <dd>{roomEntity.studio ? roomEntity.studio.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/room/${roomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoomDetail;
