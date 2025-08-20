import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getRooms } from 'app/entities/room/room.reducer';
import { DayOfWeek } from 'app/shared/model/enumerations/day-of-week.model';
import { createEntity, getEntity, reset, updateEntity } from './special-price.reducer';

export const SpecialPriceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const rooms = useAppSelector(state => state.room.entities);
  const specialPriceEntity = useAppSelector(state => state.specialPrice.entity);
  const loading = useAppSelector(state => state.specialPrice.loading);
  const updating = useAppSelector(state => state.specialPrice.updating);
  const updateSuccess = useAppSelector(state => state.specialPrice.updateSuccess);
  const dayOfWeekValues = Object.keys(DayOfWeek);

  const handleClose = () => {
    navigate('/special-price');
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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }

    const entity = {
      ...specialPriceEntity,
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
      ? {}
      : {
          dayOfWeek: 'MONDAY',
          ...specialPriceEntity,
          room: specialPriceEntity?.room?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="slappApp.specialPrice.home.createOrEditLabel" data-cy="SpecialPriceCreateUpdateHeading">
            <Translate contentKey="slappApp.specialPrice.home.createOrEditLabel">Create or edit a SpecialPrice</Translate>
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
                  id="special-price-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('slappApp.specialPrice.dayOfWeek')}
                id="special-price-dayOfWeek"
                name="dayOfWeek"
                data-cy="dayOfWeek"
                type="select"
              >
                {dayOfWeekValues.map(dayOfWeek => (
                  <option value={dayOfWeek} key={dayOfWeek}>
                    {translate(`slappApp.DayOfWeek.${dayOfWeek}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('slappApp.specialPrice.startTime')}
                id="special-price-startTime"
                name="startTime"
                data-cy="startTime"
                type="time"
                placeholder="HH:mm"
              />
              <ValidatedField
                label={translate('slappApp.specialPrice.endTime')}
                id="special-price-endTime"
                name="endTime"
                data-cy="endTime"
                type="time"
                placeholder="HH:mm"
              />
              <ValidatedField
                label={translate('slappApp.specialPrice.price')}
                id="special-price-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('slappApp.specialPrice.description')}
                id="special-price-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.specialPrice.active')}
                id="special-price-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                id="special-price-room"
                name="room"
                data-cy="room"
                label={translate('slappApp.specialPrice.room')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/special-price" replace color="info">
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

export default SpecialPriceUpdate;
