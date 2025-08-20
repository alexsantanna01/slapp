import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './cancellation-policy.reducer';

export const CancellationPolicyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cancellationPolicyEntity = useAppSelector(state => state.cancellationPolicy.entity);
  const loading = useAppSelector(state => state.cancellationPolicy.loading);
  const updating = useAppSelector(state => state.cancellationPolicy.updating);
  const updateSuccess = useAppSelector(state => state.cancellationPolicy.updateSuccess);

  const handleClose = () => {
    navigate('/cancellation-policy');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.hoursBeforeEvent !== undefined && typeof values.hoursBeforeEvent !== 'number') {
      values.hoursBeforeEvent = Number(values.hoursBeforeEvent);
    }
    if (values.refundPercentage !== undefined && typeof values.refundPercentage !== 'number') {
      values.refundPercentage = Number(values.refundPercentage);
    }

    const entity = {
      ...cancellationPolicyEntity,
      ...values,
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
          ...cancellationPolicyEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="slappApp.cancellationPolicy.home.createOrEditLabel" data-cy="CancellationPolicyCreateUpdateHeading">
            <Translate contentKey="slappApp.cancellationPolicy.home.createOrEditLabel">Create or edit a CancellationPolicy</Translate>
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
                  id="cancellation-policy-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('slappApp.cancellationPolicy.name')}
                id="cancellation-policy-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.cancellationPolicy.description')}
                id="cancellation-policy-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('slappApp.cancellationPolicy.hoursBeforeEvent')}
                id="cancellation-policy-hoursBeforeEvent"
                name="hoursBeforeEvent"
                data-cy="hoursBeforeEvent"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('slappApp.cancellationPolicy.refundPercentage')}
                id="cancellation-policy-refundPercentage"
                name="refundPercentage"
                data-cy="refundPercentage"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('slappApp.cancellationPolicy.active')}
                id="cancellation-policy-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cancellation-policy" replace color="info">
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

export default CancellationPolicyUpdate;
