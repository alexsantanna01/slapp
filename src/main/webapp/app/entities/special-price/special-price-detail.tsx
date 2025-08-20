import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './special-price.reducer';

export const SpecialPriceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specialPriceEntity = useAppSelector(state => state.specialPrice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specialPriceDetailsHeading">
          <Translate contentKey="slappApp.specialPrice.detail.title">SpecialPrice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{specialPriceEntity.id}</dd>
          <dt>
            <span id="dayOfWeek">
              <Translate contentKey="slappApp.specialPrice.dayOfWeek">Day Of Week</Translate>
            </span>
          </dt>
          <dd>{specialPriceEntity.dayOfWeek}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="slappApp.specialPrice.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{specialPriceEntity.startTime}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="slappApp.specialPrice.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{specialPriceEntity.endTime}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="slappApp.specialPrice.price">Price</Translate>
            </span>
          </dt>
          <dd>{specialPriceEntity.price}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="slappApp.specialPrice.description">Description</Translate>
            </span>
          </dt>
          <dd>{specialPriceEntity.description}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="slappApp.specialPrice.active">Active</Translate>
            </span>
          </dt>
          <dd>{specialPriceEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="slappApp.specialPrice.room">Room</Translate>
          </dt>
          <dd>{specialPriceEntity.room ? specialPriceEntity.room.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/special-price" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/special-price/${specialPriceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecialPriceDetail;
