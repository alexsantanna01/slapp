import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './review.reducer';

export const ReviewDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reviewEntity = useAppSelector(state => state.review.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reviewDetailsHeading">
          <Translate contentKey="slappApp.review.detail.title">Review</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.id}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="slappApp.review.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.rating}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="slappApp.review.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.comment}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="slappApp.review.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.createdAt ? <TextFormat value={reviewEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="slappApp.review.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{reviewEntity.updatedAt ? <TextFormat value={reviewEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="slappApp.review.customer">Customer</Translate>
          </dt>
          <dd>{reviewEntity.customer ? reviewEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="slappApp.review.studio">Studio</Translate>
          </dt>
          <dd>{reviewEntity.studio ? reviewEntity.studio.id : ''}</dd>
          <dt>
            <Translate contentKey="slappApp.review.reservation">Reservation</Translate>
          </dt>
          <dd>{reviewEntity.reservation ? reviewEntity.reservation.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/review" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/review/${reviewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReviewDetail;
