package org.toxsoft.core.tsgui.ved.screen.items;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * VISEL interceptor: checks that property value with ID = PROPID_VISEL_PROP_ID exists and is instance of allowed Class.
 *
 * @author vs
 */
public class VedActorInterceptorViselPropertyValidator
    implements IVedItemPropertyChangeInterceptor<VedAbstractActor> {

  private boolean valid = true;

  private final IList<Class<?>> classesList;

  private final ITsGuiContext tsContext;

  /**
   * Validates property with ID = PROPID_VISEL_PROP_ID.
   *
   * @param aAllowedClasses IList&lt;Class&lt;?>> - list of allowed classes (may be {@link IList#EMPTY}
   * @param aTsContext {@link ITsGuiContext} - corresponding context
   */
  public VedActorInterceptorViselPropertyValidator( IList<Class<?>> aAllowedClasses, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    if( !aAllowedClasses.isEmpty() ) {
      classesList = new ElemArrayList<>( aAllowedClasses );
    }
    else {
      classesList = IList.EMPTY;
    }
  }

  // ------------------------------------------------------------------------------------
  // IPropertyChangeInterceptor
  //

  @Override
  public void interceptPropsChange( VedAbstractActor aSource, IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    if( aNewValues.hasKey( PROPID_VISEL_PROP_ID ) && aSource.props().hasKey( PROPID_VISEL_ID ) ) {
      String viselId = aSource.props().getStr( PROPID_VISEL_ID );
      VedAbstractVisel visel = aSource.findVisel( viselId );
      if( visel != null ) {
        String propId = aNewValues.getStr( PROPID_VISEL_PROP_ID );
        if( !VedEditorUtils.isViselProperyId( propId, visel, tsContext ) ) {
          valid = false;
          TsDialogUtils.error( tsContext.get( Shell.class ), "Proprty with ID: %s does not exists", propId ); //$NON-NLS-1$
          return;
        }
        for( Class<?> clazz : classesList ) {
          if( VedEditorUtils.isPropertyClass( clazz, propId, visel, tsContext ) ) {
            valid = true;
            return;
          }
        }
        valid = false;
        TsDialogUtils.error( tsContext.get( Shell.class ), "Wrong property type.\nMust be %s", classesList.toString() ); //$NON-NLS-1$
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns result of checking last of property with ID = PROPID_VISEL_PROP_ID.
   *
   * @return <b>true</b> - last value of property with ID = PROPID_VISEL_PROP_ID is valid<br>
   *         <b>false</b> - last value of property with ID = PROPID_VISEL_PROP_ID is not valid
   */
  public boolean isValid() {
    return valid;
  }

}
