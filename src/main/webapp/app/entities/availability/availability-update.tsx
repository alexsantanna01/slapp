import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getRooms } from 'app/entities/room/room.reducer';
import { createEntity, getEntity, reset, updateEntity } from './availability.reducer';

export const AvailabilityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const rooms = useAppSelector(state => state.room.entities);
  const availabilityEntity = useAppSelector(state => state.availability.entity);
  const loading = useAppSelector(state => state.availability.loading);
  const updating = useAppSelector(state => state.availability.updating);
  const updateSuccess = useAppSelector(state => state.availability.updateSuccess);

  const handleClose = () => {
    navigate('/availability');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...availabilityEntity,
      ...values,
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
        }
      : {
          ...availabilityEntity,
          startDateTime: convertDateTimeFromServer(availabilityEntity.startDateTime),
          endDateTime: convertDateTimeFromServer(availabilityEntity.endDateTime),
          createdAt: convertDateTimeFromServer(availabilityEntity.createdAt),
          room: availabilityEntity?.room?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="slappApp.availability.home.createOrEditLabel" data-cy="AvailabilityCreateUpdateHeading">
            <Translate contentKey="slappApp.availability.home.createOrEditLabel">Create or edit a Availability</Translate>
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
                  id="availability-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('slappApp.availability.startDateTime')}
                id="availability-startDateTime"
                name="startDateTime"
                data-cy="startDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('slappApp.availability.endDateTime')}
                id="availability-endDateTime"
                name="endDateTime"
                data-cy="endDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('slappApp.availability.available')}
                id="availability-available"
                name="available"
                data-cy="available"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('slappApp.availability.reason')}
                id="availability-reason"
                name="reason"
                data-cy="reason"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.availability.createdAt')}
                id="availability-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="availability-room"
                name="room"
                data-cy="room"
                label={translate('slappApp.availability.room')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/availability" replace color="info">
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

export default AvailabilityUpdate;
