import React from 'react';

import { DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Avatar } from '@mui/material';
import { useCurrentUserProfile } from 'app/shared/util/user-profile-util';

export const NavDropdown = props => {
  const { userProfile } = useCurrentUserProfile();

  // Use Avatar apenas para o menu de conta (identificado pelo ícone "user")
  const shouldUseAvatar = props.icon === 'user';

  return (
    <UncontrolledDropdown nav inNavbar id={props.id} data-cy={props['data-cy']}>
      <DropdownToggle nav caret className="d-flex align-items-center">
        {shouldUseAvatar ? (
          <Avatar
            src={userProfile?.profileImage || undefined}
            alt={props.name}
            sx={{
              width: 24,
              height: 24,
              fontSize: '0.875rem',
              mr: 1,
            }}
          >
            {!userProfile?.profileImage && props.name?.charAt(0)?.toUpperCase()}
          </Avatar>
        ) : (
          <>
            <FontAwesomeIcon icon={props.icon} />
            <span>{props.name}</span>
          </>
        )}
      </DropdownToggle>
      <DropdownMenu end style={props.style}>
        {props.children}
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};
