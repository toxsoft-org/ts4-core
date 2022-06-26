package org.toxsoft.core.tsgui.ved.olds.api;

import org.toxsoft.core.tsgui.ved.olds.incub.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;

public interface IVedComponent
    extends //
    IStridable, //
    IPropertableEdit, //
    IVedShape //
{

  String libraryId();

  String providerId();

  // capbilites constants
  IOptionSet capabilities();

  // external, expansible data
  IOptionSet extdata();

  IVedComponentView createView( /* ??? screen environment */ );

}
