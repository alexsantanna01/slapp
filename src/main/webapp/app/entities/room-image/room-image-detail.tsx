import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './room-image.reducer';

export const RoomImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roomImageEntity = useAppSelector(state => state.roomImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roomImageDetailsHeading">
          <Translate contentKey="slappApp.roomImage.detail.title">RoomImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{roomImageEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="slappApp.roomImage.url">Url</Translate>
            </span>
          </dt>
          <dd>{roomImageEntity.url}</dd>
          <dt>
            <span id="altText">
              <Translate contentKey="slappApp.roomImage.altText">Alt Text</Translate>
            </span>
          </dt>
          <dd>{roomImageEntity.altText}</dd>
          <dt>
            <span id="displayOrder">
              <Translate contentKey="slappApp.roomImage.displayOrder">Display Order</Translate>
            </span>
          </dt>
          <dd>{roomImageEntity.displayOrder}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="slappApp.roomImage.active">Active</Translate>
            </span>
          </dt>
          <dd>{roomImageEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="slappApp.roomImage.room">Room</Translate>
          </dt>
          <dd>{roomImageEntity.room ? roomImageEntity.room.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/room-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/room-image/${roomImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoomImageDetail;
