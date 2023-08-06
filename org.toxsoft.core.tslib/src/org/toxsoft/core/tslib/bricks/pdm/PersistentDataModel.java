package org.toxsoft.core.tslib.bricks.pdm;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base abstract implementation of {@link IPersistentDataModel}.
 *
 * @author hazard157
 * @param <C> - the type of the content, extends {@link IPdmContent}
 * @param <M> - the type of the memento, extends {@link IPdmMemento}
 */
public abstract class PersistentDataModel<C extends IPdmContent, M extends IPdmMemento>
    implements IPersistentDataModel {

  private final GenericChangeEventer genericChangeEventer;

  private final Class<C> contentClass;
  private final Class<M> mementoClass;

  /**
   * Constructor for subclasses.
   *
   * @param aContentClass {@link Class}&lt;C&gt; - the type of the content, extends {@link IPdmContent}
   * @param aMementoClass {@link Class}&lt;M&gt; - the type of the memento, extends {@link IPdmMemento}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected PersistentDataModel( Class<C> aContentClass, Class<M> aMementoClass ) {
    TsNullArgumentRtException.checkNulls( aContentClass, aMementoClass );
    genericChangeEventer = new GenericChangeEventer( this ) {

      @Override
      protected void whenChanged() {
        PersistentDataModel.this.whenChanged();
      }
    };
    contentClass = aContentClass;
    mementoClass = aMementoClass;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  final public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableEx
  //

  @Override
  final public void clear() {
    if( isClearContent() ) {
      return;
    }
    doClear();
  }

  @Override
  public abstract boolean isClearContent();

  // ------------------------------------------------------------------------------------
  // ITsValidatable
  //

  @Override
  public abstract ValidationResult validate();

  // ------------------------------------------------------------------------------------
  // IPersistentDataModel
  //

  @Override
  final public C createClearPdmContent() {
    Object content = doCreateClearContent();
    TsInternalErrorRtException.checkNull( content );
    TsInternalErrorRtException.checkFalse( contentClass.isInstance( content ) );
    return contentClass.cast( content );
  }

  @Override
  final public IPdmContent getPdmContent() {
    Object content = doGetPdmContent();
    TsInternalErrorRtException.checkNull( content );
    TsInternalErrorRtException.checkFalse( contentClass.isInstance( content ) );
    return contentClass.cast( content );
  }

  @Override
  public void setPdmContent( IPdmContent aContent ) {
    TsNullArgumentRtException.checkNull( aContent );
    TsInternalErrorRtException.checkFalse( contentClass.isInstance( aContent ) );
    doSetPdmContent( contentClass.cast( aContent ) );
  }

  @Override
  public boolean isMemetoSupported() {
    return doIsMementoSupported();
  }

  @Override
  public IPdmMemento getMemento() {
    TsUnsupportedFeatureRtException.checkFalse( isMemetoSupported() );
    Object memento = doGetPdmMemento();
    TsInternalErrorRtException.checkNull( memento );
    TsInternalErrorRtException.checkFalse( mementoClass.isInstance( memento ) );
    return mementoClass.cast( memento );
  }

  @Override
  public void setMemento( IPdmMemento aMemento ) {
    TsUnsupportedFeatureRtException.checkFalse( isMemetoSupported() );
    TsNullArgumentRtException.checkNull( aMemento );
    TsInternalErrorRtException.checkFalse( mementoClass.isInstance( aMemento ) );
    doSetPdmMemento( mementoClass.cast( aMemento ) );
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must reset content to the clear state.
   * <p>
   * Called from {@link #clear()} only when {@link #isClearContent()} = <code>false</code>.
   */
  protected abstract void doClear();

  /**
   * Subclass must create the instance of the cleared (empty) content.
   *
   * @return &lt;C&gt; - the empty content, not <code>null</code>
   */
  protected abstract C doCreateClearContent();

  /**
   * Subclass must create the instance of the content.
   *
   * @return &lt;C&gt; - the content, not <code>null</code>
   */
  protected abstract C doGetPdmContent();

  /**
   * Subclass must set content from the specified content.
   * <p>
   * Subclass must generate change event on any change.
   *
   * @param aContent &lt;C&gt; - the content, not <code>null</code>
   */
  protected abstract void doSetPdmContent( C aContent );

  /**
   * Determines if memento is supported.
   * <p>
   * If memento is supported then {@link #doGetPdmMemento()} and {@link #doSetPdmMemento(IPdmMemento)} methods must be
   * implemented by the subclass.
   *
   * @return boolean - <code>true</code> memento can be used
   */
  protected abstract boolean doIsMementoSupported();

  /**
   * When overriding subclass must create the instance of the memento.
   * <p>
   * Throws exception {@link TsUnsupportedFeatureRtException} in base class, parent methods must not be called when
   * overriding.
   *
   * @return &lt;M&gt; - the memento, not <code>null</code>
   */
  protected M doGetPdmMemento() {
    throw new TsUnderDevelopmentRtException();
  }

  /**
   * When overriding subclass must set content from the specified memento.
   * <p>
   * Subclass must generate change event on any change.
   * <p>
   * Throws exception {@link TsUnsupportedFeatureRtException} in base class, parent methods must not be called when
   * overriding.
   *
   * @param aMemento &lt;M&gt; - the memento, not <code>null</code>
   */
  protected void doSetPdmMemento( M aMemento ) {
    throw new TsUnderDevelopmentRtException();
  }

  /**
   * Called every time when model changes.
   * <p>
   * Subclass may perform addition actions before events are actually fired.
   * <p>
   * In the base class does nothing there is no need to call superclass method when overriding.
   */
  protected void whenChanged() {
    // nop
  }

}
