package org.toxsoft.core.tsgui.panels.opsedit.group;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * The panel to edit several sections at once.
 * <p>
 * Panel consists of:
 * <ul>
 * <li>list of sections at the left side;</li>
 * <li>options values editors at right side - displayes known options of the selected section in the left list.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IPanelOptionSectionsEdit
    extends ILazyControl<Control> {

  IStridablesList<ISectionDef> getSections();

  void setSections( IStridablesList<ISectionDef> aSections );

  IStringMap<IOptionSet> listChangedValues();

  void resetChangedValues();

  void addSectionValuesChangeListener( ISectionValuesChangeListener aListener );

  void removeSectionValuesChangeListener( ISectionValuesChangeListener aListener );

}
