import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="slappApp.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="slappApp.userProfile.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.phone}</dd>
          <dt>
            <span id="profileImage">
              <Translate contentKey="slappApp.userProfile.profileImage">Profile Image</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.profileImage}</dd>
          <dt>
            <span id="userType">
              <Translate contentKey="slappApp.userProfile.userType">User Type</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.userType}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="slappApp.userProfile.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.createdAt ? <TextFormat value={userProfileEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="slappApp.userProfile.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.updatedAt ? <TextFormat value={userProfileEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="slappApp.userProfile.user">User</Translate>
          </dt>
          <dd>{userProfileEntity.user ? userProfileEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
