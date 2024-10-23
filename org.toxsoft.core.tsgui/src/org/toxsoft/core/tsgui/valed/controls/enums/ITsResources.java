package org.toxsoft.core.tsgui.valed.controls.enums;

import org.toxsoft.core.tsgui.utils.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String FMT_ERR_CLASS_NOT_VIS_PROVIDER   =
      Messages.getString( "FMT_ERR_CLASS_NOT_VIS_PROVIDER" ) + ITsVisualsProvider.class.getSimpleName(); //$NON-NLS-1$
  String FMT_ERR_CLASS_NOT_ENUM           = Messages.getString( "FMT_ERR_CLASS_NOT_ENUM" );              //$NON-NLS-1$
  String MSG_ERR_ENUM_CLASS_NOT_SPECIFIED = Messages.getString( "MSG_ERR_ENUM_CLASS_NOT_SPECIFIED" );    //$NON-NLS-1$
  String FMT_ERR_ENUM_CLASS_NOT_FOUND     = Messages.getString( "FMT_ERR_ENUM_CLASS_NOT_FOUND" );        //$NON-NLS-1$
  String FMT_ERR_KEEPER_CLASS_NOT_FOUND   = Messages.getString( "FMT_ERR_KEEPER_CLASS_NOT_FOUND" );      //$NON-NLS-1$
  String FMT_ERR_EMPTY_ENUM               = Messages.getString( "FMT_ERR_EMPTY_ENUM" );                  //$NON-NLS-1$

}
