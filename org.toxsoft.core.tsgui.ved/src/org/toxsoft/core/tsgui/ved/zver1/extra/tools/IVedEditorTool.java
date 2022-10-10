package org.toxsoft.core.tsgui.ved.zver1.extra.tools;

import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.zver1.core.library.*;
import org.toxsoft.core.tsgui.ved.zver1.core.view.*;
import org.toxsoft.core.tsgui.ved.zver1.incub.*;
import org.toxsoft.core.tsgui.ved.zver1.utils.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;

/**
 * Editor tool is a means of special visual editing of a component.
 * <p>
 * The only allowed implementation of this inteface is {@link VedAbstractEditorTool} class.
 *
 * @author hazard157
 */
public interface IVedEditorTool
    extends IStridableParameterized, IIconIdable, IVedContextable, IDisposable {

  IVedComponentProvider componentProvider();

  IVedScreenDecorator screenDecorator();

  IVedViewDecorator viewDecorator();

  ITsUserInputListener screenInputListener();

  ITsCollectionChangeListener componentsListListener();

  IVedScreen vedScreen();

  void activate();

  void deactivate();

}
