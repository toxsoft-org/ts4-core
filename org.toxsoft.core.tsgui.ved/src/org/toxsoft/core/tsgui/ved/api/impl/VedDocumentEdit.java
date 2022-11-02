package org.toxsoft.core.tsgui.ved.api.impl;

import static org.toxsoft.core.tsgui.ved.api.IVedFrameworkConstants.*;

import org.toxsoft.core.tsgui.ved.api.comp.*;
import org.toxsoft.core.tsgui.ved.api.doc.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * {@link IVedDocumentEdit} implementation.
 *
 * @author hazard157
 */
public class VedDocumentEdit
    implements IVedDocumentEdit {

  private final VedEntityManagerEdit<IVedComponent> emComps   = new VedEntityManagerEdit<>( EVedEntityKind.COMPONENT );
  private final VedEntityManagerEdit<IVedTailor>    emTailors = new VedEntityManagerEdit<>( EVedEntityKind.TAILOR );
  private final VedEntityManagerEdit<IVedActor>     emActors  = new VedEntityManagerEdit<>( EVedEntityKind.ACTOR );

  private final GenericChangeEventer genericChangeEventer;
  private final PropertiesSet        props;

  /**
   * Constructor.
   */
  public VedDocumentEdit() {
    genericChangeEventer = new GenericChangeEventer( this );
    props = new PropertiesSet( VED_DOCUMENT_PROP_DEFS );
    props.propsEventer().addListener( ( src, pId, oldVal, newVal ) -> genericChangeEventer.fireChangeEvent() );
    emComps.genericChangeEventer().addListener( genericChangeEventer );
    emTailors.genericChangeEventer().addListener( genericChangeEventer );
    emActors.genericChangeEventer().addListener( genericChangeEventer );
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  public IPropertiesSet props() {
    return props;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    genericChangeEventer.pauseFiring();
    emActors.items().pauseFiring();
    emTailors.items().pauseFiring();
    emComps.items().pauseFiring();
    props().propsEventer().pauseFiring();
    try {
      // reset properties to defaults
      for( IDataDef p : VED_DOCUMENT_PROP_DEFS ) {
        props.setValue( p, p.defaultValue() );
      }
      // clear entities managers
      emActors.clear();
      emTailors.clear();
      emComps.clear();
      // fire the change event
      genericChangeEventer.fireChangeEvent();
    }
    finally {
      props.propsEventer().resumeFiring( true );
      emComps.items().resumeFiring( true );
      emTailors.items().resumeFiring( true );
      emActors.items().resumeFiring( true );
      genericChangeEventer.resumeFiring( true );
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IVedDocumentEdit
  //

  @Override
  public IVedEntityManagerEdit<IVedComponent> components() {
    return emComps;
  }

  @Override
  public IVedEntityManagerEdit<IVedTailor> tailors() {
    return emTailors;
  }

  @Override
  public IVedEntityManagerEdit<IVedActor> actors() {
    return emActors;
  }

}