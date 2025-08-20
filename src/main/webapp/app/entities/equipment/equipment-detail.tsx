import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './equipment.reducer';

export const EquipmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const equipmentEntity = useAppSelector(state => state.equipment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="equipmentDetailsHeading">
          <Translate contentKey="slappApp.equipment.detail.title">Equipment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="slappApp.equipment.name">Name</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.name}</dd>
          <dt>
            <span id="brand">
              <Translate contentKey="slappApp.equipment.brand">Brand</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.brand}</dd>
          <dt>
            <span id="model">
              <Translate contentKey="slappApp.equipment.model">Model</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.model}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="slappApp.equipment.description">Description</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.description}</dd>
          <dt>
            <span id="available">
              <Translate contentKey="slappApp.equipment.available">Available</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.available ? 'true' : 'false'}</dd>
          <dt>
            <span id="equipmentType">
              <Translate contentKey="slappApp.equipment.equipmentType">Equipment Type</Translate>
            </span>
          </dt>
          <dd>{equipmentEntity.equipmentType}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="slappApp.equipment.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {equipmentEntity.createdAt ? <TextFormat value={equipmentEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="slappApp.equipment.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {equipmentEntity.updatedAt ? <TextFormat value={equipmentEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="slappApp.equipment.room">Room</Translate>
          </dt>
          <dd>{equipmentEntity.room ? equipmentEntity.room.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/equipment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/equipment/${equipmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EquipmentDetail;
