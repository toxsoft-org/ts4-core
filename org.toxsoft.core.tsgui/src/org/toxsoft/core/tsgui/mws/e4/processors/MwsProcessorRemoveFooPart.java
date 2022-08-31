package org.toxsoft.core.tsgui.mws.e4.processors;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;

import java.util.*;

import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;

/**
 * Processor removes uipart {@link IMwsCoreConstants#MWSID_PART_FOO} from the e4 model.
 * <p>
 * Processor should be added to the plugin.xml - Extensions - org.rclipse.e4.workbench.model processors. Specify
 * <code>beforefragment = false</code>, and <code>apply = always</code>.
 *
 * @author hazard157
 */
public class MwsProcessorRemoveFooPart
    extends MwsAbstractProcessor {

  @Override
  protected void doProcess() {
    MApplication app = eclipseContext().get( MApplication.class );
    EModelService ms = eclipseContext().get( EModelService.class );
    MPartStack partStack = findElement( ms, app, MWSID_PARTSTACK_DEFAULT, MPartStack.class, EModelService.ANYWHERE );
    MPart fooPart = findElement( ms, app, MWSID_PART_FOO, MPart.class, EModelService.ANYWHERE );
    partStack.getChildren().remove( fooPart );
  }

  @SuppressWarnings( "rawtypes" )
  <T> T findElement( EModelService aModelService, MElementContainer aRoot, String aId, Class<T> aClass, int aFlags ) {
    ElementMatcher matcher = new ElementMatcher( aId, aClass, (String)null );
    List<T> elems = aModelService.findElements( aRoot, aClass, aFlags, matcher );
    if( elems.isEmpty() ) {
      return null;
    }
    return elems.get( 0 );
  }

}
