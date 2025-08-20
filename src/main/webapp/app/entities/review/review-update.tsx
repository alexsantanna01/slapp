import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntities as getStudios } from 'app/entities/studio/studio.reducer';
import { getEntities as getReservations } from 'app/entities/reservation/reservation.reducer';
import { createEntity, getEntity, reset, updateEntity } from './review.reducer';

export const ReviewUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const studios = useAppSelector(state => state.studio.entities);
  const reservations = useAppSelector(state => state.reservation.entities);
  const reviewEntity = useAppSelector(state => state.review.entity);
  const loading = useAppSelector(state => state.review.loading);
  const updating = useAppSelector(state => state.review.updating);
  const updateSuccess = useAppSelector(state => state.review.updateSuccess);

  const handleClose = () => {
    navigate(`/review${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getStudios({}));
    dispatch(getReservations({}));
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
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...reviewEntity,
      ...values,
      customer: userProfiles.find(it => it.id.toString() === values.customer?.toString()),
      studio: studios.find(it => it.id.toString() === values.studio?.toString()),
      reservation: reservations.find(it => it.id.toString() === values.reservation?.toString()),
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
          ...reviewEntity,
          createdAt: convertDateTimeFromServer(reviewEntity.createdAt),
          updatedAt: convertDateTimeFromServer(reviewEntity.updatedAt),
          customer: reviewEntity?.customer?.id,
          studio: reviewEntity?.studio?.id,
          reservation: reviewEntity?.reservation?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="slappApp.review.home.createOrEditLabel" data-cy="ReviewCreateUpdateHeading">
            <Translate contentKey="slappApp.review.home.createOrEditLabel">Create or edit a Review</Translate>
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
                  id="review-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('slappApp.review.rating')}
                id="review-rating"
                name="rating"
                data-cy="rating"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('slappApp.review.comment')}
                id="review-comment"
                name="comment"
                data-cy="comment"
                type="textarea"
              />
              <ValidatedField
                label={translate('slappApp.review.createdAt')}
                id="review-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('slappApp.review.updatedAt')}
                id="review-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="review-customer"
                name="customer"
                data-cy="customer"
                label={translate('slappApp.review.customer')}
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
                id="review-studio"
                name="studio"
                data-cy="studio"
                label={translate('slappApp.review.studio')}
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
              <ValidatedField
                id="review-reservation"
                name="reservation"
                data-cy="reservation"
                label={translate('slappApp.review.reservation')}
                type="select"
              >
                <option value="" key="0" />
                {reservations
                  ? reservations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/review" replace color="info">
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

export default ReviewUpdate;
