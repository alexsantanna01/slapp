import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './studio.reducer';

export const StudioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studioEntity = useAppSelector(state => state.studio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studioDetailsHeading">
          <Translate contentKey="slappApp.studio.detail.title">Studio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studioEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="slappApp.studio.name">Name</Translate>
            </span>
          </dt>
          <dd>{studioEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="slappApp.studio.description">Description</Translate>
            </span>
          </dt>
          <dd>{studioEntity.description}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="slappApp.studio.address">Address</Translate>
            </span>
          </dt>
          <dd>{studioEntity.address}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="slappApp.studio.city">City</Translate>
            </span>
          </dt>
          <dd>{studioEntity.city}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="slappApp.studio.state">State</Translate>
            </span>
          </dt>
          <dd>{studioEntity.state}</dd>
          <dt>
            <span id="zipCode">
              <Translate contentKey="slappApp.studio.zipCode">Zip Code</Translate>
            </span>
          </dt>
          <dd>{studioEntity.zipCode}</dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="slappApp.studio.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{studioEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="slappApp.studio.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{studioEntity.longitude}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="slappApp.studio.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{studioEntity.phone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="slappApp.studio.email">Email</Translate>
            </span>
          </dt>
          <dd>{studioEntity.email}</dd>
          <dt>
            <span id="website">
              <Translate contentKey="slappApp.studio.website">Website</Translate>
            </span>
          </dt>
          <dd>{studioEntity.website}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="slappApp.studio.image">Image</Translate>
            </span>
          </dt>
          <dd>{studioEntity.image}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="slappApp.studio.active">Active</Translate>
            </span>
          </dt>
          <dd>{studioEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="slappApp.studio.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{studioEntity.createdAt ? <TextFormat value={studioEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="slappApp.studio.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{studioEntity.updatedAt ? <TextFormat value={studioEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="slappApp.studio.owner">Owner</Translate>
          </dt>
          <dd>{studioEntity.owner ? studioEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="slappApp.studio.cancellationPolicy">Cancellation Policy</Translate>
          </dt>
          <dd>{studioEntity.cancellationPolicy ? studioEntity.cancellationPolicy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/studio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/studio/${studioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudioDetail;
