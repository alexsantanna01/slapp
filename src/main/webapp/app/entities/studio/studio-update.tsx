import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntities as getCancellationPolicies } from 'app/entities/cancellation-policy/cancellation-policy.reducer';
import { createEntity, getEntity, reset, updateEntity } from './studio.reducer';
import OperatingHoursManager from './components/OperatingHoursManager';
import axios from 'axios';

interface OperatingHours {
  id?: number;
  dayOfWeek: string;
  startTime?: string;
  endTime?: string;
  isOpen: boolean;
  studioId?: number;
}

export const StudioUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const [operatingHours, setOperatingHours] = useState<OperatingHours[]>([]);
  const [operatingHoursSaving, setOperatingHoursSaving] = useState(false);

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const cancellationPolicies = useAppSelector(state => state.cancellationPolicy.entities);
  const studioEntity = useAppSelector(state => state.studio.entity);
  const loading = useAppSelector(state => state.studio.loading);
  const updating = useAppSelector(state => state.studio.updating);
  const updateSuccess = useAppSelector(state => state.studio.updateSuccess);

  const handleClose = () => {
    navigate(`/studio${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getCancellationPolicies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = async values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.latitude !== undefined && typeof values.latitude !== 'number') {
      values.latitude = Number(values.latitude);
    }
    if (values.longitude !== undefined && typeof values.longitude !== 'number') {
      values.longitude = Number(values.longitude);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...studioEntity,
      ...values,
      owner: userProfiles.find(it => it.id.toString() === values.owner?.toString()),
      cancellationPolicy: cancellationPolicies.find(it => it.id.toString() === values.cancellationPolicy?.toString()),
    };

    try {
      let studioResult;
      if (isNew) {
        studioResult = await dispatch(createEntity(entity)).unwrap();
      } else {
        studioResult = await dispatch(updateEntity(entity)).unwrap();
      }

      // Save operating hours after studio is saved
      if (studioResult && operatingHours.length > 0) {
        await saveOperatingHours(studioResult.id || id);
      }
    } catch (error) {
      console.error('Error saving studio:', error);
    }
  };

  const saveOperatingHours = async (studioId: string | number) => {
    if (!studioId || operatingHours.length === 0) return;

    setOperatingHoursSaving(true);
    try {
      const hoursToSave = operatingHours.map(oh => ({
        ...oh,
        studioId: Number(studioId),
      }));

      await axios.put(`/api/studio-operating-hours/studio/${studioId}`, hoursToSave);
    } catch (error) {
      console.error('Error saving operating hours:', error);
    } finally {
      setOperatingHoursSaving(false);
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...studioEntity,
          createdAt: convertDateTimeFromServer(studioEntity.createdAt),
          updatedAt: convertDateTimeFromServer(studioEntity.updatedAt),
          owner: studioEntity?.owner?.id,
          cancellationPolicy: studioEntity?.cancellationPolicy?.id,
        };

  const handleBack = () => {
    navigate(-1);
  };

  return (
    <div>
      <div
        style={{
          backgroundImage: `linear-gradient(rgba(77, 52, 36, 0.6), rgba(77, 52, 36, 0.6)), url(${studioEntity.image})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          height: '400px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: 'white',
          textAlign: 'center',
        }}
      >
        <div>
          <h1 style={{ fontSize: '3.5rem', fontWeight: 'bold', marginBottom: '1rem', textShadow: '2px 2px 4px rgba(0,0,0,0.7)' }}>
            {studioEntity.name}
          </h1>
          <p style={{ fontSize: '1.3rem', maxWidth: '600px', textShadow: '1px 1px 2px rgba(0,0,0,0.7)' }}>{studioEntity.description}</p>
        </div>
      </div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="slappApp.studio.home.createOrEditLabel" data-cy="StudioCreateUpdateHeading">
            <Translate contentKey="slappApp.studio.home.createOrEditLabel">Create or edit a Studio</Translate>
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
                  id="studio-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('slappApp.studio.name')}
                id="studio-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.description')}
                id="studio-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('slappApp.studio.address')}
                id="studio-address"
                name="address"
                data-cy="address"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.city')}
                id="studio-city"
                name="city"
                data-cy="city"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.state')}
                id="studio-state"
                name="state"
                data-cy="state"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.zipCode')}
                id="studio-zipCode"
                name="zipCode"
                data-cy="zipCode"
                type="text"
                validate={{
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.latitude')}
                id="studio-latitude"
                name="latitude"
                data-cy="latitude"
                type="text"
              />
              <ValidatedField
                label={translate('slappApp.studio.longitude')}
                id="studio-longitude"
                name="longitude"
                data-cy="longitude"
                type="text"
              />
              <ValidatedField
                label={translate('slappApp.studio.phone')}
                id="studio-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.email')}
                id="studio-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.website')}
                id="studio-website"
                name="website"
                data-cy="website"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.image')}
                id="studio-image"
                name="image"
                data-cy="image"
                type="text"
                validate={{
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <ValidatedField
                label={translate('slappApp.studio.active')}
                id="studio-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('slappApp.studio.createdAt')}
                id="studio-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('slappApp.studio.updatedAt')}
                id="studio-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="studio-owner"
                name="owner"
                data-cy="owner"
                label={translate('slappApp.studio.owner')}
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
                id="studio-cancellationPolicy"
                name="cancellationPolicy"
                data-cy="cancellationPolicy"
                label={translate('slappApp.studio.cancellationPolicy')}
                type="select"
              >
                <option value="" key="0" />
                {cancellationPolicies
                  ? cancellationPolicies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              {/* Operating Hours Section */}
              <div className="mt-4">
                <OperatingHoursManager
                  studioId={!isNew ? Number(id) : undefined}
                  isNew={isNew}
                  onOperatingHoursChange={setOperatingHours}
                />
              </div>
              <Row className="d-flex justify-content-between">
                <Button onClick={handleBack} replace className="button-slapp-voltar">
                  <FontAwesomeIcon icon="arrow-left" />{' '}
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Voltar</Translate>
                  </span>
                </Button>
                <Button className="button-slapp-editar" type="submit" disabled={updating || operatingHoursSaving}>
                  <FontAwesomeIcon icon="save" />{' '}
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.save">Save</Translate>
                  </span>
                </Button>
              </Row>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default StudioUpdate;
