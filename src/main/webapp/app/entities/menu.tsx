import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/studio">
        <Translate contentKey="global.menu.entities.studio" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/room">
        <Translate contentKey="global.menu.entities.room" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/room-image">
        <Translate contentKey="global.menu.entities.roomImage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/equipment">
        <Translate contentKey="global.menu.entities.equipment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reservation">
        <Translate contentKey="global.menu.entities.reservation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/review">
        <Translate contentKey="global.menu.entities.review" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/special-price">
        <Translate contentKey="global.menu.entities.specialPrice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/availability">
        <Translate contentKey="global.menu.entities.availability" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cancellation-policy">
        <Translate contentKey="global.menu.entities.cancellationPolicy" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
