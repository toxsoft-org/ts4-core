package org.toxsoft.core.tsgui.ved.olds.api;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.filebound.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;

public interface IVedDataModel
    extends IKeepedContent {

  INotifierStridablesListEdit<IVedComponent> comps();

  // TODO canvas properties

}

interface IVedDataModel2
    extends IKeepedContent {

  INotifierStridablesList<IVedComponent> comps();

  void addComponent( IVedComponent aComp );

  IVedComponent createComponent( String aLibraryId, String aProviderId, IOptionSet aProps, IOptionSet aExtdata );

  // TODO canvas properties

}
