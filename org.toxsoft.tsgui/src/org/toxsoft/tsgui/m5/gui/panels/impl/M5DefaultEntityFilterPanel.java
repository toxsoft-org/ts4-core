package org.toxsoft.tsgui.m5.gui.panels.impl;

import static org.toxsoft.tsgui.m5.IM5Constants.*;
import static org.toxsoft.tsgui.m5.gui.panels.impl.ITsResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5.*;
import org.toxsoft.tsgui.m5.gui.panels.IM5FilterPanel;
import org.toxsoft.tsgui.panels.lazy.AbstractLazyPanel;
import org.toxsoft.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.tslib.bricks.filter.ITsFilter;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.txtmatch.ETextMatchMode;
import org.toxsoft.tslib.utils.txtmatch.TextMatcher;

/**
 * Default implementation of {@link IM5FilterPanel}.
 * <p>
 * Default filter panel has one {@link Text} where user enters the text to search for. Panel create filter (in method
 * {@link #getFilter()}) which searches specified text matches in the fields with {@link IM5Constants#M5FF_COLUMN}. Text
 * is searched using {@link ETextMatchMode#CONTAINS} mode.
 * <p>
 * Empty content of the {@link Text} control causes <code>null</code> to be returned as the filter {@link #getFilter()}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5DefaultEntityFilterPanel<T>
    extends AbstractLazyPanel<Control>
    implements IM5FilterPanel<T> {

  /**
   * The implementation of the filter returned by method {@link IM5FilterPanel#getFilter()}.
   *
   * @author hazard157
   */
  class FilterImpl
      implements ITsFilter<T> {

    private final TextMatcher textMatcher;

    public FilterImpl( String aSearchText ) {
      textMatcher = new TextMatcher( ETextMatchMode.CONTAINS, aSearchText, false );
    }

    @Override
    public boolean accept( T aEntity ) {
      for( IM5FieldDef<T, ?> fdef : searchFields ) {
        String fieldValueString = fdef.getter().getName( aEntity );
        if( textMatcher.match( fieldValueString ) ) {
          return true;
        }
      }
      return false;
    }

  }

  private final GenericChangeEventer               eventer;
  private final IM5Model<T>                        model;
  private final IStridablesList<IM5FieldDef<T, ?>> searchFields;

  private Text text;

  private ITsFilter<T> filter = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - entity model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5DefaultEntityFilterPanel( ITsGuiContext aContext, IM5Model<T> aModel ) {
    super( aContext );
    TsNullArgumentRtException.checkNull( aModel );
    eventer = new GenericChangeEventer( this );
    model = aModel;
    // list fields to be searched
    IStridablesListEdit<IM5FieldDef<T, ?>> ll = new StridablesList<>();
    for( IM5FieldDef<T, ?> fDef : model().fieldDefs() ) {
      if( fDef.hasFlag( M5FF_COLUMN ) ) {
        ll.add( fDef );
      }
    }
    searchFields = ll;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void whenWidgetsContentChanged() {
    String str = text.getText();
    if( !str.isEmpty() ) {
      filter = new FilterImpl( str );
    }
    else {
      filter = null;
    }
    eventer.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    text = new Text( aParent, SWT.BORDER );
    text.setMessage( TXT_M_TEXT );
    text.setToolTipText( TXT_P_TEXT );
    text.addModifyListener( aEvent -> whenWidgetsContentChanged() );
    return text;
  }

  // ------------------------------------------------------------------------------------
  // IM5ModelRelated
  //

  @Override
  public IM5Model<T> model() {
    return model;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IM5FilterPanel
  //

  @Override
  public ITsFilter<T> getFilter() {
    return filter;
  }

  @Override
  public void reset() {
    text.setText( TsLibUtils.EMPTY_STRING );
  }

}
