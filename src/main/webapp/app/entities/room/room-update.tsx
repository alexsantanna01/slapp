import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getStudios } from 'app/entities/studio/studio.reducer';
import { RoomType } from 'app/shared/model/enumerations/room-type.model';
import { createEntity, getEntity, reset, updateEntity } from './room.reducer';

export const RoomUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const studios = useAppSelector(state => state.studio.entities);
  const roomEntity = useAppSelector(state => state.room.entity);
  const loading = useAppSelector(state => state.room.loading);
  const updating = useAppSelector(state => state.room.updating);
  const updateSuccess = useAppSelector(state => state.room.updateSuccess);
  const roomTypeValues = Object.keys(RoomType);

  const handleClose = () => {
    navigate(`/room${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStudios({}));
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
    if (values.hourlyRate !== undefined && typeof values.hourlyRate !== 'number') {
      values.hourlyRate = Number(values.hourlyRate);
    }
    if (values.capacity !== undefined && typeof values.capacity !== 'number') {
      values.capacity = Number(values.capacity);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...roomEntity,
      ...values,
      studio: studios.find(it => it.id.toString() === values.studio?.toString()),
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
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          roomType: 'RECORDING',
          ...roomEntity,
          createdAt: convertDateTimeFromServer(roomEntity.createdAt),
          updatedAt: convertDateTimeFromServer(roomEntity.updatedAt),
          studio: roomEntity?.studio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="slappApp.room.home.createOrEditLabel" data-cy="RoomCreateUpdateHeading">
            <Translate contentKey="slappApp.room.home.createOrEditLabel">Create or edit a Room</Translate>
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
                  id="room-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('slappApp.room.name')}
                id="room-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.room.description')}
                id="room-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('slappApp.room.hourlyRate')}
                id="room-hourlyRate"
                name="hourlyRate"
                data-cy="hourlyRate"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('slappApp.room.capacity')}
                id="room-capacity"
                name="capacity"
                data-cy="capacity"
                type="text"
                validate={{
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('slappApp.room.soundproofed')}
                id="room-soundproofed"
                name="soundproofed"
                data-cy="soundproofed"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('slappApp.room.airConditioning')}
                id="room-airConditioning"
                name="airConditioning"
                data-cy="airConditioning"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('slappApp.room.roomType')}
                id="room-roomType"
                name="roomType"
                data-cy="roomType"
                type="select"
              >
                {roomTypeValues.map(roomType => (
                  <option value={roomType} key={roomType}>
                    {translate(`slappApp.RoomType.${roomType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('slappApp.room.active')}
                id="room-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('slappApp.room.createdAt')}
                id="room-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('slappApp.room.updatedAt')}
                id="room-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="room-studio"
                name="studio"
                data-cy="studio"
                label={translate('slappApp.room.studio')}
                type="select"
                required
              >
                <option value="" key="0" />
                {studios
                  ? studios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/room" replace color="info">
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

export default RoomUpdate;
