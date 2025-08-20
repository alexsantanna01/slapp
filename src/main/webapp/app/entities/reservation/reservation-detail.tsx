import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reservation.reducer';

export const ReservationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservationEntity = useAppSelector(state => state.reservation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservationDetailsHeading">
          <Translate contentKey="slappApp.reservation.detail.title">Reservation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.id}</dd>
          <dt>
            <span id="startDateTime">
              <Translate contentKey="slappApp.reservation.startDateTime">Start Date Time</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.startDateTime ? (
              <TextFormat value={reservationEntity.startDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDateTime">
              <Translate contentKey="slappApp.reservation.endDateTime">End Date Time</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.endDateTime ? (
              <TextFormat value={reservationEntity.endDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="totalPrice">
              <Translate contentKey="slappApp.reservation.totalPrice">Total Price</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.totalPrice}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="slappApp.reservation.status">Status</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.status}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="slappApp.reservation.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.notes}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="slappApp.reservation.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.createdAt ? <TextFormat value={reservationEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="slappApp.reservation.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.updatedAt ? <TextFormat value={reservationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="cancelledAt">
              <Translate contentKey="slappApp.reservation.cancelledAt">Cancelled At</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.cancelledAt ? (
              <TextFormat value={reservationEntity.cancelledAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="cancelReason">
              <Translate contentKey="slappApp.reservation.cancelReason">Cancel Reason</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.cancelReason}</dd>
          <dt>
            <Translate contentKey="slappApp.reservation.customer">Customer</Translate>
          </dt>
          <dd>{reservationEntity.customer ? reservationEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="slappApp.reservation.room">Room</Translate>
          </dt>
          <dd>{reservationEntity.room ? reservationEntity.room.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reservation/${reservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservationDetail;
