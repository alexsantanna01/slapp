import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntities as getRooms } from 'app/entities/room/room.reducer';
import { ReservationStatus } from 'app/shared/model/enumerations/reservation-status.model';
import { createEntity, getEntity, reset, updateEntity } from './reservation.reducer';

export const ReservationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const rooms = useAppSelector(state => state.room.entities);
  const reservationEntity = useAppSelector(state => state.reservation.entity);
  const loading = useAppSelector(state => state.reservation.loading);
  const updating = useAppSelector(state => state.reservation.updating);
  const updateSuccess = useAppSelector(state => state.reservation.updateSuccess);
  const reservationStatusValues = Object.keys(ReservationStatus);

  const handleClose = () => {
    navigate(`/reservation${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getRooms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.startDateTime = convertDateTimeToServer(values.startDateTime);
    values.endDateTime = convertDateTimeToServer(values.endDateTime);
    if (values.totalPrice !== undefined && typeof values.totalPrice !== 'number') {
      values.totalPrice = Number(values.totalPrice);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.cancelledAt = convertDateTimeToServer(values.cancelledAt);

    const entity = {
      ...reservationEntity,
      ...values,
      customer: userProfiles.find(it => it.id.toString() === values.customer?.toString()),
      room: rooms.find(it => it.id.toString() === values.room?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startDateTime: displayDefaultDateTime(),
          endDateTime: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
          cancelledAt: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...reservationEntity,
          startDateTime: convertDateTimeFromServer(reservationEntity.startDateTime),
          endDateTime: convertDateTimeFromServer(reservationEntity.endDateTime),
          createdAt: convertDateTimeFromServer(reservationEntity.createdAt),
          updatedAt: convertDateTimeFromServer(reservationEntity.updatedAt),
          cancelledAt: convertDateTimeFromServer(reservationEntity.cancelledAt),
          customer: reservationEntity?.customer?.id,
          room: reservationEntity?.room?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="slappApp.reservation.home.createOrEditLabel" data-cy="ReservationCreateUpdateHeading">
            <Translate contentKey="slappApp.reservation.home.createOrEditLabel">Create or edit a Reservation</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="reservation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('slappApp.reservation.startDateTime')}
                id="reservation-startDateTime"
                name="startDateTime"
                data-cy="startDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('slappApp.reservation.endDateTime')}
                id="reservation-endDateTime"
                name="endDateTime"
                data-cy="endDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('slappApp.reservation.totalPrice')}
                id="reservation-totalPrice"
                name="totalPrice"
                data-cy="totalPrice"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('slappApp.reservation.status')}
                id="reservation-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {reservationStatusValues.map(reservationStatus => (
                  <option value={reservationStatus} key={reservationStatus}>
                    {translate(`slappApp.ReservationStatus.${reservationStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('slappApp.reservation.notes')}
                id="reservation-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                label={translate('slappApp.reservation.createdAt')}
                id="reservation-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('slappApp.reservation.updatedAt')}
                id="reservation-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('slappApp.reservation.cancelledAt')}
                id="reservation-cancelledAt"
                name="cancelledAt"
                data-cy="cancelledAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('slappApp.reservation.cancelReason')}
                id="reservation-cancelReason"
                name="cancelReason"
                data-cy="cancelReason"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                id="reservation-customer"
                name="customer"
                data-cy="customer"
                label={translate('slappApp.reservation.customer')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="reservation-room"
                name="room"
                data-cy="room"
                label={translate('slappApp.reservation.room')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rooms
                  ? rooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reservation" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReservationUpdate;
